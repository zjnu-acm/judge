package jnc.platform.win32

import jnc.foreign.LibraryLoader
import jnc.foreign.Pointer
import jnc.foreign.annotation.In
import jnc.foreign.annotation.Out
import jnc.foreign.annotation.Stdcall
import jnc.foreign.byref.AddressByReference
import jnc.foreign.byref.IntByReference
import jnc.foreign.byref.PointerByReference
import jnc.foreign.typedef.int32_t
import jnc.foreign.typedef.uint32_t
import jnc.foreign.typedef.uintptr_t

/**
 * @author zhanhb
 */
@Suppress("FunctionName")
@Stdcall
interface Kernel32 {

    @int32_t
    fun AssignProcessToJobObject(@uintptr_t hJob: Long /*HANDLE*/, @uintptr_t hProcess: Long /*HANDLE*/): Boolean

    /**
     *
     * @param hThread A handle to the thread to be restarted.
     * @return If the function succeeds, the return value is the thread's
     * previous suspend count. If the function fails, the return value is
     * (DWORD) -1. To get extended error information, call GetLastError.
     */
    /*Kernel32.dll; KernelBase.dll on Windows Phone 8.1*/
    @uint32_t
    fun ResumeThread(@uintptr_t hThread: Long /*HANDLE*/): Int

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
     * @see [GetProcessTimes](https://msdn.microsoft.com/en-us/library/windows/desktop/ms683223)
     */
    @int32_t
    fun GetProcessTimes(
            @uintptr_t hProcess: Long /*HANDLE*/,
            @Out lpCreationTime: FILETIME,
            @Out lpExitTime: FILETIME,
            @Out lpKernelTime: FILETIME,
            @Out lpUserTime: FILETIME): Boolean

    @uintptr_t
    fun CreateJobObjectW(@In lpJobAttributes: SECURITY_ATTRIBUTES?, lpName: Pointer?): Long // HANDLE

    @uint32_t
    fun SetErrorMode(@uint32_t uMode: Int): Int

    @int32_t
    fun GetExitCodeProcess(@uintptr_t hProcess: Long /*HANDLE*/, dwExitCode: IntByReference /*LPDWORD*/): Boolean

    @int32_t
    fun SetHandleInformation(
            @uintptr_t handle: Long /*HANDLE*/,
            @uint32_t dwMask: Int /*DWORD*/,
            @uint32_t dwFlags: Int /*DWORD*/): Boolean

    @int32_t
    fun CloseHandle(@uintptr_t handle: Long /*HANDLE*/): Boolean

    /**
     *
     * @param hJob
     * @param JobObjectInfoClass
     * @param lpJobObjectInfo
     * @param cbJobObjectInfoLength
     * @return If the function succeeds, the return value is nonzero. If the
     * function fails, the return value is zero. To get extended error
     * information, call GetLastError.
     * @see [SetInformationJobObject](https://msdn.microsoft.com/en-us/library/windows/desktop/ms686216)
     */
    @int32_t
    fun SetInformationJobObject(
            @uintptr_t hJob: Long /*HANDLE*/,
            JobObjectInfoClass: JOBOBJECTINFOCLASS,
            @In lpJobObjectInfo: JobObjectInformation,
            @uint32_t cbJobObjectInfoLength: Int): Boolean

    @uintptr_t
    fun CreateFileW(
            lpFileName: Pointer,
            @uint32_t dwDesiredAccess: Int,
            @uint32_t dwShareMode: Int,
            @In lpSecurityAttributes: SECURITY_ATTRIBUTES?,
            @uint32_t dwCreationDisposition: Int,
            @uint32_t dwFlagsAndAttributes: Int,
            @uintptr_t hTemplateFile: Long /*HANDLE*/): Long

    @uintptr_t
    fun GetCurrentProcess(): Long //HANDLE

    @uint32_t
    fun WaitForSingleObject(
            @uintptr_t hHandle: Long,
            @uint32_t millis: Int /*DWORD*/): Int

    @int32_t
    fun TerminateProcess(@uintptr_t hProcess: Long, @uint32_t uExitCode: Int): Boolean

    @uintptr_t
    fun LocalFree(@uintptr_t hMem: Long): Long

    @uint32_t
    fun FormatMessageW(
            @uint32_t dwFlags: Int /*DWORD*/,
            @uintptr_t lpSource: Long,
            @uint32_t dwMessageId: Int /*DWORD*/,
            @uint32_t dwLanguageId: Int /*DWORD*/,
            lpBuffer: PointerByReference,
            @uint32_t nSize: Int /*DWORD*/,
            @uintptr_t Arguments: Long /* va_list* */): Int

    @int32_t
    fun DuplicateHandle(
            @uintptr_t hSourceProcessHandle: Long /*HANDLE*/,
            @uintptr_t hSourceHandle: Long /*HANDLE*/,
            @uintptr_t hTargetProcessHandle: Long /*HANDLE*/,
            lpTargetHandle: AddressByReference,
            @uint32_t dwDesiredAccess: Int /*DWORD*/,
            @int32_t bInheritHandle: Boolean,
            @uint32_t dwOptions: Int /*DWORD*/
    ): Boolean

    companion object {
        @JvmField
        val INSTANCE = LibraryLoader.create(Kernel32::class.java).load("kernel32")
    }

}
