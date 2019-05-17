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
class EXPLICIT_ACCESS : jnc.foreign.Struct() {

    private val grfAccessPermissions = DWORD()
    private val /*ACCESS_MODE*/ grfAccessMode = int32_t()
    private val grfInheritance = DWORD()
    val trustee: TRUSTEE = inner(TRUSTEE())

    var accessPermissions: Int
        get() = grfAccessPermissions.get().toInt()
        set(value) = grfAccessPermissions.set(value.toLong())

    var accessMode: Int
        get() = grfAccessMode.get()
        set(value) = grfAccessMode.set(value)

    var inheritance: Int
        get() = grfInheritance.get().toInt()
        set(value) = grfInheritance.set(value.toLong())

    fun setAccessMode(value: ACCESS_MODE) {
        this.accessMode = value.value()
    }

}
