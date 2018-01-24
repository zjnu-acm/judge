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
package com.github.zhanhb.jnc.platform.win32;

import java.util.Arrays;
import jnc.foreign.byref.IntByReference;
import jnc.foreign.byref.PointerByReference;

import static com.github.zhanhb.jnc.platform.win32.WinNT.ANYSIZE_ARRAY;
import static com.github.zhanhb.jnc.platform.win32.WinNT.SID_MAX_SUB_AUTHORITIES;

public class SID extends jnc.foreign.Struct {

    private static SID ofMaxSubAuthorities() {
        return new SID(SID_MAX_SUB_AUTHORITIES);
    }

    public static SID copyOf(long pSid) {
        SID sid = ofMaxSubAuthorities();
        Kernel32Util.assertTrue(Advapi32.INSTANCE.CopySid(sid.size(), sid, pSid));
        return sid;
    }

    public static SID ofWellKnown(WELL_KNOWN_SID_TYPE type) throws Win32Exception {
        SID sid = ofMaxSubAuthorities();
        IntByReference sizeSid = new IntByReference(sid.size());
        Kernel32Util.assertTrue(Advapi32.INSTANCE.CreateWellKnownSid(type.value(), 0 /*nullptr*/, sid, sizeSid));
        return sid;
    }

    public static String toString(long pSid) {
        PointerByReference stringSid = new PointerByReference();
        Kernel32Util.assertTrue(Advapi32.INSTANCE.ConvertSidToStringSidW(
                pSid, stringSid));
        jnc.foreign.Pointer ptr = stringSid.getValue();
        try {
            return WString.fromNative(ptr);
        } finally {
            Kernel32Util.freeLocalMemory(ptr.address());
        }
    }

    private final BYTE Revision = new BYTE();
    private final BYTE SubAuthorityCount = new BYTE();
    private final SID_IDENTIFIER_AUTHORITY IdentifierAuthority = inner(new SID_IDENTIFIER_AUTHORITY());
    private final DWORD[] SubAuthority;

    private SID(int subAuthorityCount) {
        SubAuthority = array(new DWORD[subAuthorityCount]);
    }

    public SID() {
        this(ANYSIZE_ARRAY);
    }

    public long asPSID() {
        return getMemory().address();
    }

    public byte getRevision() {
        return Revision.byteValue();
    }

    public void setRevision(byte revision) {
        this.Revision.set(revision);
    }

    public byte getSubAuthorityCount() {
        return SubAuthorityCount.byteValue();
    }

    public void setSubAuthorityCount(byte subAuthorityCount) {
        this.SubAuthorityCount.set(subAuthorityCount);
    }

    public SID_IDENTIFIER_AUTHORITY getIdentifierAuthority() {
        return IdentifierAuthority;
    }

    public int[] getSubAuthority() {
        int[] subAuthority = new int[getSubAuthorityCount()];
        for (int i = 0, len = subAuthority.length; i < len; ++i) {
            subAuthority[i] = SubAuthority[i].intValue();
        }
        return subAuthority;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SID other = (SID) obj;
        return Advapi32.INSTANCE.EqualSid(asPSID(), other.asPSID());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 13 * hash + getRevision();
        hash = 13 * hash + getSubAuthorityCount();
        hash = 13 * hash + IdentifierAuthority.hashCode();
        hash = 13 * hash + Arrays.hashCode(getSubAuthority());
        return hash;
    }

    @Override
    public String toString() {
        return toString(asPSID());
    }

}
