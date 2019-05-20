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

import java.time.Instant

/**
 * @see [FILETIME](https://msdn.microsoft.com/zh-tw/library/windows/desktop/ms724284)
 * @author zhanhb
 */
@Suppress("SpellCheckingInspection")
class FILETIME : jnc.foreign.Struct() {

    private val dwLowDateTime = DWORD()
    private val dwHighDateTime = DWORD()

    var lowDateTime: Int
        get() = dwLowDateTime.toInt()
        set(value) = dwLowDateTime.set(value.toLong())

    var highDateTime: Int
        get() = dwHighDateTime.toInt()
        set(value) = dwHighDateTime.set(value.toLong())

    fun longValue(): Long {
        return highDateTime.toLong() shl 32 or (lowDateTime.toLong() and 0xFFFFFFFFL)
    }

    fun toInstant(): Instant = toInstant(longValue())

    companion object {
        private fun toInstant(filetime: Long): Instant {
            val t = filetime - 116444736000000000L
            return Instant.ofEpochSecond(t / 10000000, t % 10000000 * 100)
        }
    }

}
