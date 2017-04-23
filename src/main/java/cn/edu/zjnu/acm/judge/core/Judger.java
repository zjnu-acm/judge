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
package cn.edu.zjnu.acm.judge.core;

import cn.edu.zjnu.acm.judge.config.JudgeConfiguration;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.domain.RunRecord;
import cn.edu.zjnu.acm.judge.domain.Submission;
import cn.edu.zjnu.acm.judge.mapper.ProblemMapper;
import cn.edu.zjnu.acm.judge.mapper.SubmissionMapper;
import cn.edu.zjnu.acm.judge.mapper.UserProblemMapper;
import cn.edu.zjnu.acm.judge.service.JudgeServerService;
import cn.edu.zjnu.acm.judge.service.LanguageService;
import cn.edu.zjnu.acm.judge.util.ResultType;
import com.github.zhanhb.judge.classic.JudgeBridge;
import com.github.zhanhb.judge.common.ExecuteResult;
import com.github.zhanhb.judge.common.JudgeException;
import com.github.zhanhb.judge.common.Options;
import com.github.zhanhb.judge.common.Validator;
import com.github.zhanhb.judge.impl.SimpleValidator;
import com.github.zhanhb.judge.impl.SpecialValidator;
import com.github.zhanhb.judge.win32.ProcessCreationHelper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
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
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

/**
 *
 * @author zhanhb
 */
@Service
@Slf4j
public class Judger {

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
    private ProblemMapper problemMapper;
    @Autowired
    private JudgeServerService judgeServerService;
    @Autowired
    private JudgeConfiguration judgeConfiguration;
    @Autowired
    private LanguageService languageService;
    private ExecutorService executorService;

    @PostConstruct
    public void init() {
        final ThreadGroup group = new ThreadGroup("judge group");
        final AtomicInteger countet = new AtomicInteger();
        final ThreadFactory threadFactory = runnable -> new Thread(group, runnable, "judge thread " + countet.incrementAndGet());
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), threadFactory);
    }

    @PreDestroy
    public void destroy() {
        executorService.shutdownNow();
    }

    private void updateSubmissionStatus(RunRecord runRecord) {
        userProblemMapper.update(runRecord.getUserId(), runRecord.getProblemId());
        userProblemMapper.updateUser(runRecord.getUserId());
        userProblemMapper.updateProblem(runRecord.getProblemId());
    }

    public void reJudge(long submissionId) throws InterruptedException, ExecutionException {
        Submission submission = submissionMapper.findOne(submissionId);
        if (submission == null) {
            return;
        }
        Problem problem = problemMapper.findOneNoI18n(submission.getProblem());
        if (problem == null) {
            return;
        }
        Path dataPath = judgeConfiguration.getDataDirectory(problem.getId());
        RunRecord runRecord = RunRecord.builder()
                .submissionId(submission.getId())
                .language(languageService.getAvailableLanguage(submission.getLanguage()))
                .problemId(submission.getProblem())
                .userId(submission.getUser())
                .source(submissionMapper.findSourceById(submissionId))
                .dataPath(dataPath)
                .memoryLimit(problem.getMemoryLimit())
                .timeLimit(problem.getTimeLimit())
                .build();
        executorService.submit(() -> {
            Path workDirectory = judgeConfiguration.getWorkDirectory(submissionId);
            judgeServerService.delete(workDirectory);
            judgeInternal(runRecord);
            judgeServerService.delete(workDirectory);
        }).get();
    }

    private boolean runProcess(RunRecord runRecord) throws IOException {
        Path dataPath = runRecord.getDataPath();
        Objects.requireNonNull(dataPath, "dataPath");
        Path specialFile = dataPath.resolve(JudgeConfiguration.VALIDATE_FILE_NAME);
        boolean isspecial = Files.exists(specialFile);
        if (!Files.isDirectory(dataPath)) {
            log.error("{} not exists", runRecord.getDataPath());
            return false;
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
        int casenum = files.size();
        log.debug("casenum = {}", casenum);
        if (casenum == 0) {
            return false;
        }
        int accept = 0; //最后通过的个数
        ArrayList<String> details = new ArrayList<>(casenum << 2);
        long time = 0; //时间
        long memory = 0; //内存
        String command = runRecord.getLanguage().getExecuteCommand();
        Path work = judgeConfiguration.getWorkDirectory(runRecord.getSubmissionId()); //建立临时文件
        command = !StringUtils.isEmptyOrWhitespace(command) ? command : work.resolve("Main." + runRecord.getLanguage().getExecutableExtension()).toString();
        long extTime = runRecord.getLanguage().getExtTime();
        long castTimeLimit = runRecord.getTimeLimit() * runRecord.getLanguage().getTimeFactor() + extTime;
        long extraMemory = runRecord.getLanguage().getExtMemory(); //内存附加
        long caseMemoryLimit = (runRecord.getMemoryLimit() + extraMemory) * 1024;
        Options[] optionses = new Options[casenum];
        for (int cas = 0; cas < casenum; cas++) {
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
                    .errFile(getNull(work))
                    .build();
        }
        String detailMessageStr = null;
        String scorePerCase = new DecimalFormat("0.#").format(100.0 / casenum);
        final Validator validator = isspecial
                ? new SpecialValidator(specialFile.toString(), work)
                : new SimpleValidator();
        try {
            ExecuteResult[] ers = JudgeBridge.INSTANCE.execute(optionses, false, validator);
            for (ExecuteResult er : ers) {
                long tim1 = er.getTime() - extTime;
                tim1 = Math.max(0, tim1);
                long mem1 = er.getMemory() / 1024 - extraMemory;
                mem1 = Math.max(0, mem1);
                String message = er.getMessage();
                int caseResult = getResultFromExecuteResult(er);
                time = Math.max(time, tim1);
                memory = Math.max(memory, mem1);
                log.debug("message = {}", message);
                log.debug("Time = {}, Memory = {}", time, memory);

                details.add(String.valueOf(caseResult));
                if (caseResult == 0) {
                    details.add(scorePerCase);
                } else {
                    details.add("0");
                }
                details.add(String.valueOf(tim1));
                details.add(String.valueOf(mem1));
                if (caseResult == 0) {
                    ++accept;
                }
            }
        } catch (JudgeException | RuntimeException | Error ex) {
            log.error("", ex);
            accept = ResultType.SYSTEM_ERROR;
            detailMessageStr = ex.getMessage();
        }
        log.debug("{}", details);
        int score = accept >= 0 ? (int) Math.round(accept * 100.0 / casenum) : accept;
        if (score == 0 && accept != 0) {
            ++score;
        } else if (score == 100 && accept != casenum) {
            --score;
        }
        submissionMapper.updateResult(runRecord.getSubmissionId(), score, time, memory);
        submissionMapper.saveDetail(runRecord.getSubmissionId(), detailMessageStr != null ? detailMessageStr : details.stream().map(String::valueOf).collect(Collectors.joining(",")));
        updateSubmissionStatus(runRecord);
        return score == 100;
    }

    private boolean compile(RunRecord runRecord) throws IOException {
        String source = runRecord.getSource();
        if (StringUtils.isEmptyOrWhitespace(source)) {
            return false;
        }
        Path previous = judgeConfiguration.getWorkDirectory(runRecord.getSubmissionId() - 1);
        judgeServerService.delete(previous);
        Path work = judgeConfiguration.getWorkDirectory(runRecord.getSubmissionId());
        final String main = "Main";
        Files.createDirectories(work);
        Path sourceFile = work.resolve(main + "." + runRecord.getLanguage().getSourceExtension()); //源码码文件
        Files.copy(new ByteArrayInputStream(source.getBytes(Platform.getCharset())), sourceFile, StandardCopyOption.REPLACE_EXISTING);

        String compileCommand = runRecord.getLanguage().getCompileCommand();
        log.debug("Compile Command: {}", compileCommand); //编译命令
        if (StringUtils.isEmptyOrWhitespace(compileCommand)) {
            return true;
        }
        assert compileCommand != null;
        //创建编译进程
        // VC++信息会输出在标准输出
        // G++编译信息输出在标准错误输出
        Path compileInfo = work.resolve("compileinfo.txt");
        Process process = ProcessCreationHelper.execute(new ProcessBuilder(compileCommand.split("\\s+"))
                .directory(work.toFile())
                .redirectOutput(compileInfo.toFile())
                .redirectErrorStream(true)::start);
        process.getInputStream().close();
        try {
            process.waitFor(45, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            throw new InterruptedIOException();
        }
        //编译信息导出
        String errorInfo;
        if (process.isAlive()) {
            process.destroy();
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

    public Future<?> judge(RunRecord runRecord) {
        return executorService.submit(() -> judgeInternal(runRecord));
    }

    private void judgeInternal(RunRecord runRecord) {
        try {
            if (compile(runRecord)) {
                runProcess(runRecord);
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    private int getResultFromExecuteResult(ExecuteResult er) {
        return er.getHaltCode().getResult();
    }

    private Path getNull(Path work) {
        return File.pathSeparatorChar == ';' ? work.resolve("NUL") : Paths.get("/dev/null");
    }

}
