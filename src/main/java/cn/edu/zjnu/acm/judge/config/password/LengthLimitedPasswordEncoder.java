/*
 * Copyright 2015 zhanhb.
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
package cn.edu.zjnu.acm.judge.config.password;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author zhanhb
 */
public class LengthLimitedPasswordEncoder extends PasswordEncoderWrapper {

    private final int length;

    public LengthLimitedPasswordEncoder(PasswordEncoder parent, int length) {
        super(parent);
        this.length = length;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return super.encode(limit(rawPassword));
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return super.matches(limit(rawPassword), encodedPassword);
    }

    private CharSequence limit(CharSequence originPassword) {
        return originPassword.length() <= length
                ? originPassword
                : originPassword.subSequence(0, length);
    }

}
