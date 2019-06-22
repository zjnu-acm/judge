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

import javax.annotation.Nonnull;

public final class TOKEN_USER extends TokenInformation {

    @Nonnull
    public static TOKEN_USER withPadding(int padding) {
        TOKEN_USER tokenUser = new TOKEN_USER();
        tokenUser.padding(padding);
        return tokenUser;
    }

    private final SID_AND_ATTRIBUTES user = inner(new SID_AND_ATTRIBUTES());

    @Nonnull
    public final SID_AND_ATTRIBUTES getUser() {
        return this.user;
    }

}
