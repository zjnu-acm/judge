package jnc.platform.win32

import jnc.foreign.LibraryLoader
import jnc.foreign.annotation.Out
import jnc.foreign.annotation.Stdcall
import jnc.foreign.typedef.int32_t
import jnc.foreign.typedef.uint32_t
import jnc.foreign.typedef.uintptr_t

/**
 * @author zhanhb
 */
@Suppress("SpellCheckingInspection", "FunctionName")
@Stdcall
interface Psapi {

    /**
     * Retrieves information about the memory usage of the specified process.
     *
     * @param hProcess [in] A handle to the process. The handle must have the
     * **PROCESS_QUERY_INFORMATION** or
     * **PROCESS_QUERY_LIMITED_INFORMATION** access right and the
     * **PROCESS_VM_READ** access right. For more information, see
     * Process Security and Access Rights.
     * @param ppsmemCounters A pointer to the
     * **PROCESS_MEMORY_COUNTERS** or
     * **PROCESS_MEMORY_COUNTERS_EX** structure that receives
     * information about the memory usage of the process.
     * @param cb The size of the *ppsmemCounters* structure, in bytes.
     * @return If the function succeeds, the return value is nonzero. If the
     * function fails, the return value is zero. To get extended error
     * information, call GetLastError.
     * @see [GetProcessMemoryInfo](http://msdn.microsoft.com/en-us/library/ms683219)
     */
    @int32_t
    fun GetProcessMemoryInfo(
            @uintptr_t hProcess: Long /*HANDLE*/,
            // this parameter is for out only, cb is not set until the method called
            @Out ppsmemCounters: PROCESS_MEMORY_COUNTERS,
            @uint32_t cb: Int): Boolean

    companion object {

        @JvmField
        val INSTANCE = LibraryLoader.create(Psapi::class.java).load("psapi")
    }

}
