/*
 * Copyright 2018 ZJNU ACM.
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

import jnc.platform.win32.WinNT.ANYSIZE_ARRAY

/**
 * @author zhanhb
 */
@Suppress("ClassName", "PrivatePropertyName")
class TOKEN_PRIVILEGES private constructor(count: Int) : TokenInformation() {

    private val PrivilegeCount = DWORD()
    private val Privileges: Array<LUID_AND_ATTRIBUTES> = Array(count) { inner(LUID_AND_ATTRIBUTES()) }

    var privilegeCount: Int
        get() = PrivilegeCount.toInt()
        set(value) = PrivilegeCount.set(value.toLong())

    constructor() : this(ANYSIZE_ARRAY)

    operator fun get(index: Int): LUID_AND_ATTRIBUTES = Privileges[index]

    private object Lazy {
        val INFO = Info.of({ TOKEN_PRIVILEGES() }, { tp -> tp[0] })
    }

    companion object {
        @JvmStatic
        fun ofSize(size: Int): TOKEN_PRIVILEGES = TOKEN_PRIVILEGES(Lazy.INFO.toCount(size))
    }

}
