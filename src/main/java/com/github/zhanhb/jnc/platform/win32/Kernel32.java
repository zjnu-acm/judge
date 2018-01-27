package com.github.zhanhb.jnc.platform.win32;

import jnc.foreign.ForeignProviders;
import jnc.foreign.LibraryLoader;
import jnc.foreign.Pointer;
import jnc.foreign.abi.Stdcall;
import jnc.foreign.annotation.In;
import jnc.foreign.annotation.Out;
import jnc.foreign.byref.AddressByReference;
import jnc.foreign.byref.IntByReference;
import jnc.foreign.byref.PointerByReference;
import jnc.foreign.typedef.int32_t;
import jnc.foreign.typedef.uint32_t;
import jnc.foreign.typedef.uintptr_t;

@Stdcall
public interface Kernel32 {

    Kernel32 INSTANCE = LibraryLoader.create(Kernel32.class).load("kernel32");

    @int32_t
    boolean AssignProcessToJobObject(@uintptr_t long /*HANDLE*/ hJob, @uintptr_t long /*HANDLE*/ hProcess);

    /**
     *
     * @param hThread A handle to the thread to be restarted.
     * @return If the function succeeds, the return value is the thread's
     * previous suspend count. If the function fails, the return value is
     * (DWORD) -1. To get extended error information, call GetLastError.
     */
    @uint32_t
    int ResumeThread(@uintptr_t long /*HANDLE*/ hThread);

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
    boolean GetProcessTimes(
            @uintptr_t long /*HANDLE*/ hProcess,
            @Out FILETIME lpCreationTime,
            @Out FILETIME lpExitTime,
            @Out FILETIME lpKernelTime,
            @Out FILETIME lpUserTime);

    @uintptr_t
    long /*HANDLE*/ CreateJobObjectW(@In SECURITY_ATTRIBUTES lpJobAttributes, Pointer lpName);

    @uint32_t
    int SetErrorMode(@uint32_t int uMode);

    @int32_t
    boolean GetExitCodeProcess(@uintptr_t long /*HANDLE*/ hProcess, IntByReference/*LPDWORD*/ dwExitCode);

    @int32_t
    boolean SetHandleInformation(
            @uintptr_t long /*HANDLE*/ handle,
            @uint32_t int /*DWORD*/ dwMask,
            @uint32_t int /*DWORD*/ dwFlags);

    @int32_t
    boolean CloseHandle(@uintptr_t long /*HANDLE*/ handle);

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
    boolean SetInformationJobObject(
            @uintptr_t long /*HANDLE*/ hJob,
            @uint32_t int JobObjectInfoClass,
            @In JOBOBJECT_INFORMATION lpJobObjectInfo,
            @uint32_t int cbJobObjectInfoLength);

    @uintptr_t
    long CreateFileW(
            Pointer lpFileName,
            @uint32_t int dwDesiredAccess,
            @uint32_t int dwShareMode,
            @In SECURITY_ATTRIBUTES lpSecurityAttributes,
            @uint32_t int dwCreationDisposition,
            @uint32_t int dwFlagsAndAttributes,
            @uintptr_t long /*HANDLE*/ hTemplateFile);

    @uintptr_t
    long /*HANDLE*/ GetCurrentProcess();

    @uint32_t
    int WaitForSingleObject(
            @uintptr_t long hHandle,
            @uint32_t int /*DWORD*/ millis);

    @int32_t
    boolean TerminateProcess(@uintptr_t long hProcess, @uint32_t int uExitCode);

    @uintptr_t
    long LocalFree(@uintptr_t long hMem);

    @uint32_t
    int FormatMessageW(
            @uint32_t int /*DWORD*/ dwFlags,
            @uintptr_t long lpSource,
            @uint32_t int /*DWORD*/ dwMessageId,
            @uint32_t int /*DWORD*/ dwLanguageId,
            PointerByReference lpBuffer,
            @uint32_t int /*DWORD*/ nSize,
            @uintptr_t long /* va_list* */ Arguments);

    default int GetLastError() {
        return ForeignProviders.getDefault().getLastError();
    }

    @int32_t
    boolean DuplicateHandle(
            @uintptr_t long /*HANDLE*/ hSourceProcessHandle,
            @uintptr_t long /*HANDLE*/ hSourceHandle,
            @uintptr_t long /*HANDLE*/ hTargetProcessHandle,
            AddressByReference lpTargetHandle,
            @uint32_t int /*DWORD*/ dwDesiredAccess,
            @int32_t boolean bInheritHandle,
            @uint32_t int /*DWORD*/ dwOptions
    );

}
