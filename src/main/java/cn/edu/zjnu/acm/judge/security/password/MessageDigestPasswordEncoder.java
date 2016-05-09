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
package cn.edu.zjnu.acm.judge.security.password;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author zhanhb
 */
@RequiredArgsConstructor
public enum MessageDigestPasswordEncoder implements PasswordEncoder {

    MD5(Hashing.md5()),
    SHA1(Hashing.sha1()),
    SHA256(Hashing.sha256()),
    SHA512(Hashing.sha512());

    private final HashFunction function;

    @Override
    public String encode(CharSequence password) {
        return function.hashString(password != null ? password : "", StandardCharsets.UTF_8).toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return function.bits() >> 2 == encodedPassword.length()
                && encode(rawPassword).equalsIgnoreCase(encodedPassword);
    }

}
