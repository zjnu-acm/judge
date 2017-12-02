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
package com.github.zhanhb.judge.win32.struct;

/**
 *
 * @see JOBOBJECTINFOCLASS#BasicLimitInformation
 * @see
 * <a href="https://msdn.microsoft.com/en-us/library/windows/desktop/ms684147(v=vs.85).aspx">JOBOBJECT_BASIC_LIMIT_INFORMATION
 * </a>
 */
public class JOBOBJECT_BASIC_LIMIT_INFORMATION extends JOBOBJECT_INFORMATION {

    private final Signed64 PerProcessUserTimeLimit = new Signed64();
    private final Signed64 PerJobUserTimeLimit = new Signed64();
    private final DWORD LimitFlags = new DWORD();
    private final size_t MinimumWorkingSetSize = new size_t();
    private final size_t MaximumWorkingSetSize = new size_t();
    private final DWORD ActiveProcessLimit = new DWORD();
    private final uintptr_t Affinity = new uintptr_t();
    private final DWORD PriorityClass = new DWORD();
    private final DWORD SchedulingClass = new DWORD();

    public JOBOBJECT_BASIC_LIMIT_INFORMATION(jnr.ffi.Runtime runtime) {
        super(runtime);
    }

    public void setActiveProcessLimit(int activeProcessLimit) {
        ActiveProcessLimit.set(activeProcessLimit);
    }

    public void setLimitFlags(int limitFlags) {
        LimitFlags.set(limitFlags);
    }

}
