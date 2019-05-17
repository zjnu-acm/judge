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
 * @see [PROCESS_INFORMATION](https://msdn.microsoft.com/en-us/library/windows/desktop/ms684873)
 * @author zhanhb
 */
@Suppress("ClassName", "unused")
class PROCESS_INFORMATION : jnc.foreign.Struct() {

    private val /*HANDLE*/ hProcess = uintptr_t()
    private val /*HANDLE*/ hThread = uintptr_t()
    private val dwProcessId = DWORD()
    private val dwThreadId = DWORD()

    var process: Long
        get() = hProcess.get()
        set(value) = hProcess.set(value)

    var thread: Long
        get() = hThread.get()
        set(value) = hThread.set(value)

    var processId: Int
        get() = dwProcessId.get().toInt()
        set(value) = dwProcessId.set(value.toLong())

    var threadId: Int
        get() = dwThreadId.get().toInt()
        set(value) = dwThreadId.set(value.toLong())

}
