package com.github.zhanhb.judge.jna.win32;

import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.BaseTSD;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;
import java.util.Arrays;
import java.util.List;

public interface Psapi extends StdCallLibrary {

    Psapi INSTANCE = Native.loadLibrary("psapi", Psapi.class, W32APIOptions.DEFAULT_OPTIONS);

    /**
     * Memory statistics for a process.
     *
     * @see
     * <a href="http://msdn.microsoft.com/en-us/library/ms684877(VS.85).aspx">PROCESS_MEMORY_COUNTERS</a>
     */
    @SuppressWarnings({"PublicField", "PublicInnerClass"})
    public static class PROCESS_MEMORY_COUNTERS extends Structure {

        /**
         * The size of the structure, in bytes.
         */
        public int cb = size();
        /**
         * The number of page faults.
         */
        public int PageFaultCount;
        /**
         * The peak working set size, in bytes.
         */
        public BaseTSD.SIZE_T PeakWorkingSetSize;
        /**
         * The current working set size, in bytes.
         */
        public BaseTSD.SIZE_T WorkingSetSize;
        /**
         * The peak paged pool usage, in bytes.
         */
        public BaseTSD.SIZE_T QuotaPeakPagedPoolUsage;
        /**
         * The current paged pool usage, in bytes.
         */
        public BaseTSD.SIZE_T QuotaPagedPoolUsage;
        /**
         * The peak nonpaged pool usage, in bytes.
         */
        public BaseTSD.SIZE_T QuotaPeakNonPagedPoolUsage;
        /**
         * The current nonpaged pool usage, in bytes.
         */
        public BaseTSD.SIZE_T QuotaNonPagedPoolUsage;
        /**
         * The Commit Charge value in bytes for this process. Commit Charge is
         * the total amount of memory that the memory manager has committed for
         * a running process.
         */
        public BaseTSD.SIZE_T PagefileUsage;
        /**
         * The peak value in bytes of the Commit Charge during the lifetime of
         * this process.
         */
        public BaseTSD.SIZE_T PeakPagefileUsage;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList(
                    "cb",
                    "PageFaultCount",
                    "PeakWorkingSetSize",
                    "WorkingSetSize",
                    "QuotaPeakPagedPoolUsage",
                    "QuotaPagedPoolUsage",
                    "QuotaPeakNonPagedPoolUsage",
                    "QuotaNonPagedPoolUsage",
                    "PagefileUsage",
                    "PeakPagefileUsage"
            );
        }
    }

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
    boolean GetProcessMemoryInfo(HANDLE hProcess, PROCESS_MEMORY_COUNTERS ppsmemCounters, int cb);

}
