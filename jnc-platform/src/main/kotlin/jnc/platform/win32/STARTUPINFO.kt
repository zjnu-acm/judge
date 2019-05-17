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
 * @see [STARTUPINFO](https://msdn.microsoft.com/zh-tw/library/windows/desktop/ms686331)
 * @author zhanhb
 */
@Suppress("SpellCheckingInspection", "unused")
class STARTUPINFO : jnc.foreign.Struct() {

    private val cb = DWORD()
    private val /*LPTSTR*/ lpReserved = uintptr_t() //new UTF8String();
    private val /*LPTSTR*/ lpDesktop = uintptr_t() //UTF8String();
    private val /*LPTSTR*/ lpTitle = uintptr_t() //new UTF8String();
    private val dwX = DWORD()
    private val dwY = DWORD()
    private val dwXSize = DWORD()
    private val dwYSize = DWORD()
    private val dwXCountChars = DWORD()
    private val dwYCountChars = DWORD()
    private val dwFillAttribute = DWORD()
    private val dwFlags = DWORD()
    private val wShowWindow = WORD()
    private val cbReserved2 = WORD()
    private val /*LPBYTE*/ lpReserved2 = uintptr_t()
    private val /*HANDLE*/ hStdInput = uintptr_t()
    private val /*HANDLE*/ hStdOutput = uintptr_t()
    private val /*HANDLE*/ hStdError = uintptr_t()

    fun getCb(): Int = cb.get().toInt()
    fun setCb(cb: Int) = this.cb.set(cb.toLong())

    var desktop: Long
        get() = lpDesktop.get()
        set(value) = lpDesktop.set(value)

    var title: Long
        get() = lpTitle.get()
        set(value) = lpTitle.set(value)

    var x: Int
        get() = dwX.get().toInt()
        set(value) = dwX.set(value.toLong())

    var y: Int
        get() = dwY.get().toInt()
        set(value) = dwY.set(value.toLong())

    var xSize: Int
        get() = dwXSize.get().toInt()
        set(value) = dwXSize.set(value.toLong())

    var ySize: Int
        get() = dwYSize.get().toInt()
        set(value) = dwYSize.set(value.toLong())

    var xCountChars: Int
        get() = dwXCountChars.get().toInt()
        set(value) = dwXCountChars.set(value.toLong())

    var yCountChars: Int
        get() = dwYCountChars.get().toInt()
        set(value) = dwYCountChars.set(value.toLong())

    var fillAttribute: Int
        get() = dwFillAttribute.get().toInt()
        set(value) = dwFillAttribute.set(value.toLong())

    var flags: Int
        get() = dwFlags.get().toInt()
        set(value) = dwFlags.set(value.toLong())

    var showWindow: Char
        get() = wShowWindow.intValue().toChar()
        set(value) = wShowWindow.set(value)

    var stdInput: Long
        get() = hStdInput.get()
        set(value) = hStdInput.set(value)

    var stdOutput: Long
        get() = hStdOutput.get()
        set(value) = hStdOutput.set(value)

    var stdError: Long
        get() = hStdError.get()
        set(value) = hStdError.set(value)

}
