package com.github.zhanhb.jnc.platform.win32;

import jnc.foreign.LibraryLoader;
import jnc.foreign.abi.Stdcall;
import jnc.foreign.annotation.Out;
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
     * <strong>PROCESS_QUERY_INFORMATION</strong> or
     * <strong>PROCESS_QUERY_LIMITED_INFORMATION</strong> access right and the
     * <strong>PROCESS_VM_READ</strong> access right. For more information, see
     * Process Security and Access Rights.
     * @param ppsmemCounters A pointer to the
     * <strong>PROCESS_MEMORY_COUNTERS</strong> or
     * <strong>PROCESS_MEMORY_COUNTERS_EX</strong> structure that receives
     * information about the memory usage of the process.
     * @param cb The size of the <em>ppsmemCounters</em> structure, in bytes.
     * @return If the function succeeds, the return value is nonzero. If the
     * function fails, the return value is zero. To get extended error
     * information, call GetLastError.
     * @see
     * <a href="http://msdn.microsoft.com/en-us/library/ms683219(VS.85).aspx">GetProcessMemoryInfo</a>
     */
    @int32_t
    boolean GetProcessMemoryInfo(
            @uintptr_t long /*HANDLE*/ hProcess,
            // this parameter is for out only, cb is not set until the method called
            @Out PROCESS_MEMORY_COUNTERS ppsmemCounters,
            @uint32_t int cb);

}
