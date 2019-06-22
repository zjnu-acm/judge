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

import javax.annotation.Nonnull;
import jnc.foreign.StructArray;

@SuppressWarnings({"WeakerAccess", "unused"})
public final class TOKEN_PRIVILEGES extends TokenInformation {

    @Nonnull
    public static TOKEN_PRIVILEGES ofSize(int size) {
        return new TOKEN_PRIVILEGES(TOKEN_PRIVILEGES.Lazy.INFO.toCount(size));
    }

    private final DWORD PrivilegeCount = new DWORD();
    private final StructArray<LUID_AND_ATTRIBUTES> Privileges;

    private TOKEN_PRIVILEGES(int count) {
        Privileges = structArray(LUID_AND_ATTRIBUTES::new, count);
    }

    public TOKEN_PRIVILEGES() {
        this(WinNT.ANYSIZE_ARRAY);
    }

    public final int getPrivilegeCount() {
        return this.PrivilegeCount.intValue();
    }

    public final void setPrivilegeCount(int value) {
        this.PrivilegeCount.set(value);
    }

    @Nonnull
    public final LUID_AND_ATTRIBUTES get(int index) {
        return this.Privileges.get(index);
    }

    private interface Lazy {

        Info INFO = Info.of(TOKEN_PRIVILEGES::new, tp -> tp.get(0));

    }

}
