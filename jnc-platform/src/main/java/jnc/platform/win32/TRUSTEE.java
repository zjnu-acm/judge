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

import java.util.Objects;
import javax.annotation.Nonnull;

@SuppressWarnings({"unused", "SpellCheckingInspection"})
public final class TRUSTEE extends jnc.foreign.Struct {

    private final uintptr_t pMultipleTrustee = new uintptr_t();
    private final EnumField<MULTIPLE_TRUSTEE_OPERATION> MultipleTrusteeOperation = enumField(MULTIPLE_TRUSTEE_OPERATION.class);
    private final EnumField<TRUSTEE_FORM> TrusteeForm = enumField(TRUSTEE_FORM.class);
    private final EnumField<TRUSTEE_TYPE> TrusteeType = enumField(TRUSTEE_TYPE.class);
    private final Pointer ptstrName = new Pointer();

    public final long getMultipleTrustee() {
        return this.pMultipleTrustee.get();
    }

    public final void setMultipleTrustee(long value) {
        this.pMultipleTrustee.set(value);
    }

    @Nonnull
    public final MULTIPLE_TRUSTEE_OPERATION getMultipleTrusteeOperation() {
        return this.MultipleTrusteeOperation.get();
    }

    public final void setMultipleTrusteeOperation(@Nonnull MULTIPLE_TRUSTEE_OPERATION value) {
        this.MultipleTrusteeOperation.set(Objects.requireNonNull(value));
    }

    @Nonnull
    public final TRUSTEE_FORM getTrusteeForm() {
        return this.TrusteeForm.get();
    }

    public final void setTrusteeForm(@Nonnull TRUSTEE_FORM value) {
        this.TrusteeForm.set(Objects.requireNonNull(value));
    }

    @Nonnull
    public final TRUSTEE_TYPE getTrusteeType() {
        return this.TrusteeType.get();
    }

    public final void setTrusteeType(@Nonnull TRUSTEE_TYPE value) {
        this.TrusteeType.set(Objects.requireNonNull(value));
    }

    public final jnc.foreign.Pointer getName() {
        return this.ptstrName.get();
    }

    public final void setName(jnc.foreign.Pointer value) {
        this.ptstrName.set(value);
    }
}
