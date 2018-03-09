/*
 * Copyright 2017 ZJNU ACM.
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
package com.github.zhanhb.judge.win32;

import com.github.zhanhb.jnc.platform.win32.Advapi32;
import com.github.zhanhb.jnc.platform.win32.Kernel32;
import com.github.zhanhb.jnc.platform.win32.Kernel32Util;
import com.github.zhanhb.jnc.platform.win32.PROCESS_INFORMATION;
import com.github.zhanhb.jnc.platform.win32.SECURITY_ATTRIBUTES;
import com.github.zhanhb.jnc.platform.win32.STARTUPINFO;
import com.github.zhanhb.jnc.platform.win32.WString;
import com.github.zhanhb.judge.common.ExecuteResult;
import com.github.zhanhb.judge.common.Executor;
import com.github.zhanhb.judge.common.Options;
import com.github.zhanhb.judge.common.Status;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import jnc.foreign.Pointer;

import static com.github.zhanhb.jnc.platform.win32.FileApi.CREATE_ALWAYS;
import static com.github.zhanhb.jnc.platform.win32.FileApi.OPEN_ALWAYS;
import static com.github.zhanhb.jnc.platform.win32.FileApi.OPEN_EXISTING;
import static com.github.zhanhb.jnc.platform.win32.WinBase.CREATE_BREAKAWAY_FROM_JOB;
import static com.github.zhanhb.jnc.platform.win32.WinBase.CREATE_NEW_PROCESS_GROUP;
import static com.github.zhanhb.jnc.platform.win32.WinBase.CREATE_NO_WINDOW;
import static com.github.zhanhb.jnc.platform.win32.WinBase.CREATE_SUSPENDED;
import static com.github.zhanhb.jnc.platform.win32.WinBase.CREATE_UNICODE_ENVIRONMENT;
import static com.github.zhanhb.jnc.platform.win32.WinBase.DETACHED_PROCESS;
import static com.github.zhanhb.jnc.platform.win32.WinBase.FILE_FLAG_DELETE_ON_CLOSE;
import static com.github.zhanhb.jnc.platform.win32.WinBase.FILE_FLAG_WRITE_THROUGH;
import static com.github.zhanhb.jnc.platform.win32.WinBase.HANDLE_FLAG_INHERIT;
import static com.github.zhanhb.jnc.platform.win32.WinBase.HIGH_PRIORITY_CLASS;
import static com.github.zhanhb.jnc.platform.win32.WinBase.STARTF_FORCEOFFFEEDBACK;
import static com.github.zhanhb.jnc.platform.win32.WinBase.STARTF_USESTDHANDLES;
import static com.github.zhanhb.jnc.platform.win32.WinNT.FILE_ATTRIBUTE_NORMAL;
import static com.github.zhanhb.jnc.platform.win32.WinNT.FILE_SHARE_READ;
import static com.github.zhanhb.jnc.platform.win32.WinNT.FILE_SHARE_WRITE;
import static com.github.zhanhb.jnc.platform.win32.WinNT.GENERIC_READ;
import static com.github.zhanhb.jnc.platform.win32.WinNT.GENERIC_WRITE;
import static com.github.zhanhb.judge.common.Constants.TERMINATE_TIMEOUT;
import static com.github.zhanhb.judge.common.Constants.UPDATE_TIME_THRESHOLD;

/**
 *
 * @author zhanhb
 */
public enum WindowsExecutor implements Executor {

    INSTANCE;

    private final Sandbox sandbox = new Sandbox();
    private final Pointer DESKTOP = WString.toNative("Winsta0\\default");
    private final Pointer EMPTY_ENV = WString.toNative("\000");

    private Handle fileOpen(Path path, int flags) {
        final int access
                = (flags & O_WRONLY) != 0 ? GENERIC_WRITE
                        : (flags & O_RDWR) != 0 ? (GENERIC_READ | GENERIC_WRITE)
                                : GENERIC_READ;
        final int sharing = FILE_SHARE_READ | FILE_SHARE_WRITE;
        /* Note: O_TRUNC overrides O_CREAT */
        final int disposition
                = (flags & O_TRUNC) != 0 ? CREATE_ALWAYS
                        : (flags & O_CREAT) != 0 ? OPEN_ALWAYS
                                : OPEN_EXISTING;
        final int maybeWriteThrough
                = (flags & (O_SYNC | O_DSYNC)) != 0
                        ? FILE_FLAG_WRITE_THROUGH
                        : FILE_ATTRIBUTE_NORMAL;
        final int maybeDeleteOnClose
                = (flags & O_TEMPORARY) != 0
                        ? FILE_FLAG_DELETE_ON_CLOSE
                        : FILE_ATTRIBUTE_NORMAL;

        final int flagsAndAttributes = maybeWriteThrough | maybeDeleteOnClose;
        long h = Kernel32.INSTANCE.CreateFileW(
                WString.toNative(path.toString()), /* Wide char path name */
                access, /* Read and/or write permission */
                sharing, /* File sharing flags */
                null, /* Security attributes */
                disposition, /* creation disposition */
                flagsAndAttributes, /* flags and attributes */
                0 /*NULL*/);
        return new Handle(h);
    }

    @Override
    public ExecuteResult execute(Options options) throws IOException {
        Path inputFile = options.getInputFile();
        Path outputPath = options.getOutputFile();
        boolean redirectErrorStream = options.isRedirectErrorStream();
        Path errorPath = options.getErrFile();
        Path workDirectory = options.getWorkDirectory();
        String command = options.getCommand();

        long timeLimit = options.getTimeLimit();
        long memoryLimit = options.getMemoryLimit();
        long outputLimit = options.getOutputLimit();

        PROCESS_INFORMATION pi;

        try (Handle hIn = fileOpen(inputFile, O_RDONLY);
                Handle hOut = fileOpen(outputPath, O_WRONLY | O_CREAT | O_TRUNC);
                Handle hErr = redirectErrorStream ? hOut : fileOpen(errorPath, O_WRONLY | O_CREAT | O_TRUNC)) {
            pi = createProcess(command, hIn.getValue(), hOut.getValue(), hErr.getValue(), redirectErrorStream, workDirectory);
        }

        try (Job job = new Job();
                Handle hProcess = new Handle(pi.getProcess());
                Handle hThread = new Handle(pi.getThread())) {
            JudgeProcess judgeProcess = new JudgeProcess(hProcess.getValue());
            try (FileChannel cOut = FileChannel.open(outputPath);
                    FileChannel cErr = redirectErrorStream ? cOut : FileChannel.open(errorPath)) {
                job.init();
                job.assignProcess(hProcess.getValue());

                int dwCount = Kernel32.INSTANCE.ResumeThread(hThread.getValue());
                Kernel32Util.assertTrue(dwCount != -1);
                hThread.close();

                Instant startTime = judgeProcess.getStartTime();
                while (true) {
                    long memory = judgeProcess.getPeakMemory();
                    if (memory > memoryLimit) {
                        judgeProcess.terminate(Status.MEMORY_LIMIT_EXCEED);
                        judgeProcess.join(TERMINATE_TIMEOUT);
                        break;
                    }
                    long time = ChronoUnit.MILLIS.between(startTime, Instant.now()) - 5000; // extra 5 seconds
                    if (time > timeLimit || judgeProcess.getTime() > timeLimit) {
                        judgeProcess.terminate(Status.TIME_LIMIT_EXCEED);
                        judgeProcess.join(TERMINATE_TIMEOUT);
                        break;
                    }
                    long dwWaitTime = timeLimit - time;
                    if (dwWaitTime > UPDATE_TIME_THRESHOLD) {
                        dwWaitTime = UPDATE_TIME_THRESHOLD;
                    }
                    if (judgeProcess.join(dwWaitTime)) {
                        break;
                    }
                    if (checkOle(cOut, cErr, redirectErrorStream, outputLimit)) {
                        judgeProcess.terminate(Status.OUTPUT_LIMIT_EXCEED);
                        judgeProcess.join(TERMINATE_TIMEOUT);
                        break;
                    }
                }
                if (checkOle(cOut, cErr, redirectErrorStream, outputLimit)) {
                    judgeProcess.terminate(Status.OUTPUT_LIMIT_EXCEED);
                }
            } finally {
                judgeProcess.terminate(Status.ACCEPTED);
            }
            judgeProcess.join(Long.MAX_VALUE);
            Status status = judgeProcess.getStatus();
            int exitCode = judgeProcess.getExitCode();
            if (status == Status.ACCEPTED && exitCode != 0) {
                status = Status.RUNTIME_ERROR;
            }
            long time = judgeProcess.getTime();
            if (status == Status.TIME_LIMIT_EXCEED) {
                time = ((time - timeLimit - 1) % 200 + 200) % 200 + 1 + timeLimit;
            }
            return ExecuteResult.builder()
                    .time(time)
                    .memory(judgeProcess.getPeakMemory())
                    .code(status)
                    .exitCode(exitCode)
                    .build();
        }
    }

    private void setInheritable(long handle) {
        Kernel32Util.assertTrue(Kernel32.INSTANCE.SetHandleInformation(handle, HANDLE_FLAG_INHERIT, HANDLE_FLAG_INHERIT));
    }

    private PROCESS_INFORMATION createProcess(String lpCommandLine,
            long /*HANDLE*/ hIn, long /*HANDLE*/ hOut, long /*HANDLE*/ hErr,
            boolean redirectErrorStream, Path lpCurrentDirectory) {
        String lpApplicationName = null;
        SECURITY_ATTRIBUTES lpProcessAttributes = null;
        SECURITY_ATTRIBUTES lpThreadAttributes = null;
        int dwCreationFlags
                = CREATE_SUSPENDED
                | DETACHED_PROCESS
                | HIGH_PRIORITY_CLASS
                | CREATE_NEW_PROCESS_GROUP
                | CREATE_UNICODE_ENVIRONMENT
                | CREATE_BREAKAWAY_FROM_JOB
                | CREATE_NO_WINDOW;
        STARTUPINFO lpStartupInfo = new STARTUPINFO();
        lpStartupInfo.setCb(lpStartupInfo.size());
        lpStartupInfo.setDesktop(DESKTOP.address());
        PROCESS_INFORMATION lpProcessInformation = new PROCESS_INFORMATION();

        // without cursor feed back
        lpStartupInfo.setFlags(STARTF_USESTDHANDLES | STARTF_FORCEOFFFEEDBACK);
        lpStartupInfo.setStdInput(hIn);
        lpStartupInfo.setStdOutput(hOut);
        lpStartupInfo.setStdError(hErr);

        setInheritable(hIn);
        setInheritable(hOut);
        if (!redirectErrorStream) {
            setInheritable(hErr);
        }

        try (Handle hToken = new Handle(sandbox.createRestrictedToken(
                TokenLevel.USER_LIMITED,
                IntegrityLevel.INTEGRITY_LEVEL_LOW,
                TokenType.PRIMARY,
                true))) {
            ProcessCreationHelper.execute(() -> Kernel32Util.assertTrue(Advapi32.INSTANCE.CreateProcessAsUserW(
                    hToken.getValue(),
                    WString.toNative(lpApplicationName), // executable name
                    WString.toNative(lpCommandLine),// command line
                    lpProcessAttributes, // process security attribute
                    lpThreadAttributes, // thread security attribute
                    true, // inherits system handles
                    dwCreationFlags, // selected based on exe type
                    EMPTY_ENV,
                    WString.toNative(Objects.toString(lpCurrentDirectory, null)),
                    lpStartupInfo,
                    lpProcessInformation)));
        }
        return lpProcessInformation;
    }

    private boolean checkOle(FileChannel outputPath, FileChannel errorPath,
            boolean redirectErrorStream, long outputLimit) throws IOException {
        return outputPath.size() > outputLimit
                || !redirectErrorStream && errorPath.size() > outputLimit;
    }

}
