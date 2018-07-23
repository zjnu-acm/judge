/*
 * Copyright 2015 ZJNU ACM.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.edu.zjnu.acm.judge.service;

import cn.edu.zjnu.acm.judge.config.JudgeConfiguration;
import cn.edu.zjnu.acm.judge.data.dto.RunRecord;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.domain.Submission;
import cn.edu.zjnu.acm.judge.exception.BusinessCode;
import cn.edu.zjnu.acm.judge.exception.BusinessException;
import cn.edu.zjnu.acm.judge.mapper.SubmissionMapper;
import cn.edu.zjnu.acm.judge.mapper.UserProblemMapper;
import cn.edu.zjnu.acm.judge.util.ResultType;
import com.github.zhanhb.judge.common.ExecuteResult;
import com.github.zhanhb.judge.common.JudgeBridge;
import com.github.zhanhb.judge.common.JudgeException;
import com.github.zhanhb.judge.common.Options;
import com.github.zhanhb.judge.common.SimpleValidator;
import com.github.zhanhb.judge.common.Validator;
import com.github.zhanhb.judge.win32.ProcessCreationHelper;
import com.github.zhanhb.judge.win32.SpecialValidator;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 *
 * @author zhanhb
 */
@Service
@Slf4j
public class JudgeService {

    private static final File NULL_FILE = Paths.get(Platform.isWindows() ? "NUL" : "/dev/null").toFile();
    private static final int MAX_SLEEPS = 9;

    private static String collectLines(Path path) throws IOException {
        Charset charset = Platform.getCharset();
        String compileInfo;
        try (InputStream is = Files.newInputStream(path);
                InputStreamReader isr = new InputStreamReader(is, charset);
                BufferedReader br = new BufferedReader(isr)) {
            compileInfo = br.lines().collect(Collectors.joining("\n"));
        }
        return compileInfo.length() > 1000 ? compileInfo.substring(0, 997) + "..." : compileInfo;
    }

    @Autowired
    private SubmissionMapper submissionMapper;
    @Autowired
    private UserProblemMapper userProblemMapper;
    @Autowired
    private ProblemService problemService;
    @Autowired
    private JudgeConfiguration judgeConfiguration;
    @Autowired
    private LanguageService languageService;
    @Autowired
    private DeleteService deleteService;

    private void updateSubmissionStatus(RunRecord runRecord) {
        userProblemMapper.update(runRecord.getUserId(), runRecord.getProblemId());
        userProblemMapper.updateUser(runRecord.getUserId());
        userProblemMapper.updateProblem(runRecord.getProblemId());
    }

    CompletableFuture<?> toCompletableFuture(Executor executor, long submissionId) {
        return CompletableFuture.runAsync(() -> {
            Submission submission = submissionMapper.findOne(submissionId);
            if (submission == null) {
                throw new BusinessException(BusinessCode.SUBMISSION_NOT_FOUND);
            }
            Problem problem = problemService.findOneNoI18n(submission.getProblem());
            RunRecord runRecord = RunRecord.builder()
                    .submissionId(submission.getId())
                    .language(languageService.getAvailableLanguage(submission.getLanguage()))
                    .problemId(submission.getProblem())
                    .userId(submission.getUser())
                    .source(submissionMapper.findSourceById(submissionId))
                    .memoryLimit(problem.getMemoryLimit())
                    .timeLimit(problem.getTimeLimit())
                    .build();
            judgeInternal(runRecord);
        }, executor);
    }

    private void runProcess(RunRecord runRecord) throws IOException {
        Path dataPath = judgeConfiguration.getDataDirectory(runRecord.getProblemId());
        Objects.requireNonNull(dataPath, "dataPath");
        Path specialFile = dataPath.resolve(JudgeConfiguration.VALIDATE_FILE_NAME);
        boolean isSpecial = Files.exists(specialFile);
        if (!Files.isDirectory(dataPath)) {
            log.error("{} not exists", dataPath);
            return;
        }
        List<Path[]> files = new ArrayList<>(20);
        try (DirectoryStream<Path> listFiles = Files.newDirectoryStream(dataPath)) {
            log.debug("dataPath = {}", dataPath);
            for (Path inFile : listFiles) {
                String inFileName = inFile.getFileName().toString();
                if (!inFileName.toLowerCase().endsWith(".in")) {
                    continue;
                }
                Path outFile = dataPath.resolve(inFileName.substring(0, inFileName.length() - 3) + ".out");
                if (!Files.exists(outFile)) {
                    continue;
                }
                files.add(new Path[]{inFile, outFile});//统计输入,输出文件
            }
        }
        int caseNum = files.size();
        log.debug("caseNum = {}", caseNum);
        if (caseNum == 0) {
            log.error("No test cases found for problem({})", runRecord.getProblemId());
            return;
        }
        int accept = 0; //最后通过的个数
        ArrayList<String> details = new ArrayList<>(caseNum << 2);
        String command = runRecord.getLanguage().getExecuteCommand();
        Path work = judgeConfiguration.getWorkDirectory(runRecord.getSubmissionId()); //建立临时文件
        command = StringUtils.hasText(command) ? command : work.resolve("Main." + runRecord.getLanguage().getExecutableExtension()).toString();
        long extTime = runRecord.getLanguage().getExtTime();
        long castTimeLimit = runRecord.getTimeLimit() * runRecord.getLanguage().getTimeFactor() + extTime;
        long extraMemory = runRecord.getLanguage().getExtMemory(); //内存附加
        long caseMemoryLimit = (runRecord.getMemoryLimit() + extraMemory) * 1024;
        Options[] optionses = new Options[caseNum];
        for (int cas = 0; cas < caseNum; cas++) {
            Path[] entry = files.get(cas);
            Path in = entry[0];
            Path standard = entry[1];
            Path progOutput = work.resolve(standard.getFileName());

            optionses[cas] = Options.builder()
                    .timeLimit(castTimeLimit) // time limit
                    .memoryLimit(caseMemoryLimit) // memory in bytes
                    .outputLimit(16 * 1024 * 1024) // 16M
                    .command(command)
                    .workDirectory(work)
                    .inputFile(in)
                    .outputFile(progOutput)
                    .standardOutput(standard)
                    .errFile(NULL_FILE.toPath())
                    .build();
        }
        String detailMessageStr = null;
        String scorePerCase = new DecimalFormat("0.#").format(100.0 / caseNum);
        final Validator validator = isSpecial
                ? new SpecialValidator(specialFile.toString(), work)
                : SimpleValidator.PE_AS_ACCEPTED;
        long time = 0; //时间
        long memory = 0; //内存
        try {
            ExecuteResult[] ers = JudgeBridge.INSTANCE.judge(optionses, false, validator);
            for (ExecuteResult er : ers) {
                long tim1 = Math.max(0, er.getTime() - extTime);
                long mem1 = Math.max(0, er.getMemory() / 1024 - extraMemory);
                String message = er.getMessage();
                boolean success = er.isSuccess();
                time = Math.max(time, tim1);
                memory = Math.max(memory, mem1);
                log.debug("message = {}, time = {}, memory = {}", message, time, memory);

                details.add(String.valueOf(er.getCode().getResult()));
                details.add(success ? scorePerCase : "0");
                details.add(String.valueOf(tim1));
                details.add(String.valueOf(mem1));
                if (success) {
                    ++accept;
                }
            }
        } catch (JudgeException | RuntimeException | Error ex) {
            log.error("", ex);
            accept = ResultType.SYSTEM_ERROR;
            detailMessageStr = ex.getMessage();
        }
        log.debug("{}", details);
        int score = accept >= 0 ? (int) Math.round(accept * 100.0 / caseNum) : accept;
        if (score == 0 && accept != 0) {
            ++score;
        } else if (score == ResultType.SCORE_ACCEPT && accept != caseNum) {
            --score;
        }
        submissionMapper.updateResult(runRecord.getSubmissionId(), score, time, memory);
        submissionMapper.saveDetail(runRecord.getSubmissionId(), detailMessageStr != null ? detailMessageStr : details.stream().map(String::valueOf).collect(Collectors.joining(",")));
        updateSubmissionStatus(runRecord);
    }

    private boolean compile(RunRecord runRecord) throws IOException {
        String source = runRecord.getSource();
        if (StringUtils.isEmpty(source)) {
            return false;
        }
        Path work = judgeConfiguration.getWorkDirectory(runRecord.getSubmissionId());
        final String main = "Main";
        Files.createDirectories(work);
        Path sourceFile = work.resolve(main + "." + runRecord.getLanguage().getSourceExtension()); //源码码文件
        Files.write(sourceFile, source.getBytes(Platform.getCharset()));

        String compileCommand = runRecord.getLanguage().getCompileCommand();
        log.debug("Compile Command: {}", compileCommand); //编译命令
        if (!StringUtils.hasText(compileCommand)) {
            return true;
        }
        assert compileCommand != null;
        //创建编译进程
        // VC++信息会输出在标准输出
        // G++编译信息输出在标准错误输出
        Path compileInfo = work.resolve("compileInfo.txt");
        Process process = ProcessCreationHelper.execute(new ProcessBuilder(compileCommand.split("\\s+"))
                .directory(work.toFile())
                .redirectInput(ProcessBuilder.Redirect.from(NULL_FILE))
                .redirectOutput(compileInfo.toFile())
                .redirectErrorStream(true)::start);
        try {
            process.waitFor(1, TimeUnit.MINUTES);
        } catch (InterruptedException ex) {
            throw new InterruptedIOException();
        }
        //编译信息导出
        String errorInfo;
        if (process.isAlive()) {
            process.destroyForcibly();
            try {
                process.waitFor();
            } catch (InterruptedException ex) {
                throw new InterruptedIOException();
            }
            errorInfo = "Compile timeout\nOutput:\n" + collectLines(compileInfo);
        } else {
            errorInfo = collectLines(compileInfo);
        }
        log.debug("errorInfo = {}", errorInfo);
        Path executable = work.resolve(main + "." + runRecord.getLanguage().getExecutableExtension()); //编译后可执行文件
        log.debug("executable = {}", executable);
        boolean compileOK = Files.exists(executable);
        //编译未通过
        if (!compileOK) {
            submissionMapper.updateResult(runRecord.getSubmissionId(), ResultType.COMPILE_ERROR, 0, 0);
            submissionMapper.saveCompileInfo(runRecord.getSubmissionId(), errorInfo);
            updateSubmissionStatus(runRecord);
        }
        return compileOK;
    }

    private void judgeInternal(RunRecord runRecord) {
        try (Closeable c = () -> delete(judgeConfiguration.getWorkDirectory(runRecord.getSubmissionId()))) {
            if (compile(runRecord)) {
                runProcess(runRecord);
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @SuppressWarnings("CallToThreadYield")
    private void delete(Path path) throws IOException {
        Objects.requireNonNull(path, "path");
        if (!judgeConfiguration.isDeleteTempFile()) {
            return;
        }
        deleteService.delete(path);
    }

}
