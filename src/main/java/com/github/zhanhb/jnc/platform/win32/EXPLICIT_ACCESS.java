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
public class EXPLICIT_ACCESS extends jnc.foreign.Struct {

    private final DWORD grfAccessPermissions = new DWORD();
    private final int32_t /*ACCESS_MODE*/ grfAccessMode = new int32_t();
    private final DWORD grfInheritance = new DWORD();
    private final TRUSTEE Trustee = inner(new TRUSTEE());

    public int getAccessPermissions() {
        return grfAccessPermissions.intValue();
    }

    public void setAccessPermissions(int accessPermissions) {
        this.grfAccessPermissions.set(accessPermissions);
    }

    public int getAccessMode() {
        return grfAccessMode.get();
    }

    public void setAccessMode(int accessMode) {
        this.grfAccessMode.set(accessMode);
    }

    public void setAccessMode(ACCESS_MODE accessMode) {
        this.setAccessMode(accessMode.value());
    }

    public int getInheritance() {
        return grfInheritance.intValue();
    }

    public void setInheritance(int inheritance) {
        this.grfInheritance.set(inheritance);
    }

    public TRUSTEE getTrustee() {
        return Trustee;
    }

}
