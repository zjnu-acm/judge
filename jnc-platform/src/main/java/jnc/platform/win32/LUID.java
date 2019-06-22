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
package jnc.platform.win32;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import jnc.foreign.byref.IntByReference;

/**
 * @see <a href="https://msdn.microsoft.com/en-us/library/windows/desktop/aa379261">LUID</a>
 */
@SuppressWarnings("WeakerAccess")
public final class LUID extends jnc.foreign.Struct {

    @Nonnull
    public static LUID lookup(@Nullable String systemName, @Nonnull String name) {
        LUID luid = new LUID();
        Kernel32Util.assertTrue(Advapi32.INSTANCE.LookupPrivilegeValueW(WString.toNative(systemName), WString.toNative(name), luid));
        return luid;
    }

    @Nonnull
    public static LUID lookup(@Nonnull String name) {
        return lookup(null, name);
    }

    private final DWORD LowPart = new DWORD();
    private final int32_t HighPart = new int32_t();

    public final int getLowPart() {
        return this.LowPart.intValue();
    }

    public final void setLowPart(int value) {
        this.LowPart.set(value);
    }

    public final int getHighPart() {
        return this.HighPart.get();
    }

    public final void setHighPart(int value) {
        this.HighPart.set(value);
    }

    private long longValue() {
        return (long) getHighPart() << 32 | getLowPart() & 0xFFFFFFFFL;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if (!(other instanceof LUID)) {
            return false;
        }
        LUID luid = (LUID) other;
        return longValue() == luid.longValue();
    }

    @Override
    public int hashCode() {
        return Long.hashCode(longValue());
    }

    public final void copyFrom(@Nonnull LUID luid) {
        long value = luid.longValue();
        setHighPart((int) (value >>> 32));
        setLowPart((int) value);
    }

    @Nonnull
    @Override
    public String toString() {
        int MAX_NAME = 256;
        IntByReference ref = new IntByReference(MAX_NAME);
        char[] name = new char[MAX_NAME];
        return Advapi32.INSTANCE.LookupPrivilegeNameW(null, this, name, ref) ? new String(name, 0, ref.getValue()) : Long.toHexString(longValue()).toUpperCase();
    }

}
