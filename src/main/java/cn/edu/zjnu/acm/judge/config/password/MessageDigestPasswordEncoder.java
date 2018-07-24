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

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author zhanhb
 */
public class MessageDigestPasswordEncoder implements PasswordEncoder {

    @Deprecated
    public static MessageDigestPasswordEncoder md5() {
        return MD5Holder.INSTANCE;
    }

    @Deprecated
    public static MessageDigestPasswordEncoder sha1() {
        return SHAHolder.INSTANCE;
    }

    public static MessageDigestPasswordEncoder sha256() {
        return SHA256Holder.INSTANCE;
    }

    public static MessageDigestPasswordEncoder sha384() {
        return SHA384Holder.INSTANCE;
    }

    public static MessageDigestPasswordEncoder sha512() {
        return SHA512Holder.INSTANCE;
    }

    private final Supplier<HashFunction> s;

    private MessageDigestPasswordEncoder(Supplier<HashFunction> s) {
        this.s = s;
    }

    @Override
    public String encode(CharSequence password) {
        return s.get().hashString(password, StandardCharsets.UTF_8).toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return s.get().bits() >>> 2 == encodedPassword.length()
                && encode(rawPassword).equalsIgnoreCase(encodedPassword);
    }

    @Deprecated
    private interface MD5Holder {

        MessageDigestPasswordEncoder INSTANCE = new MessageDigestPasswordEncoder(Hashing::md5);

    }

    @Deprecated
    private interface SHAHolder {

        MessageDigestPasswordEncoder INSTANCE = new MessageDigestPasswordEncoder(Hashing::sha1);

    }

    private interface SHA256Holder {

        MessageDigestPasswordEncoder INSTANCE = new MessageDigestPasswordEncoder(Hashing::sha256);

    }

    private interface SHA384Holder {

        MessageDigestPasswordEncoder INSTANCE = new MessageDigestPasswordEncoder(Hashing::sha384);

    }

    private interface SHA512Holder {

        MessageDigestPasswordEncoder INSTANCE = new MessageDigestPasswordEncoder(Hashing::sha512);

    }

}
