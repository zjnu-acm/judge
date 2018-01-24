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

/**
 *
 * @author zhanhb
 */
public class TRUSTEE extends jnc.foreign.Struct {

    private final uintptr_t /*PTRUSTEE*/ pMultipleTrustee = new uintptr_t();
    private final int32_t /*MULTIPLE_TRUSTEE_OPERATION*/ MultipleTrusteeOperation = new int32_t();
    private final int32_t /*TRUSTEE_FORM*/ TrusteeForm = new int32_t();
    private final int32_t /*TRUSTEE_TYPE*/ TrusteeType = new int32_t();
    private final uintptr_t /*LPSTR*/ ptstrName = new uintptr_t();

    public long getMultipleTrustee() {
        return pMultipleTrustee.get();
    }

    public int getMultipleTrusteeOperation() {
        return MultipleTrusteeOperation.get();
    }

    public int getTrusteeForm() {
        return TrusteeForm.get();
    }

    public int getTrusteeType() {
        return TrusteeType.get();
    }

    public long getName() {
        return ptstrName.get();
    }

    public void setMultipleTrustee(long multipleTrustee) {
        this.pMultipleTrustee.set(multipleTrustee);
    }

    public void setMultipleTrusteeOperation(int multipleTrusteeOperation) {
        this.MultipleTrusteeOperation.set(multipleTrusteeOperation);
    }

    public void setMultipleTrusteeOperation(MULTIPLE_TRUSTEE_OPERATION multipleTrusteeOperation) {
        setMultipleTrusteeOperation(multipleTrusteeOperation.value());
    }

    public void setTrusteeForm(int trusteeForm) {
        this.TrusteeForm.set(trusteeForm);
    }

    public void setTrusteeForm(TRUSTEE_FORM trusteeForm) {
        setTrusteeForm(trusteeForm.value());
    }

    public void setTrusteeType(int trusteeType) {
        this.TrusteeType.set(trusteeType);
    }

    public void setName(long name) {
        this.ptstrName.set(name);
    }

}
