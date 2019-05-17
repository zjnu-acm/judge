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

/**
 * @author zhanhb
 */
@Suppress("ClassName", "PrivatePropertyName")
class TOKEN_GROUPS private constructor(size: Int) : TOKEN_INFORMATION() {

    private val GroupCount = DWORD()
    private val Groups: Array<SID_AND_ATTRIBUTES> = (0 until size).map { inner(SID_AND_ATTRIBUTES()) }.toTypedArray()

    var groupCount: Int
        get() = GroupCount.get().toInt()
        set(value) = GroupCount.set(value.toLong())

    constructor() : this(WinNT.ANYSIZE_ARRAY)

    operator fun get(index: Int): SID_AND_ATTRIBUTES = Groups[index]

    private object Lazy {
        val INFO = Info.of({ TOKEN_GROUPS() }, { tp -> tp[0] })
    }

    companion object {
        @JvmStatic
        fun ofSize(size: Int): TOKEN_GROUPS {
            return TOKEN_GROUPS(Lazy.INFO.toCount(size))
        }
    }

}
