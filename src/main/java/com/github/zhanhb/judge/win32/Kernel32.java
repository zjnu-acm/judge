package com.github.zhanhb.judge.win32;

import com.github.zhanhb.judge.win32.struct.FILETIME;
import com.github.zhanhb.judge.win32.struct.JOBOBJECT_INFORMATION;
import com.github.zhanhb.judge.win32.struct.PROCESS_INFORMATION;
import com.github.zhanhb.judge.win32.struct.SECURITY_ATTRIBUTES;
import com.github.zhanhb.judge.win32.struct.STARTUPINFO;
import javax.annotation.Nullable;
import jnr.ffi.annotations.In;
import jnr.ffi.annotations.Out;
import jnr.ffi.byref.IntByReference;
import jnr.ffi.byref.PointerByReference;
import jnr.ffi.types.int32_t;
import jnr.ffi.types.u_int32_t;
import jnr.ffi.types.uintptr_t;

public interface Kernel32 {

    Kernel32 INSTANCE = Native.loadLibrary("kernel32", Kernel32.class);

    @int32_t
    /* BOOL */ int AssignProcessToJobObject(@In @uintptr_t long /*HANDLE*/ hJob, @In @uintptr_t long /*HANDLE*/ hProcess);

    /**
     * The system does not display the critical-error-handler message box.
     * Instead, the system sends the error to the calling process.
     */
    int SEM_FAILCRITICALERRORS = 0x0001;
    /**
     * The system automatically fixes memory alignment faults and makes them
     * invisible to the application. It does this for the calling process and
     * any descendant processes. This feature is only supported by certain
     * processor architectures. For more information, see the Remarks section.
     */
    int SEM_NOALIGNMENTFAULTEXCEPT = 0x0004;
    /**
     * The system does not display the Windows Error Reporting dialog.
     */
    int SEM_NOGPFAULTERRORBOX = 0x0002;
    /**
     * The system does not display a message box when it fails to find a file.
     * Instead, the error is returned to the calling process.
     */
    int SEM_NOOPENFILEERRORBOX = 0x8000;

    /**
     *
     * @param hThread A handle to the thread to be restarted.
     * @return If the function succeeds, the return value is the thread's
     * previous suspend count. If the function fails, the return value is
     * (DWORD) -1. To get extended error information, call GetLastError.
     */
    @u_int32_t
    int ResumeThread(@In @uintptr_t long /*HANDLE*/ hThread);

    /**
     *
     * @param hProcess A handle to the process whose timing information is
     * sought.
     * @param lpCreationTime A pointer to a FILETIME structure that receives the
     * creation time of the process.
     * @param lpExitTime A pointer to a FILETIME structure that receives the
     * exit time of the process. If the process has not exited, the content of
     * this structure is undefined.
     * @param lpKernelTime A pointer to a FILETIME structure that receives the
     * amount of time that the process has executed in kernel mode. The time
     * that each of the threads of the process has executed in kernel mode is
     * determined, and then all of those times are summed together to obtain
     * this value.
     * @param lpUserTime A pointer to a FILETIME structure that receives the
     * amount of time that the process has executed in user mode. The time that
     * each of the threads of the process has executed in user mode is
     * determined, and then all of those times are summed together to obtain
     * this value. Note that this value can exceed the amount of real time
     * elapsed (between lpCreationTime and lpExitTime) if the process executes
     * across multiple CPU cores.
     * @return If the function succeeds, the return value is nonzero. If the
     * function fails, the return value is zero. To get extended error
     * information, call GetLastError.
     * @see
     * <a href="https://msdn.microsoft.com/en-us/library/windows/desktop/ms683223(v=vs.85).aspx">GetProcessTimes</a>
     */
    @int32_t
    /* BOOL */ int GetProcessTimes(
            @In @uintptr_t long /*HANDLE*/ hProcess,
            @Out FILETIME lpCreationTime,
            @Out FILETIME lpExitTime,
            @Out FILETIME lpKernelTime,
            @Out FILETIME lpUserTime);

    @Nullable
    @uintptr_t long /*HANDLE*/ CreateJobObjectW(@In SECURITY_ATTRIBUTES lpJobAttributes, @In byte[] lpName);

    /**
     * @param uMode
     * @param lpOldMode
     * @return
     * @see
     * https://msdn.microsoft.com/en-us/library/windows/desktop/dd553630(v=vs.85).aspx
     */
    @int32_t
    /* BOOL */ int SetThreadErrorMode(@In @u_int32_t int uMode, IntByReference lpOldMode);

    @int32_t
    /* BOOL */ int GetExitCodeProcess(@In @uintptr_t long /*HANDLE*/ hProcess, @Out IntByReference dwExitCode);

    int JOB_OBJECT_LIMIT_WORKINGSET = 0x0001;
    int JOB_OBJECT_LIMIT_PROCESS_TIME = 0x0002;
    int JOB_OBJECT_LIMIT_JOB_TIME = 0x0004;
    int JOB_OBJECT_LIMIT_ACTIVE_PROCESS = 0x0008;
    int JOB_OBJECT_LIMIT_AFFINITY = 0x0010;
    int JOB_OBJECT_LIMIT_PRIORITY_CLASS = 0x0020;
    int JOB_OBJECT_LIMIT_PRESERVE_JOB_TIME = 0x0040;
    int JOB_OBJECT_LIMIT_SCHEDULING_CLASS = 0x0080;
    int JOB_OBJECT_LIMIT_PROCESS_MEMORY = 0x0100;
    int JOB_OBJECT_LIMIT_JOB_MEMORY = 0x0200;
    int JOB_OBJECT_LIMIT_DIE_ON_UNHANDLED_EXCEPTION = 0x0400;
    int JOB_OBJECT_BREAKAWAY_OK = 0x0800;
    int JOB_OBJECT_SILENT_BREAKAWAY = 0x1000;

    int JOB_OBJECT_LIMIT_BREAKAWAY_OK = 0x00000800;
    int JOB_OBJECT_LIMIT_SILENT_BREAKAWAY_OK = 0x00001000;
    int JOB_OBJECT_LIMIT_KILL_ON_JOB_CLOSE = 0x00002000;
    int JOB_OBJECT_LIMIT_SUBSET_AFFINITY = 0x00004000;

    /* JOBOBJECT_BASIC_UI_RESTRICTIONS.UIRestrictionsClass constants */
    int JOB_OBJECT_UILIMIT_HANDLES = 0x0001;
    int JOB_OBJECT_UILIMIT_READCLIPBOARD = 0x0002;
    int JOB_OBJECT_UILIMIT_WRITECLIPBOARD = 0x0004;
    int JOB_OBJECT_UILIMIT_SYSTEMPARAMETERS = 0x0008;
    int JOB_OBJECT_UILIMIT_DISPLAYSETTINGS = 0x0010;
    int JOB_OBJECT_UILIMIT_GLOBALATOMS = 0x0020;
    int JOB_OBJECT_UILIMIT_DESKTOP = 0x0040;
    int JOB_OBJECT_UILIMIT_EXITWINDOWS = 0x0080;

    /* JOBOBJECT_SECURITY_LIMIT_INFORMATION.SecurityLimitFlags constants */
    int JOB_OBJECT_SECURITY_NO_ADMIN = 0x0001;
    int JOB_OBJECT_SECURITY_RESTRICTED_TOKEN = 0x0002;
    int JOB_OBJECT_SECURITY_ONLY_TOKEN = 0x0004;
    int JOB_OBJECT_SECURITY_FILTER_TOKENS = 0x0008;

    /* JOBOBJECT_END_OF_JOB_TIME_INFORMATION.EndOfJobTimeAction constants */
    int JOB_OBJECT_TERMINATE_AT_END_OF_JOB = 0;
    int JOB_OBJECT_POST_AT_END_OF_JOB = 1;

    int JOB_OBJECT_MSG_END_OF_JOB_TIME = 1;
    int JOB_OBJECT_MSG_END_OF_PROCESS_TIME = 2;
    int JOB_OBJECT_MSG_ACTIVE_PROCESS_LIMIT = 3;
    int JOB_OBJECT_MSG_ACTIVE_PROCESS_ZERO = 4;
    int JOB_OBJECT_MSG_NEW_PROCESS = 6;
    int JOB_OBJECT_MSG_EXIT_PROCESS = 7;
    int JOB_OBJECT_MSG_ABNORMAL_EXIT_PROCESS = 8;
    int JOB_OBJECT_MSG_PROCESS_MEMORY_LIMIT = 9;
    int JOB_OBJECT_MSG_JOB_MEMORY_LIMIT = 10;

    @int32_t
    /* BOOL */ int SetHandleInformation(
            @In @uintptr_t long /*HANDLE*/ handle,
            @In @u_int32_t int /*DWORD*/ dwMask,
            @In @u_int32_t int /*DWORD*/ dwFlags);

    @int32_t
    /* BOOL */ int CloseHandle(@In @uintptr_t long /*HANDLE*/ handle);

    /**
     *
     * @param hJob
     * @param JobObjectInfoClass
     * @param lpJobObjectInfo
     * @param cbJobObjectInfoLength
     * @return If the function succeeds, the return value is nonzero. If the
     * function fails, the return value is zero. To get extended error
     * information, call GetLastError.
     * @see
     * <a href="https://msdn.microsoft.com/en-us/library/windows/desktop/ms686216(v=vs.85).aspx">SetInformationJobObject</a>
     */
    @int32_t
    /* BOOL */ int SetInformationJobObject(
            @In @uintptr_t long /*HANDLE*/ hJob,
            @In @u_int32_t int JobObjectInfoClass,
            @In JOBOBJECT_INFORMATION lpJobObjectInfo,
            @In @u_int32_t int cbJobObjectInfoLength);

    int NORMAL_PRIORITY_CLASS = 0x00000020;
    int IDLE_PRIORITY_CLASS = 0x00000040;
    int HIGH_PRIORITY_CLASS = 0x00000080;
    int HANDLE_FLAG_INHERIT = 0x00000001;

    @int32_t
    /* BOOL */ int CreateProcessAsUserW(
            @In @uintptr_t long /*HANDLE*/ hToken,
            @In byte[] lpApplicationName,
            @In /*@Out*/ byte[] lpCommandLine,
            @In SECURITY_ATTRIBUTES lpProcessAttributes,
            @In SECURITY_ATTRIBUTES lpThreadAttributes,
            @In @u_int32_t boolean bInheritHandles,
            @In @u_int32_t int /*DWORD*/ dwCreationFlags,
            @In byte[] lpEnvironment,
            @In byte[] lpCurrentDirectory,
            @In STARTUPINFO lpStartupInfo,
            @Out PROCESS_INFORMATION lpProcessInformation);

    @uintptr_t long CreateFileW(
            @In byte[] lpFileName,
            @In @u_int32_t int dwDesiredAccess,
            @In @u_int32_t int dwShareMode,
            @In SECURITY_ATTRIBUTES lpSecurityAttributes,
            @In @u_int32_t int dwCreationDisposition,
            @In @u_int32_t int dwFlagsAndAttributes,
            @In @uintptr_t long /*HANDLE*/ hTemplateFile);

    @uintptr_t long /*HANDLE*/ GetCurrentProcess();

    @u_int32_t
    int WaitForSingleObject(
            @In @uintptr_t long hHandle,
            @In @u_int32_t int /*DWORD*/ millis);

    @int32_t
    /* BOOL */ int TerminateProcess(@In @uintptr_t long hProcess, @In @u_int32_t int uExitCode);

    @uintptr_t long LocalFree(@In @uintptr_t long hMem);

    @u_int32_t
    int FormatMessageW(
            @In @u_int32_t int /*DWORD*/ dwFlags,
            @In @uintptr_t long lpSource,
            @In @u_int32_t int /*DWORD*/ dwMessageId,
            @In @u_int32_t int /*DWORD*/ dwLanguageId,
            @Out PointerByReference lpBuffer,
            @In @u_int32_t int /*DWORD*/ nSize,
            @In Object... /**/ arguments);

}
