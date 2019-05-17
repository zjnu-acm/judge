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

import jnc.foreign.byref.IntByReference

/**
 * @see [LUID](https://msdn.microsoft.com/en-us/library/windows/desktop/aa379261)
 * @author zhanhb
 */
class LUID : jnc.foreign.Struct() {

    private val LowPart = DWORD()
    private val HighPart = int32_t()

    var lowPart: Int
        get() = LowPart.get().toInt()
        set(value) = LowPart.set(value.toLong())

    var highPart: Int
        get() = HighPart.get()
        set(value) = HighPart.set(value)

    private fun longValue(): Long {
        return highPart.toLong() shl 32 or (lowPart.toLong() and 0xFFFFFFFFL)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null) {
            return false
        }
        if (javaClass != other.javaClass) {
            return false
        }
        val luid = other as LUID
        return longValue() == luid.longValue()
    }

    override fun hashCode(): Int = java.lang.Long.hashCode(longValue())

    fun copyFrom(luid: LUID) {
        val value = luid.longValue()
        highPart = value.ushr(32).toInt()
        lowPart = value.toInt()
    }

    override fun toString(): String {
        val MAX_NAME = 256
        val ref = IntByReference(MAX_NAME)
        val name = CharArray(MAX_NAME)
        return if (Advapi32.INSTANCE.LookupPrivilegeNameW(null, this, name, ref)) {
            String(name, 0, ref.value)
        } else java.lang.Long.toHexString(longValue()).toUpperCase()
    }

    companion object {
        @JvmStatic
        fun lookup(systemName: String?, name: String): LUID {
            val luid = LUID()
            Kernel32Util.assertTrue(Advapi32.INSTANCE.LookupPrivilegeValueW(
                    WString.toNative(systemName), WString.toNative(name)!!, luid))
            return luid
        }

        @JvmStatic
        fun lookup(name: String): LUID = lookup(null, name)
    }

}
