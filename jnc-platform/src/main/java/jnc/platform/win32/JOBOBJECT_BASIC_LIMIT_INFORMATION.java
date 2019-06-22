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
package jnc.platform.win32;

/**
 * @see JOBOBJECTINFOCLASS#JobObjectBasicLimitInformation
 * @see <a href="https://msdn.microsoft.com/en-us/library/windows/desktop/ms684147>JOBOBJECT_BASIC_LIMIT_INFORMATION</a>
 */
@SuppressWarnings("unused")
public final class JOBOBJECT_BASIC_LIMIT_INFORMATION extends JobObjectInformation {

    private final int64_t PerProcessUserTimeLimit = new int64_t();
    private final int64_t PerJobUserTimeLimit = new int64_t();
    private final DWORD LimitFlags = new DWORD();
    private final size_t MinimumWorkingSetSize = new size_t();
    private final size_t MaximumWorkingSetSize = new size_t();
    private final DWORD ActiveProcessLimit = new DWORD();
    private final uintptr_t Affinity = new uintptr_t();
    private final DWORD PriorityClass = new DWORD();
    private final DWORD SchedulingClass = new DWORD();

    public final long getPerProcessUserTimeLimit() {
        return this.PerProcessUserTimeLimit.get();
    }

    public final void setPerProcessUserTimeLimit(long value) {
        this.PerProcessUserTimeLimit.set(value);
    }

    public final long getPerJobUserTimeLimit() {
        return this.PerJobUserTimeLimit.get();
    }

    public final void setPerJobUserTimeLimit(long value) {
        this.PerJobUserTimeLimit.set(value);
    }

    public final int getLimitFlags() {
        return this.LimitFlags.intValue();
    }

    public final void setLimitFlags(int value) {
        this.LimitFlags.set(value);
    }

    public final long getMinimumWorkingSetSize() {
        return this.MinimumWorkingSetSize.get();
    }

    public final void setMinimumWorkingSetSize(long value) {
        this.MinimumWorkingSetSize.set(value);
    }

    public final long getMaximumWorkingSetSize() {
        return this.MaximumWorkingSetSize.get();
    }

    public final void setMaximumWorkingSetSize(long value) {
        this.MaximumWorkingSetSize.set(value);
    }

    public final int getActiveProcessLimit() {
        return this.ActiveProcessLimit.intValue();
    }

    public final void setActiveProcessLimit(int value) {
        this.ActiveProcessLimit.set(value);
    }

    public final long getAffinity() {
        return this.Affinity.get();
    }

    public final void setAffinity(long value) {
        this.Affinity.set(value);
    }

    public final int getPriorityClass() {
        return this.PriorityClass.intValue();
    }

    public final void setPriorityClass(int value) {
        this.PriorityClass.set(value);
    }

    public final int getSchedulingClass() {
        return this.SchedulingClass.intValue();
    }

    public final void setSchedulingClass(int value) {
        this.SchedulingClass.set(value);
    }
}
