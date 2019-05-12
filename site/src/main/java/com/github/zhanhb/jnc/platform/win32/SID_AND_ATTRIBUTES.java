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

/**
 * @see
 * <a href="https://msdn.microsoft.com/en-us/library/windows/desktop/aa379595(v=vs.85).aspx">SID_AND_ATTRIBUTES</a>
 */
public class SID_AND_ATTRIBUTES extends jnc.foreign.Struct {

    /**
     * Pointer to a SID structure.
     */
    private final uintptr_t /*PSID*/ Sid = new uintptr_t();

    /**
     * Specifies attributes of the SID. This value contains up to 32 one-bit
     * flags. Its meaning depends on the definition and use of the SID.
     */
    private final DWORD Attributes = new DWORD();

    public long getSid() {
        return Sid.get();
    }

    public void setSid(long sid) {
        this.Sid.set(sid);
    }

    public int getAttributes() {
        return Attributes.intValue();
    }

    public void setAttributes(int attrubites) {
        this.Attributes.set(attrubites);
    }

}
