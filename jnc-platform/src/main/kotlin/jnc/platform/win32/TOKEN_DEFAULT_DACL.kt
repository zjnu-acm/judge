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
class TOKEN_DEFAULT_DACL : TOKEN_INFORMATION() {

    private val DefaultDacl = uintptr_t()

    var defaultDacl: Long
        get() = DefaultDacl.get()
        set(value) = DefaultDacl.set(value)

    private object Lazy {

        val SIZE = TOKEN_DEFAULT_DACL().size()

    }

    companion object {

        @JvmStatic
        fun ofSize(size: Int): TOKEN_DEFAULT_DACL {
            val tokenDefaultDacl = TOKEN_DEFAULT_DACL()
            if (size > Lazy.SIZE) {
                tokenDefaultDacl.padding(size - Lazy.SIZE)
            }
            return tokenDefaultDacl
        }
    }

}
