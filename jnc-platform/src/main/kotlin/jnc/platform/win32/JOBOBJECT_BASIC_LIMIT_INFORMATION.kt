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
 *
 * @see JOBOBJECTINFOCLASS.JobObjectBasicLimitInformation
 *
 * @see  [JOBOBJECT_BASIC_LIMIT_INFORMATION](https://msdn.microsoft.com/en-us/library/windows/desktop/ms684147)
 * @author zhanhb
 */
@Suppress("ClassName", "PrivatePropertyName", "unused")
class JOBOBJECT_BASIC_LIMIT_INFORMATION : JOBOBJECT_INFORMATION() {

    private val PerProcessUserTimeLimit = int64_t()
    private val PerJobUserTimeLimit = int64_t()
    private val LimitFlags = DWORD()
    private val MinimumWorkingSetSize = size_t()
    private val MaximumWorkingSetSize = size_t()
    private val ActiveProcessLimit = DWORD()
    private val /*ULONG_PTR*/ Affinity = uintptr_t()
    private val PriorityClass = DWORD()
    private val SchedulingClass = DWORD()

    var perProcessUserTimeLimit: Long
        get() = PerProcessUserTimeLimit.get()
        set(value) = PerProcessUserTimeLimit.set(value)

    var perJobUserTimeLimit: Long
        get() = PerJobUserTimeLimit.get()
        set(value) = PerJobUserTimeLimit.set(value)

    var limitFlags: Int
        get() = LimitFlags.get().toInt()
        set(value) = LimitFlags.set(value.toLong())

    var minimumWorkingSetSize: Long
        get() = MinimumWorkingSetSize.get()
        set(value) = MinimumWorkingSetSize.set(value)

    var maximumWorkingSetSize: Long
        get() = MaximumWorkingSetSize.get()
        set(value) = MaximumWorkingSetSize.set(value)

    var activeProcessLimit: Int
        get() = ActiveProcessLimit.get().toInt()
        set(value) = ActiveProcessLimit.set(value.toLong())

    var affinity: Long
        get() = Affinity.get()
        set(value) = Affinity.set(value)

    var priorityClass: Int
        get() = PriorityClass.get().toInt()
        set(value) = PriorityClass.set(value.toLong())

    var schedulingClass: Int
        get() = SchedulingClass.get().toInt()
        set(value) = SchedulingClass.set(value.toLong())

}
