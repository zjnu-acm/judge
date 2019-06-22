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

@SuppressWarnings("SpellCheckingInspection")
public final class TOKEN_DEFAULT_DACL extends TokenInformation {

    public static TOKEN_DEFAULT_DACL ofSize(int size) {
        TOKEN_DEFAULT_DACL tokenDefaultDacl = new TOKEN_DEFAULT_DACL();
        if (size > Lazy.SIZE) {
            tokenDefaultDacl.padding(size - Lazy.SIZE);
        }
        return tokenDefaultDacl;
    }

    private final uintptr_t DefaultDacl = new uintptr_t();

    public final long getDefaultDacl() {
        return this.DefaultDacl.get();
    }

    public final void setDefaultDacl(long value) {
        this.DefaultDacl.set(value);
    }

    private interface Lazy {

        int SIZE = new TOKEN_DEFAULT_DACL().size();

    }

}
