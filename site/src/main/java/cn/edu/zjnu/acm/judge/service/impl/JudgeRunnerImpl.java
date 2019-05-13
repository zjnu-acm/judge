/*
 * Copyright 2019 ZJNU ACM.
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
package cn.edu.zjnu.acm.judge.service.impl;

import cn.edu.zjnu.acm.judge.service.DeleteService;
import cn.edu.zjnu.acm.judge.service.JudgeRunner;
import cn.edu.zjnu.acm.judge.support.JudgeData;
import cn.edu.zjnu.acm.judge.support.RunRecord;
import cn.edu.zjnu.acm.judge.support.RunResult;
import cn.edu.zjnu.acm.judge.util.Platform;
import com.github.zhanhb.judge.common.ExecuteResult;
import com.github.zhanhb.judge.common.JudgeBridge;
import com.github.zhanhb.judge.common.Options;
import com.github.zhanhb.judge.common.Status;
import com.github.zhanhb.judge.common.Validator;
import com.github.zhanhb.judge.win32.ProcessCreationHelper;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author zhanhb
 */
@RequiredArgsConstructor
@Service("judgeRunner")
@Slf4j
public class JudgeRunnerImpl implements JudgeRunner {

    private static final File NULL_FILE = Paths.get(Platform.isWindows() ? "NUL" : "/dev/null").toFile();

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

    private final DeleteService deleteService;
    private JudgeBridge judgeBridge;

    @PostConstruct
    public void init() {
        judgeBridge = new JudgeBridge();
    }

    @PreDestroy
    public void shutdown() {
        judgeBridge.close();
    }

    private void delete(Path path) {
        Objects.requireNonNull(path, "path");
        deleteService.delete(path);
    }

    @Override
    public RunResult run(RunRecord runRecord, Path workDirectory, JudgeData judgeData,
            Validator validator, boolean cleanDirectory) {
        Objects.requireNonNull(runRecord);
        Objects.requireNonNull(workDirectory);
        try {
            String[] message = new String[1];
            if (!compile(runRecord, workDirectory, message)) {
                return RunResult.builder().type(Status.COMPILATION_ERROR).message(message[0]).build();
            }
            return runProcess(runRecord, judgeData, workDirectory, validator);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        } finally {
            if (cleanDirectory) {
                delete(workDirectory);
            }
        }
    }

    private RunResult runProcess(RunRecord runRecord, JudgeData judgeData,
            Path work, Validator validator) throws IOException {
        int caseNum = judgeData.getCaseCount();
        ArrayList<String> details = new ArrayList<>(caseNum << 2);
        String command = runRecord.getLanguage().getExecuteCommand();

        // executable command should be absolute
        command = StringUtils.hasText(command) ? command : work.toAbsolutePath().resolve("Main." + runRecord.getLanguage().getExecutableExtension()).toString();
        long extTime = runRecord.getLanguage().getExtTime();
        long castTimeLimit = runRecord.getTimeLimit() * runRecord.getLanguage().getTimeFactor() + extTime;
        long extraMemory = runRecord.getLanguage().getExtMemory(); //内存附加
        long caseMemoryLimit = (runRecord.getMemoryLimit() + extraMemory) * 1024;
        Options[] opts = new Options[caseNum];
        for (int cas = 0; cas < caseNum; cas++) {
            Path[] entry = judgeData.get(cas);
            Path in = entry[0];
            Path standard = entry[1];
            Path progOutput = work.resolve(standard.getFileName());

            opts[cas] = Options.builder()
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
        String scorePerCase = new DecimalFormat("0.#").format(100.0 / caseNum);
        long time = 0;
        long memory = 0;
        int accept = 0; // final case who's result is accepted.
        ExecuteResult[] ers = judgeBridge.judge(opts, false, validator);
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
        log.debug("{}", details);
        int score = accept >= 0 ? (int) Math.round(accept * 100.0 / caseNum) : accept;
        if (score == 0 && accept != 0) {
            ++score;
        } else if (score == 100 && accept != caseNum) {
            --score;
        }
        String msg = details.stream().map(String::valueOf).collect(Collectors.joining(","));
        return RunResult.builder().score(score).time(time).memory(memory).message(msg).build();
    }

    private boolean compile(RunRecord runRecord, Path work, String[] message)
            throws IOException {
        String source = runRecord.getSource();
        if (StringUtils.isEmpty(source)) {
            return false;
        }
        final String main = "Main";
        Files.createDirectories(work);
        Path sourceFile = work.resolve(main + "." + runRecord.getLanguage().getSourceExtension()); //源码码文件
        Files.write(sourceFile, source.getBytes(Platform.getCharset()));

        String compileCommand = runRecord.getLanguage().getCompileCommand();
        log.debug("Compile Command: {}", compileCommand); //编译命令
        if (!StringUtils.hasText(compileCommand)) {
            return true;
        }
        // create compiling process
        // VC++ will output compiling info to stdout
        // G++ will output compiling info to stderr
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
        // export compiling infomation
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
        message[0] = errorInfo;
        log.debug("errorInfo = {}", errorInfo);
        // The executable file after compiling
        Path executable = work.resolve(main + "." + runRecord.getLanguage().getExecutableExtension());
        log.debug("executable = {}", executable);
        return Files.exists(executable);
    }

}
