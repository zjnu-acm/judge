package jnc.platform.win32;

import javax.annotation.Nonnull;
import jnc.foreign.LibraryLoader;
import jnc.foreign.annotation.Out;
import jnc.foreign.annotation.Stdcall;
import jnc.foreign.typedef.int32_t;
import jnc.foreign.typedef.uint32_t;
import jnc.foreign.typedef.uintptr_t;

@Stdcall
public interface Psapi {

    Psapi INSTANCE = LibraryLoader.create(Psapi.class).load("psapi");

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
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms683219">GetProcessMemoryInfo</a>
     */
    @int32_t
    boolean GetProcessMemoryInfo(
            @uintptr_t long hProcess,
            // this parameter is for out only, cb is not set until the method called
            @Out @Nonnull PROCESS_MEMORY_COUNTERS ppsmemCounters,
            @uint32_t int cb);

}
