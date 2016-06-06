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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author zhanhb
 */
public enum MessageDigestPasswordEncoder implements PasswordEncoder {

    MD5("MD5"),
    SHA1("SHA1"),
    SHA256("SHA-256"),
    SHA512("SHA-512");

    private final String algorithm;
    private final int bytes;

    MessageDigestPasswordEncoder(String algorithm) {
        this.algorithm = algorithm;
        MessageDigest prototype = getMessageDigest(algorithm);
        this.bytes = prototype.getDigestLength();
    }

    @Override
    public String encode(CharSequence password) {
        MessageDigest digest = getMessageDigest(algorithm);
        return new String(Hex.encode(digest.digest(password != null ? password.toString().getBytes(StandardCharsets.UTF_8) : new byte[0])));
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return bytes << 1 == encodedPassword.length()
                && encode(rawPassword).equalsIgnoreCase(encodedPassword);
    }

    private static MessageDigest getMessageDigest(String algorithmName) {
        try {
            return MessageDigest.getInstance(algorithmName);
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        }
    }

}
