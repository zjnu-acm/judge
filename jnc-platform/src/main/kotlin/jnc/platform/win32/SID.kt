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

import java.util.Arrays
import jnc.foreign.byref.IntByReference
import jnc.foreign.byref.PointerByReference

/**
 * @author zhanhb
 */
@Suppress("PrivatePropertyName", "MemberVisibilityCanBePrivate", "unused", "SpellCheckingInspection")
class SID private constructor(subAuthorityCount: Int) : jnc.foreign.Struct() {

    private val Revision = BYTE()
    private val SubAuthorityCount = BYTE()
    val identifierAuthority: SID_IDENTIFIER_AUTHORITY = inner(SID_IDENTIFIER_AUTHORITY())
    private val SubAuthority = array(arrayOfNulls<DWORD>(subAuthorityCount))

    var revision: Byte
        get() = Revision.byteValue()
        set(value) = this.Revision.set(value.toShort())

    var subAuthorityCount: Byte
        get() = SubAuthorityCount.get().toByte()
        set(value) = SubAuthorityCount.set(value.toShort())

    val subAuthority: IntArray
        get() = (0 until subAuthorityCount.toInt())
                .map { SubAuthority[it].get().toInt() }
                .toIntArray()

    val isValid: Boolean
        get() = Advapi32.INSTANCE.IsValidSid(this)

    constructor() : this(WinNT.ANYSIZE_ARRAY)

    fun asPSID(): Long = memory.address()

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
        val sid = other as SID
        return Advapi32.INSTANCE.EqualSid(asPSID(), sid.asPSID())
    }

    override fun hashCode(): Int {
        var hash = 5
        hash = 13 * hash + revision
        hash = 13 * hash + subAuthorityCount
        hash = 13 * hash + identifierAuthority.hashCode()
        hash = 13 * hash + Arrays.hashCode(subAuthority)
        return hash
    }

    override fun toString(): String = toString(asPSID())

    companion object {

        private fun ofMaxSubAuthorities(): SID = SID(WinNT.SID_MAX_SUB_AUTHORITIES)

        @JvmStatic
        fun copyOf(pSid: Long): SID {
            val sid = ofMaxSubAuthorities()
            Kernel32Util.assertTrue(Advapi32.INSTANCE.CopySid(sid.size(), sid, pSid))
            return sid
        }

        @JvmStatic
        @Throws(Win32Exception::class)
        fun ofWellKnown(type: WELL_KNOWN_SID_TYPE): SID {
            val sid = ofMaxSubAuthorities()
            val sizeSid = IntByReference(sid.size())
            Kernel32Util.assertTrue(Advapi32.INSTANCE.CreateWellKnownSid(type, 0 /*nullptr*/, sid, sizeSid))
            return sid
        }

        @JvmStatic
        @Throws(Win32Exception::class)
        fun toString(pSid: Long): String {
            val stringSid = PointerByReference()
            Kernel32Util.assertTrue(Advapi32.INSTANCE.ConvertSidToStringSidW(
                    pSid, stringSid))
            val ptr = stringSid.value
            try {
                return WString.fromNative(ptr)!!
            } finally {
                Kernel32Util.freeLocalMemory(ptr.address())
            }
        }
    }

}
