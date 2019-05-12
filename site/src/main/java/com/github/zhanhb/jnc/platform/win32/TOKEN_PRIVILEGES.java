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

import static com.github.zhanhb.jnc.platform.win32.WinNT.ANYSIZE_ARRAY;

/**
 *
 * @author zhanhb
 */
public class TOKEN_PRIVILEGES extends TOKEN_INFORMATION {

    public static TOKEN_PRIVILEGES ofSize(int size) {
        return new TOKEN_PRIVILEGES(Lazy.INFO.toCount(size));
    }

    private final DWORD PrivilegeCount = new DWORD();
    private final LUID_AND_ATTRIBUTES[] Privileges;

    public TOKEN_PRIVILEGES() {
        this(ANYSIZE_ARRAY);
    }

    private TOKEN_PRIVILEGES(int count) {
        Privileges = new LUID_AND_ATTRIBUTES[count];
        for (int i = 0; i < count; ++i) {
            Privileges[i] = inner(new LUID_AND_ATTRIBUTES());
        }
    }

    public int getPrivilegeCount() {
        return PrivilegeCount.intValue();
    }

    public void setPrivilegeCount(int privilegeCount) {
        PrivilegeCount.set(privilegeCount);
    }

    public LUID_AND_ATTRIBUTES get(int index) {
        return Privileges[index];
    }

    private interface Lazy {

        Info INFO = Info.of(TOKEN_PRIVILEGES::new, tp -> tp.get(0));

    }

}
