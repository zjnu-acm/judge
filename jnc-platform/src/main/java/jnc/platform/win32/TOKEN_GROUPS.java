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
public final class TOKEN_GROUPS extends TokenInformation {

    @Nonnull
    public static TOKEN_GROUPS ofSize(int size) {
        return new TOKEN_GROUPS(Lazy.INFO.toCount(size));
    }

    private final DWORD GroupCount = new DWORD();
    private final StructArray<SID_AND_ATTRIBUTES> Groups;

    private TOKEN_GROUPS(int size) {
        Groups = structArray(SID_AND_ATTRIBUTES::new, size);
    }

    public TOKEN_GROUPS() {
        this(WinNT.ANYSIZE_ARRAY);
    }

    public final int getGroupCount() {
        return this.GroupCount.intValue();
    }

    public final void setGroupCount(int value) {
        this.GroupCount.set(value);
    }

    @Nonnull
    public final SID_AND_ATTRIBUTES get(int index) {
        return this.Groups.get(index);
    }

    private interface Lazy {

        Info INFO = Info.of(TOKEN_GROUPS::new, tp -> tp.get(0));

    }

}
