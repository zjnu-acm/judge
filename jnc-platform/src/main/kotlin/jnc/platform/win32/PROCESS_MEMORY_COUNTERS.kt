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
package jnc.platform.win32

/**
 * Memory statistics for a process.
 *
 * @see [PROCESS_MEMORY_COUNTERS](http://msdn.microsoft.com/en-us/library/ms684877)
 * @author zhanhb
 */
class PROCESS_MEMORY_COUNTERS : jnc.foreign.Struct() {

    /**
     * The size of the structure, in bytes.
     */
    private val cb = DWORD()
    /**
     * The number of page faults.
     */
    private val PageFaultCount = DWORD()
    /**
     * The peak working set size, in bytes.
     */
    private val PeakWorkingSetSize = size_t()
    /**
     * The current working set size, in bytes.
     */
    private val WorkingSetSize = size_t()
    /**
     * The peak paged pool usage, in bytes.
     */
    private val QuotaPeakPagedPoolUsage = size_t()
    /**
     * The current paged pool usage, in bytes.
     */
    private val QuotaPagedPoolUsage = size_t()
    /**
     * The peak nonpaged pool usage, in bytes.
     */
    private val QuotaPeakNonPagedPoolUsage = size_t()
    /**
     * The current nonpaged pool usage, in bytes.
     */
    private val QuotaNonPagedPoolUsage = size_t()
    /**
     * The Commit Charge value in bytes for this process. Commit Charge is the
     * total amount of memory that the memory manager has committed for a
     * running process.
     */
    private val PagefileUsage = size_t()
    /**
     * The peak value in bytes of the Commit Charge during the lifetime of this
     * process.
     */
    private val PeakPagefileUsage = size_t()

    fun getCb(): Int = cb.get().toInt()
    fun setCb(value: Int) = cb.set(value.toLong())

    var pageFaultCount: Long
        get() = PageFaultCount.get()
        set(value) = PageFaultCount.set(value)

    var peakWorkingSetSize: Int
        get() = PeakWorkingSetSize.get().toInt()
        set(value) = PeakWorkingSetSize.set(value.toLong())

    var workingSetSize: Long
        get() = WorkingSetSize.get()
        set(value) = WorkingSetSize.set(value)

    var quotaPeakPagedPoolUsage: Long
        get() = QuotaPeakPagedPoolUsage.get()
        set(value) = QuotaPeakPagedPoolUsage.set(value)

    var quotaPagedPoolUsage: Long
        get() = QuotaPagedPoolUsage.get()
        set(value) = QuotaPagedPoolUsage.set(value)

    var quotaPeakNonPagedPoolUsage: Long
        get() = QuotaPeakNonPagedPoolUsage.get()
        set(value) = QuotaPeakNonPagedPoolUsage.set(value)

    var quotaNonPagedPoolUsage: Long
        get() = QuotaNonPagedPoolUsage.get()
        set(value) = QuotaNonPagedPoolUsage.set(value)

    var pagefileUsage: Long
        get() = PagefileUsage.get()
        set(value) = PagefileUsage.set(value)

    var peakPagefileUsage: Long
        get() = PeakPagefileUsage.get()
        set(value) = PeakPagefileUsage.set(value)

}
