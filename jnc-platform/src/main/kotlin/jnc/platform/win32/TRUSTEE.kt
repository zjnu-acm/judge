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

/**
 * @author zhanhb
 */
@Suppress("PrivatePropertyName")
class TRUSTEE : jnc.foreign.Struct() {

    private val /*PTRUSTEE*/ pMultipleTrustee = uintptr_t()
    private val /*MULTIPLE_TRUSTEE_OPERATION*/ MultipleTrusteeOperation = int32_t()
    private val /*TRUSTEE_FORM*/ TrusteeForm = int32_t()
    private val /*TRUSTEE_TYPE*/ TrusteeType = int32_t()
    private val /*LPSTR*/ ptstrName = uintptr_t()

    var multipleTrustee: Long
        get() = pMultipleTrustee.get()
        set(value) = pMultipleTrustee.set(value)

    var multipleTrusteeOperation: Int
        get() = MultipleTrusteeOperation.get()
        set(value) = MultipleTrusteeOperation.set(value)

    var trusteeForm: Int
        get() = TrusteeForm.get()
        set(value) = TrusteeForm.set(value)

    var trusteeType: Int
        get() = TrusteeType.get()
        set(value) = TrusteeType.set(value)

    var name: Long
        get() = ptstrName.get()
        set(value) = ptstrName.set(value)

    fun setMultipleTrusteeOperation(multipleTrusteeOperation: MULTIPLE_TRUSTEE_OPERATION) {
        this.multipleTrusteeOperation = multipleTrusteeOperation.value()
    }

    fun setTrusteeForm(trusteeForm: TRUSTEE_FORM) {
        this.trusteeForm = trusteeForm.value()
    }

    fun setTrusteeType(trusteeType: TRUSTEE_TYPE) {
        this.trusteeType = trusteeType.value()
    }

}
