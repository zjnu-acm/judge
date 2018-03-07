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
public class TOKEN_DEFAULT_DACL extends TOKEN_INFORMATION {

    public static TOKEN_DEFAULT_DACL ofSize(int size) {
        TOKEN_DEFAULT_DACL tokenDefaultDacl = new TOKEN_DEFAULT_DACL();
        if (size > Lazy.SIZE) {
            tokenDefaultDacl.padding(size - Lazy.SIZE);
        }
        return tokenDefaultDacl;
    }

    private final uintptr_t DefaultDacl = new uintptr_t();

    public long getDefaultDacl() {
        return DefaultDacl.get();
    }

    public void setDefaultDacl(long defaultDacl) {
        this.DefaultDacl.set(defaultDacl);
    }

    private interface Lazy {

        int SIZE = new TOKEN_DEFAULT_DACL().size();

    }

}
