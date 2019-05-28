/*
 * Copyright 2017-2019 ZJNU ACM.
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

import jnc.foreign.Foreign
import jnc.foreign.Pointer
import jnc.platform.LibC

/**
 * @author zhanhb
 */
interface WString {
    companion object {

        @JvmStatic
        fun toNative(string: String?): Pointer? {
            string ?: return null
            val len = string.length
            val chars = CharArray(len)
            string.toCharArray(chars, 0, 0, len)
            val pointer = Foreign.getDefault().memoryManager.allocate((len + 1 shl 1).toLong())
            pointer.putCharArray(0, chars, 0, len)
            return pointer
        }

        @JvmStatic
        fun fromNative(ptr: Pointer?): String? {
            ptr ?: return null
            val len = LibC.INSTANCE.wcslen(ptr)
            val chars = CharArray(len)
            ptr.getCharArray(0, chars, 0, len)
            return String(chars)
        }
    }

}
