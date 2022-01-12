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
package cn.edu.zjnu.acm.judge.sandbox.win32;

import cn.edu.zjnu.acm.judge.core.Constants;
import cn.edu.zjnu.acm.judge.core.ExecuteResult;
import cn.edu.zjnu.acm.judge.core.Executor;
import cn.edu.zjnu.acm.judge.core.NotExecutableException;
import cn.edu.zjnu.acm.judge.core.Option;
import cn.edu.zjnu.acm.judge.core.Status;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import jnc.foreign.Pointer;
import jnc.platform.win32.Advapi32;
import jnc.platform.win32.Handle;
import jnc.platform.win32.Kernel32;
import jnc.platform.win32.Kernel32Util;
import jnc.platform.win32.PROCESS_INFORMATION;
import jnc.platform.win32.STARTUPINFO;
import jnc.platform.win32.WString;
import jnc.platform.win32.Win32Exception;

import static jnc.platform.win32.FileApi.CREATE_ALWAYS;
import static jnc.platform.win32.FileApi.OPEN_ALWAYS;
import static jnc.platform.win32.FileApi.OPEN_EXISTING;
import static jnc.platform.win32.WinBase.CREATE_BREAKAWAY_FROM_JOB;
import static jnc.platform.win32.WinBase.CREATE_NEW_PROCESS_GROUP;
import static jnc.platform.win32.WinBase.CREATE_NO_WINDOW;
import static jnc.platform.win32.WinBase.CREATE_SUSPENDED;
import static jnc.platform.win32.WinBase.CREATE_UNICODE_ENVIRONMENT;
import static jnc.platform.win32.WinBase.DETACHED_PROCESS;
import static jnc.platform.win32.WinBase.FILE_FLAG_DELETE_ON_CLOSE;
import static jnc.platform.win32.WinBase.FILE_FLAG_WRITE_THROUGH;
import static jnc.platform.win32.WinBase.HANDLE_FLAG_INHERIT;
import static jnc.platform.win32.WinBase.HIGH_PRIORITY_CLASS;
import static jnc.platform.win32.WinBase.STARTF_FORCEOFFFEEDBACK;
import static jnc.platform.win32.WinBase.STARTF_USESTDHANDLES;
import static jnc.platform.win32.WinNT.FILE_ATTRIBUTE_NORMAL;
import static jnc.platform.win32.WinNT.FILE_SHARE_READ;
import static jnc.platform.win32.WinNT.FILE_SHARE_WRITE;
import static jnc.platform.win32.WinNT.GENERIC_READ;
import static jnc.platform.win32.WinNT.GENERIC_WRITE;

/**
 * @author zhanhb
 */
public class WindowsExecutor implements Executor {

    private final Pointer DESKTOP = WString.toNative("Winsta0\\default");
    private final Pointer EMPTY_ENV = WString.toNative("\000");
    private final Handle hToken;

    public WindowsExecutor() {
        hToken = Handle.of(Sandbox.INSTANCE.createRestrictedToken(
                TokenLevel.USER_LIMITED,
                IntegrityLevel.INTEGRITY_LEVEL_LOW,
                TokenType.PRIMARY,
                true, null));
    }

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
                = (flags & (Executor.O_SYNC | Executor.O_DSYNC)) != 0
                        ? FILE_FLAG_WRITE_THROUGH
                        : FILE_ATTRIBUTE_NORMAL;
        final int maybeDeleteOnClose
                = (flags & Executor.O_TEMPORARY) != 0
                        ? FILE_FLAG_DELETE_ON_CLOSE
                        : FILE_ATTRIBUTE_NORMAL;

        final int flagsAndAttributes = maybeWriteThrough | maybeDeleteOnClose;
        return Handle.of(Kernel32.INSTANCE.CreateFileW(
                WString.toNative(path.toString()), /* Wide char path name */
                access, /* Read and/or write permission */
                sharing, /* File sharing flags */
                null, /* Security attributes */
                disposition, /* creation disposition */
                flagsAndAttributes, /* flags and attributes */
                0 /*NULL*/));
    }

    @Override
    public ExecuteResult execute(Option option) throws IOException {
        Path inputFile = option.getInputFile();
        Path outputPath = option.getOutputFile();
        boolean redirectErrorStream = option.isRedirectErrorStream();
        Path errorPath = option.getErrFile();
        Path workDirectory = option.getWorkDirectory();
        String command = option.getCommand();

        long timeLimit = option.getTimeLimit();
        long memoryLimit = option.getMemoryLimit();
        long outputLimit = option.getOutputLimit();

        PROCESS_INFORMATION pi;

        try (Handle hIn = fileOpen(inputFile, Executor.O_RDONLY);
                Handle hOut = fileOpen(outputPath, Executor.O_WRONLY | Executor.O_CREAT | Executor.O_TRUNC);
                Handle hErr = redirectErrorStream ? hOut : fileOpen(errorPath, Executor.O_WRONLY | Executor.O_CREAT | Executor.O_TRUNC)) {
            pi = createProcess(command, hIn.getValue(), hOut.getValue(), hErr.getValue(), redirectErrorStream, workDirectory);
        }

        try (Job job = new Job();
                Handle hProcess = Handle.of(pi.getProcess());
                Handle hThread = Handle.of(pi.getThread())) {
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
                        judgeProcess.join(Constants.TERMINATE_TIMEOUT);
                        break;
                    }
                    long time = ChronoUnit.MILLIS.between(startTime, Instant.now()) - 5000; // extra 5 seconds
                    if (time > timeLimit || judgeProcess.getTime() > timeLimit) {
                        judgeProcess.terminate(Status.TIME_LIMIT_EXCEED);
                        judgeProcess.join(Constants.TERMINATE_TIMEOUT);
                        break;
                    }
                    long dwWaitTime = timeLimit - time;
                    if (dwWaitTime > Constants.UPDATE_TIME_THRESHOLD) {
                        dwWaitTime = Constants.UPDATE_TIME_THRESHOLD;
                    }
                    if (judgeProcess.join(dwWaitTime)) {
                        break;
                    }
                    if (checkOle(cOut, cErr, redirectErrorStream, outputLimit)) {
                        judgeProcess.terminate(Status.OUTPUT_LIMIT_EXCEED);
                        judgeProcess.join(Constants.TERMINATE_TIMEOUT);
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
        STARTUPINFO startupInfo = new STARTUPINFO();
        startupInfo.setCb(startupInfo.size());
        startupInfo.setDesktop(DESKTOP);
        PROCESS_INFORMATION lpProcessInformation = new PROCESS_INFORMATION();

        // without cursor feed back
        startupInfo.setFlags(STARTF_USESTDHANDLES | STARTF_FORCEOFFFEEDBACK);
        startupInfo.setStdInput(hIn);
        startupInfo.setStdOutput(hOut);
        startupInfo.setStdError(hErr);

        setInheritable(hIn);
        setInheritable(hOut);
        if (!redirectErrorStream) {
            setInheritable(hErr);
        }

        try {
            ProcessCreationHelper.execute(() -> {
                String lpApplicationName = null;
                int dwCreationFlags
                        = CREATE_SUSPENDED
                        | DETACHED_PROCESS
                        | HIGH_PRIORITY_CLASS
                        | CREATE_NEW_PROCESS_GROUP
                        | CREATE_UNICODE_ENVIRONMENT
                        | (Kernel32Util.getOSVersion() >= 0x602 ? 0 : CREATE_BREAKAWAY_FROM_JOB)
                        | CREATE_NO_WINDOW;
                Kernel32Util.assertTrue(Advapi32.INSTANCE.CreateProcessAsUserW(
                        hToken.getValue(),
                        WString.toNative(lpApplicationName), // executable name
                        WString.toNative(lpCommandLine),// command line
                        null, // process security attribute
                        null, // thread security attribute
                        true, // inherits system handles
                        dwCreationFlags, // selected based on exe type
                        EMPTY_ENV,
                        WString.toNative(Objects.toString(lpCurrentDirectory, null)),
                        startupInfo,
                        lpProcessInformation));
            });
        } catch (Win32Exception ex) {
            throw new NotExecutableException(ex);
        }
        return lpProcessInformation;
    }

    private boolean checkOle(FileChannel outputPath, FileChannel errorPath,
            boolean redirectErrorStream, long outputLimit) throws IOException {
        return outputPath.size() > outputLimit
                || !redirectErrorStream && errorPath.size() > outputLimit;
    }

    @Override
    public void close() {
        hToken.close();
    }

}
