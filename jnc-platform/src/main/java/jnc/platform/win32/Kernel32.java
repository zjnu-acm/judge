package jnc.platform.win32;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import jnc.foreign.LibraryLoader;
import jnc.foreign.Pointer;
import jnc.foreign.annotation.In;
import jnc.foreign.annotation.Out;
import jnc.foreign.annotation.Stdcall;
import jnc.foreign.byref.AddressByReference;
import jnc.foreign.byref.IntByReference;
import jnc.foreign.byref.PointerByReference;
import jnc.foreign.typedef.int32_t;
import jnc.foreign.typedef.uint32_t;
import jnc.foreign.typedef.uintptr_t;

@Stdcall
public interface Kernel32 {

    @Nonnull
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
    /*Kernel32.dll; KernelBase.dll on Windows Phone 8.1*/
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
     * <a href="https://msdn.microsoft.com/en-us/library/windows/desktop/ms683223">GetProcessTimes</a>
     */
    @int32_t
    boolean GetProcessTimes(
            @uintptr_t long hProcess,
            @Out @Nonnull FILETIME lpCreationTime,
            @Out @Nonnull FILETIME lpExitTime,
            @Out @Nonnull FILETIME lpKernelTime,
            @Out @Nonnull FILETIME lpUserTime);

    @uintptr_t
    long /*HANDLE*/ CreateJobObjectW(@In @Nullable SECURITY_ATTRIBUTES lpJobAttributes, @Nullable Pointer lpName);

    @uint32_t
    int SetErrorMode(@uint32_t int uMode);

    @int32_t
    boolean GetExitCodeProcess(@uintptr_t long /*HANDLE*/ hProcess, @Nonnull IntByReference /*LPDWORD*/ dwExitCode);

    @int32_t
    boolean SetHandleInformation(
            @uintptr_t long /*HANDLE*/ handle,
            @uint32_t int /*DWORD*/ dwMask,
            @uint32_t int /*DWORD*/ dwFlags);

    @int32_t
    boolean CloseHandle(@uintptr_t long /*HANDLE*/ handle);

    /**
     * @return If the function succeeds, the return value is nonzero. If the
     * function fails, the return value is zero. To get extended error
     * information, call GetLastError.
     * @see
     * <a href="https://msdn.microsoft.com/en-us/library/windows/desktop/ms686216">SetInformationJobObject</a>
     */
    @int32_t
    boolean SetInformationJobObject(
            @uintptr_t long /*HANDLE*/ hJob,
            @Nonnull JOBOBJECTINFOCLASS JobObjectInfoClass,
            @In @Nonnull JobObjectInformation lpJobObjectInfo,
            @uint32_t int cbJobObjectInfoLength);

    @uintptr_t
    long /*HANDLE*/ CreateFileW(
                    @Nonnull Pointer lpFileName,
                    @uint32_t int dwDesiredAccess,
                    @uint32_t int dwShareMode,
                    @In @Nullable SECURITY_ATTRIBUTES lpSecurityAttributes,
                    @uint32_t int dwCreationDisposition,
                    @uint32_t int dwFlagsAndAttributes,
                    @uintptr_t long /*HANDLE*/ hTemplateFile);

    @uintptr_t
    long /*HANDLE*/ GetCurrentProcess();

    @uint32_t
    int WaitForSingleObject(@uintptr_t long /*HANDLE*/ hHandle, @uint32_t int /*DWORD*/ millis);

    @int32_t
    boolean TerminateProcess(@uintptr_t long /*HANDLE*/ hProcess, @uint32_t int /*UINT*/ uExitCode);

    @uintptr_t
    long LocalFree(@uintptr_t long hMem);

    @uint32_t
    int FormatMessageW(
            @uint32_t int /*DWORD*/ dwFlags,
            @uintptr_t long lpSource,
            @uint32_t int /*DWORD*/ dwMessageId,
            @uint32_t int /*DWORD*/ dwLanguageId,
            @Nonnull PointerByReference lpBuffer,
            @uint32_t int /*DWORD*/ nSize,
            @uintptr_t long /* va_list* */ Arguments);

    @int32_t
    boolean DuplicateHandle(
            @uintptr_t long /*HANDLE*/ hSourceProcessHandle,
            @uintptr_t long /*HANDLE*/ hSourceHandle,
            @uintptr_t long /*HANDLE*/ hTargetProcessHandle,
            @Nonnull AddressByReference lpTargetHandle,
            @uint32_t int /*DWORD*/ dwDesiredAccess,
            @int32_t boolean bInheritHandle,
            @uint32_t int /*DWORD*/ dwOptions);
}
