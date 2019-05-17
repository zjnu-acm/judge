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

import jnc.foreign.Struct

/**
 * @author zhanhb
 */
internal class Info private constructor(private val a: Int, private val e: Int) {

    fun toCount(size: Int): Int {
        return Math.max((size - a + e - 1) / e, 0) + 1
    }

    companion object {
        fun <T : Struct> of(supplier: () -> T, anySizeArrayElement: (T) -> Struct): Info {
            val struct = supplier()
            val apply = anySizeArrayElement(struct)
            return Info(struct.size(), apply.size())
        }
    }

}
