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
package com.github.zhanhb.jnc.platform.win32;

import jnc.foreign.byref.IntByReference;

/**
 * @see
 * <a href="https://msdn.microsoft.com/en-us/library/windows/desktop/aa379261(v=vs.85).aspx">LUID</a>
 */
public class LUID extends jnc.foreign.Struct {

    public static LUID lookup(String systemName, String name) {
        LUID luid = new LUID();
        Kernel32Util.assertTrue(Advapi32.INSTANCE.LookupPrivilegeValueW(
                WString.toNative(systemName), WString.toNative(name), luid));
        return luid;
    }

    public static LUID lookup(String name) {
        return lookup(null, name);
    }

    private final DWORD LowPart = new DWORD();
    private final int32_t HighPart = new int32_t();

    public int getLowPart() {
        return LowPart.intValue();
    }

    public void setLowPart(int lowPart) {
        this.LowPart.set(lowPart);
    }

    public int getHighPart() {
        return HighPart.get();
    }

    public void setHighPart(int highPart) {
        this.HighPart.set(highPart);
    }

    private long longValue() {
        return (long) getHighPart() << 32 | getLowPart() & 0xFFFFFFFFL;
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
        final LUID other = (LUID) obj;
        return longValue() == other.longValue();
    }

    @Override
    public int hashCode() {
        return Long.hashCode(longValue());
    }

    public void copyFrom(LUID luid) {
        long value = luid.longValue();
        setHighPart((int) (value >>> 32));
        setLowPart((int) value);
    }

    @Override
    public String toString() {
        final int MAX_NAME = 256;
        IntByReference ref = new IntByReference(MAX_NAME);
        char[] name = new char[MAX_NAME];
        if (Advapi32.INSTANCE.LookupPrivilegeNameW(null, this, name, ref)) {
            return new String(name, 0, ref.getValue());
        }
        return Long.toHexString(longValue()).toUpperCase();
    }

}
