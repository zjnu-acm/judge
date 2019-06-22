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

@SuppressWarnings("unused")
public final class EXPLICIT_ACCESS extends jnc.foreign.Struct {
    private final DWORD grfAccessPermissions = new DWORD();
    private final EnumField<ACCESS_MODE> grfAccessMode = enumField(ACCESS_MODE.class);
    private final DWORD grfInheritance = new DWORD();
    private final TRUSTEE trustee = inner(new TRUSTEE());

    @Nonnull
    public final TRUSTEE getTrustee() {
        return this.trustee;
    }

    public final int getAccessPermissions() {
        return this.grfAccessPermissions.intValue();
    }

    public final void setAccessPermissions(int value) {
        this.grfAccessPermissions.set(value);
    }

    @Nonnull
    public final ACCESS_MODE getAccessMode() {
        return this.grfAccessMode.get();
    }

    public final void setAccessMode(@Nonnull ACCESS_MODE value) {
        this.grfAccessMode.set(value);
    }

    public final int getInheritance() {
        return this.grfInheritance.intValue();
    }

    public final void setInheritance(int value) {
        this.grfInheritance.set(value);
    }
}
