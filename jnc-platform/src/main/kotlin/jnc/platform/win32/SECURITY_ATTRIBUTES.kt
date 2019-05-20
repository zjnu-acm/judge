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
 * @see [SECURITY_ATTRIBUTES](https://msdn.microsoft.com/en-us/library/windows/desktop/aa379560)
 * @author zhanhb
 */
@Suppress("ClassName", "unused")
class SECURITY_ATTRIBUTES : jnc.foreign.Struct() {

    private val nLength = DWORD()
    private val /*LPVOID*/ lpSecurityDescriptor = uintptr_t()
    private val bInheritHandle = WBOOL()

    var length: Int
        get() = nLength.toInt()
        set(value) = nLength.set(value.toLong())

    var securityDescriptor: Long
        get() = lpSecurityDescriptor.get()
        set(value) = lpSecurityDescriptor.set(value)

    var inheritHandle: Boolean
        get() = bInheritHandle.get()
        set(value) = bInheritHandle.set(value)

}
