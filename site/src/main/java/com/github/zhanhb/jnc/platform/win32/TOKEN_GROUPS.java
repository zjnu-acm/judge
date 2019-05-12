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
public class TOKEN_GROUPS extends TOKEN_INFORMATION {

    public static TOKEN_GROUPS ofSize(int size) {
        return new TOKEN_GROUPS(Lazy.INFO.toCount(size));
    }

    private final DWORD GroupCount = new DWORD();
    private final SID_AND_ATTRIBUTES[] Groups;

    public TOKEN_GROUPS() {
        this(ANYSIZE_ARRAY);
    }

    private TOKEN_GROUPS(int size) {
        Groups = new SID_AND_ATTRIBUTES[size];
        for (int i = 0; i < size; i++) {
            Groups[i] = inner(new SID_AND_ATTRIBUTES());
        }
    }

    public int getGroupCount() {
        return GroupCount.intValue();
    }

    public SID_AND_ATTRIBUTES get(int index) {
        return Groups[index];
    }

    private interface Lazy {

        Info INFO = Info.of(TOKEN_GROUPS::new, tp -> tp.get(0));

    }

}
