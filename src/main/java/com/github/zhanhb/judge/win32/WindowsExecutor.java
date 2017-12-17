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

import com.github.zhanhb.judge.common.ExecuteResult;
import com.github.zhanhb.judge.common.Executor;
import com.github.zhanhb.judge.common.Options;
import com.github.zhanhb.judge.common.Status;
import com.github.zhanhb.judge.win32.struct.PROCESS_INFORMATION;
import com.github.zhanhb.judge.win32.struct.SECURITY_ATTRIBUTES;
import com.github.zhanhb.judge.win32.struct.SID_AND_ATTRIBUTES;
import com.github.zhanhb.judge.win32.struct.SID_IDENTIFIER_AUTHORITY;
import com.github.zhanhb.judge.win32.struct.STARTUPINFO;
import com.github.zhanhb.judge.win32.struct.TOKEN_INFORMATION_CLASS;
import com.github.zhanhb.judge.win32.struct.TOKEN_MANDATORY_LABEL;
import java.nio.file.Path;
import java.util.Objects;

import static com.github.zhanhb.judge.common.Constants.TERMINATE_TIMEOUT;
import static com.github.zhanhb.judge.common.Constants.UPDATE_TIME_THRESHOLD;
import static com.github.zhanhb.judge.common.Executor.O_CREAT;
import static com.github.zhanhb.judge.common.Executor.O_DSYNC;
import static com.github.zhanhb.judge.common.Executor.O_RDONLY;
import static com.github.zhanhb.judge.common.Executor.O_RDWR;
import static com.github.zhanhb.judge.common.Executor.O_SYNC;
import static com.github.zhanhb.judge.common.Executor.O_TEMPORARY;
import static com.github.zhanhb.judge.common.Executor.O_TRUNC;
import static com.github.zhanhb.judge.common.Executor.O_WRONLY;
import static com.github.zhanhb.judge.win32.Advapi32.DISABLE_MAX_PRIVILEGE;
import static com.github.zhanhb.judge.win32.Advapi32.SANDBOX_INERT;
import static com.github.zhanhb.judge.win32.Advapi32.SECURITY_MANDATORY_LOW_RID;
import static com.github.zhanhb.judge.win32.Advapi32.SE_GROUP_INTEGRITY;
import static com.github.zhanhb.judge.win32.Kernel32.HIGH_PRIORITY_CLASS;
import static com.github.zhanhb.judge.win32.WinBase.CREATE_BREAKAWAY_FROM_JOB;
import static com.github.zhanhb.judge.win32.WinBase.CREATE_NEW_PROCESS_GROUP;
import static com.github.zhanhb.judge.win32.WinBase.CREATE_NO_WINDOW;
import static com.github.zhanhb.judge.win32.WinBase.CREATE_SUSPENDED;
import static com.github.zhanhb.judge.win32.WinBase.CREATE_UNICODE_ENVIRONMENT;
import static com.github.zhanhb.judge.win32.WinBase.STARTF_FORCEOFFFEEDBACK;
import static com.github.zhanhb.judge.win32.WinBase.STARTF_USESTDHANDLES;
import static com.github.zhanhb.judge.win32.struct.AccessRights.TOKEN_ADJUST_DEFAULT;
import static com.github.zhanhb.judge.win32.struct.AccessRights.TOKEN_ASSIGN_PRIMARY;
import static com.github.zhanhb.judge.win32.struct.AccessRights.TOKEN_DUPLICATE;
import static com.github.zhanhb.judge.win32.struct.AccessRights.TOKEN_QUERY;

/**
 *
 * @author zhanhb
 */
public enum WindowsExecutor implements Executor {

    INSTANCE;

    private static final int GENERIC_READ = 0x80000000;
    private static final int GENERIC_WRITE = 0x40000000;
    private static final int GENERIC_EXECUTE = 0x20000000;
    private static final int GENERIC_ALL = 0x10000000;
    private static final int FILE_SHARE_READ = 0x00000001;
    private static final int FILE_SHARE_WRITE = 0x00000002;
    private static final int FILE_SHARE_DELETE = 0x00000004;

    private static final int CREATE_NEW = 1;
    private static final int CREATE_ALWAYS = 2;
    private static final int OPEN_EXISTING = 3;
    private static final int OPEN_ALWAYS = 4;
    private static final int TRUNCATE_EXISTING = 5;

    private static final int FILE_FLAG_WRITE_THROUGH = 0x80000000;
    private static final int FILE_FLAG_OVERLAPPED = 0x40000000;
    private static final int FILE_FLAG_NO_BUFFERING = 0x20000000;
    private static final int FILE_FLAG_RANDOM_ACCESS = 0x10000000;
    private static final int FILE_FLAG_SEQUENTIAL_SCAN = 0x08000000;
    private static final int FILE_FLAG_DELETE_ON_CLOSE = 0x04000000;
    private static final int FILE_FLAG_BACKUP_SEMANTICS = 0x02000000;
    private static final int FILE_FLAG_POSIX_SEMANTICS = 0x01000000;
    private static final int FILE_FLAG_OPEN_REPARSE_POINT = 0x00200000;
    private static final int FILE_FLAG_OPEN_NO_RECALL = 0x00100000;

    private static final int FILE_ATTRIBUTE_READONLY = 0x00000001;
    private static final int FILE_ATTRIBUTE_HIDDEN = 0x00000002;
    private static final int FILE_ATTRIBUTE_SYSTEM = 0x00000004;
    private static final int FILE_ATTRIBUTE_DIRECTORY = 0x00000010;
    private static final int FILE_ATTRIBUTE_ARCHIVE = 0x00000020;
    private static final int FILE_ATTRIBUTE_DEVICE = 0x00000040;
    private static final int FILE_ATTRIBUTE_NORMAL = 0x00000080;
    private static final int FILE_ATTRIBUTE_TEMPORARY = 0x00000100;
    private static final int FILE_ATTRIBUTE_SPARSE_FILE = 0x00000200;
    private static final int FILE_ATTRIBUTE_REPARSE_POINT = 0x00000400;
    private static final int FILE_ATTRIBUTE_COMPRESSED = 0x00000800;
    private static final int FILE_ATTRIBUTE_OFFLINE = 0x00001000;
    private static final int FILE_ATTRIBUTE_NOT_CONTENT_INDEXED = 0x00002000;
    private static final int FILE_ATTRIBUTE_ENCRYPTED = 0x00004000;
    private static final int FILE_ATTRIBUTE_VIRTUAL = 0x00010000;

    private SafeHandle fileOpen(Path path, int flags) {
        int access
                = (flags & O_WRONLY) != 0 ? GENERIC_WRITE
                        : (flags & O_RDWR) != 0 ? (GENERIC_READ | GENERIC_WRITE)
                                : GENERIC_READ;
        int sharing = FILE_SHARE_READ | FILE_SHARE_WRITE;
        /* Note: O_TRUNC overrides O_CREAT */
        int disposition
                = (flags & O_TRUNC) != 0 ? CREATE_ALWAYS
                        : (flags & O_CREAT) != 0 ? OPEN_ALWAYS
                                : OPEN_EXISTING;
        int maybeWriteThrough
                = (flags & (O_SYNC | O_DSYNC)) != 0
                        ? FILE_FLAG_WRITE_THROUGH
                        : FILE_ATTRIBUTE_NORMAL;
        int maybeDeleteOnClose
                = (flags & O_TEMPORARY) != 0
                        ? FILE_FLAG_DELETE_ON_CLOSE
                        : FILE_ATTRIBUTE_NORMAL;

        int flagsAndAttributes = maybeWriteThrough | maybeDeleteOnClose;
        long h = Kernel32.INSTANCE.CreateFileW(
                WString.toNative(path.toString()), /* Wide char path name */
                access, /* Read and/or write permission */
                sharing, /* File sharing flags */
                null, /* Security attributes */
                disposition, /* creation disposition */
                flagsAndAttributes, /* flags and attributes */
                0 /*NULL*/);
        return new SafeHandle(h);
    }

    @Override
    public ExecuteResult execute(Options options) {
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

        try (SafeHandle hIn = fileOpen(inputFile, O_RDONLY);
                SafeHandle hOut = fileOpen(outputPath, O_WRONLY | O_CREAT | O_TRUNC);
                SafeHandle hErr = redirectErrorStream ? hOut : fileOpen(errorPath, O_WRONLY | O_CREAT | O_TRUNC)) {
            pi = createProcess(command, hIn.getValue(), hOut.getValue(), hErr.getValue(), redirectErrorStream, workDirectory);
        }

        try (Job job = new Job();
                SafeHandle hProcess = new SafeHandle(pi.getProcess());
                SafeHandle hThread = new SafeHandle(pi.getThread())) {
            JudgeProcess judgeProcess = new JudgeProcess(hProcess.getValue());
            try {
                job.init();
                job.assignProcess(hProcess.getValue());

                int dwCount = Kernel32.INSTANCE.ResumeThread(hThread.getValue());
                Kernel32Util.assertTrue(dwCount != -1);
                hThread.close();

                while (true) {
                    long memory = judgeProcess.getPeakMemory();
                    if (memory > memoryLimit) {
                        judgeProcess.terminate(Status.MEMORY_LIMIT_EXCEED);
                        break;
                    }
                    long time = judgeProcess.getActiveTime() - 2000; // extra 2000 millis
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
                    // TODO maybe we should not check the output limit for current process may wait for the file to be finish
                    if (checkOle(outputPath, errorPath, redirectErrorStream, outputLimit)) {
                        judgeProcess.terminate(Status.OUTPUT_LIMIT_EXCEED);
                        break;
                    }
                }
                if (checkOle(outputPath, errorPath, redirectErrorStream, outputLimit)) {
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

    private PROCESS_INFORMATION createProcess(String lpCommandLine, long /*HANDLE*/ hIn, long /*HANDLE*/ hOut, long /*HANDLE*/ hErr,
            boolean redirectErrorStream, Path lpCurrentDirectory) {
        SECURITY_ATTRIBUTES sa = new SECURITY_ATTRIBUTES();
        sa.setInheritHandle(true);

        String lpApplicationName = null;
        SECURITY_ATTRIBUTES lpProcessAttributes = new SECURITY_ATTRIBUTES();
        SECURITY_ATTRIBUTES lpThreadAttributes = new SECURITY_ATTRIBUTES();
        int dwCreationFlags
                = CREATE_SUSPENDED
                | HIGH_PRIORITY_CLASS
                | CREATE_NEW_PROCESS_GROUP
                | CREATE_UNICODE_ENVIRONMENT
                | CREATE_BREAKAWAY_FROM_JOB
                | CREATE_NO_WINDOW;
        STARTUPINFO lpStartupInfo = new STARTUPINFO();
        PROCESS_INFORMATION lpProcessInformation = new PROCESS_INFORMATION();

        // without cursor feed back
        lpStartupInfo.setFlags(STARTF_USESTDHANDLES | STARTF_FORCEOFFFEEDBACK);
        lpStartupInfo.setStandardInput(hIn);
        lpStartupInfo.setStandardOutput(hOut);
        lpStartupInfo.setStandardError(hErr);

        Kernel32Util.setInheritable(hIn);
        Kernel32Util.setInheritable(hOut);
        if (!redirectErrorStream) {
            Kernel32Util.setInheritable(hErr);
        }

        try (SafeHandle hToken = new SafeHandle(createRestrictedToken())) {
            SID_IDENTIFIER_AUTHORITY pIdentifierAuthority = new SID_IDENTIFIER_AUTHORITY(0, 0, 0, 0, 0, 16);

            long pSid = Advapi32Util.newPSID(pIdentifierAuthority, SECURITY_MANDATORY_LOW_RID);

            try {
                TOKEN_MANDATORY_LABEL tokenInformation = new TOKEN_MANDATORY_LABEL();

                SID_AND_ATTRIBUTES sidAndAttributes = tokenInformation.getLabel();
                sidAndAttributes.setAttributes(SE_GROUP_INTEGRITY);
                sidAndAttributes.setSid(pSid);

                Kernel32Util.assertTrue(Advapi32.INSTANCE.SetTokenInformation(
                        hToken.getValue(),
                        TOKEN_INFORMATION_CLASS.TokenIntegrityLevel.value(),
                        tokenInformation,
                        tokenInformation.size() + Advapi32.INSTANCE.GetLengthSid(pSid)));

                ProcessCreationHelper.execute(()
                        -> Kernel32Util.assertTrue(Kernel32.INSTANCE.CreateProcessAsUserW(
                                hToken.getValue(),
                                WString.toNative(lpApplicationName), // executable name
                                WString.toNative(lpCommandLine),// command line
                                lpProcessAttributes, // process security attribute
                                lpThreadAttributes, // thread security attribute
                                true, // inherits system handles
                                dwCreationFlags, // selected based on exe type
                                null,
                                WString.toNative(Objects.toString(lpCurrentDirectory, null)),
                                lpStartupInfo,
                                lpProcessInformation)));
            } finally {
                Kernel32Util.assertTrue(Advapi32.INSTANCE.FreeSid(pSid) == 0);
            }
        }
        return lpProcessInformation;
    }

    private long /*HANDLE*/ createRestrictedToken() {
        try (SafeHandle token = new SafeHandle(
                Advapi32Util.openProcessToken(Kernel32.INSTANCE.GetCurrentProcess(),
                        TOKEN_DUPLICATE | TOKEN_ASSIGN_PRIMARY | TOKEN_QUERY | TOKEN_ADJUST_DEFAULT))) {
            return Advapi32Util.createRestrictedToken(
                    token.getValue(), // ExistingTokenHandle
                    DISABLE_MAX_PRIVILEGE | SANDBOX_INERT, // Flags
                    null, // SidsToDisable
                    null, // PrivilegesToDelete
                    null // SidsToRestrict
            );
        }
    }

    private boolean checkOle(Path outputPath, Path errorPath, boolean redirectErrorStream, long outputLimit) {
        return outputPath.toFile().length() > outputLimit
                || !redirectErrorStream && errorPath.toFile().length() > outputLimit;
    }

}
