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

import java.util.Arrays

/**
 * @see [SID_IDENTIFIER_AUTHORITY](https://msdn.microsoft.com/en-us/library/windows/desktop/aa379598)
 * @author zhanhb
 */
@Suppress("ClassName")
class SID_IDENTIFIER_AUTHORITY : jnc.foreign.Struct() {

    init {
        padding(6) // actual type BYTE[6]
    }

    private fun toByteArray(): ByteArray {
        val bytes = ByteArray(6)
        memory.getBytes(0, bytes, 0, 6)
        return bytes
    }

    override fun hashCode(): Int = Arrays.hashCode(toByteArray())

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null) {
            return false
        }
        if (javaClass !== other.javaClass) {
            return false
        }
        val sia = other as SID_IDENTIFIER_AUTHORITY
        return Arrays.equals(toByteArray(), sia.toByteArray())
    }

}
