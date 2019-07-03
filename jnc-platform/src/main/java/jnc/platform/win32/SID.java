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
package jnc.platform.win32;

import java.util.Arrays;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import jnc.foreign.byref.IntByReference;
import jnc.foreign.byref.PointerByReference;

@SuppressWarnings({"WeakerAccess", "unused"})
public final class SID extends jnc.foreign.Struct {

    private static SID ofMaxSubAuthorities() {
        return new SID(WinNT.SID_MAX_SUB_AUTHORITIES);
    }

    @Nonnull
    public static SID copyOf(@Nonnull jnc.foreign.Pointer pSid) {
        SID sid = ofMaxSubAuthorities();
        Kernel32Util.assertTrue(Advapi32.INSTANCE.CopySid(sid.size(), sid, pSid));
        return sid;
    }

    @Nonnull
    public static SID ofWellKnown(@Nonnull WELL_KNOWN_SID_TYPE type) throws Win32Exception {
        Objects.requireNonNull(type, "type");
        SID sid = ofMaxSubAuthorities();
        IntByReference sizeSid = new IntByReference(sid.size());
        Kernel32Util.assertTrue(Advapi32.INSTANCE.CreateWellKnownSid(type, 0L, sid, sizeSid));
        return sid;
    }

    @Nonnull
    @SuppressWarnings("null")
    public static String toString(@Nonnull jnc.foreign.Pointer pSid) throws Win32Exception {
        PointerByReference stringSid = new PointerByReference();
        Kernel32Util.assertTrue(Advapi32.INSTANCE.ConvertSidToStringSidW(
                pSid, stringSid));
        jnc.foreign.Pointer ptr = stringSid.getValue();
        try {
            return WString.fromNative(ptr);
        } finally {
            Kernel32Util.freeLocalMemory(ptr);
        }
    }

    private final BYTE Revision = new BYTE();
    private final BYTE SubAuthorityCount = new BYTE();
    @Nonnull
    private final SID_IDENTIFIER_AUTHORITY identifierAuthority = inner(new SID_IDENTIFIER_AUTHORITY());
    private final DWORD[] SubAuthority;

    private SID(int subAuthorityCount) {
        SubAuthority = array(new DWORD[subAuthorityCount]);
    }

    public SID() {
        this(WinNT.ANYSIZE_ARRAY);
    }

    @Nonnull
    public final SID_IDENTIFIER_AUTHORITY getIdentifierAuthority() {
        return this.identifierAuthority;
    }

    public final byte getRevision() {
        return this.Revision.byteValue();
    }

    public final void setRevision(byte value) {
        this.Revision.set((short) value);
    }

    public final byte getSubAuthorityCount() {
        return this.SubAuthorityCount.byteValue();
    }

    public final void setSubAuthorityCount(byte value) {
        this.SubAuthorityCount.set((short) value);
    }

    @Nonnull
    public final int[] getSubAuthority() {
        int subAuthorityCount = SubAuthorityCount.get();
        int[] arrayOfInt = new int[subAuthorityCount];
        for (int i = 0; i < subAuthorityCount; ++i) {
            arrayOfInt[i] = this.SubAuthority[i].intValue();
        }
        return arrayOfInt;
    }

    public final boolean isValid() {
        return Advapi32.INSTANCE.IsValidSid(this);
    }

    public final jnc.foreign.Pointer asPSID() {
        return getMemory();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if (!(other instanceof SID)) {
            return false;
        }
        SID sid = (SID) other;
        return Advapi32.INSTANCE.EqualSid(asPSID(), sid.asPSID());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 13 * hash + getRevision();
        hash = 13 * hash + getSubAuthorityCount();
        hash = 13 * hash + this.identifierAuthority.hashCode();
        return 13 * hash + Arrays.hashCode(getSubAuthority());
    }

    @Nonnull
    @Override
    public String toString() {
        return toString(asPSID());
    }

}
