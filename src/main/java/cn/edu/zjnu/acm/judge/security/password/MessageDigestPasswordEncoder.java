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
@SuppressWarnings("UtilityClassWithoutPrivateConstructor")
public class MessageDigestPasswordEncoder implements PasswordEncoder {

    public static MessageDigestPasswordEncoder md5() {
        return MD5Holder.INSTANCE;
    }

    public static MessageDigestPasswordEncoder sha1() {
        return SHAHolder.INSTANCE;
    }

    public static MessageDigestPasswordEncoder sha256() {
        return SHA256Holder.INSTANCE;
    }

    public static MessageDigestPasswordEncoder sha512() {
        return SHA512Holder.INSTANCE;
    }

    private static MessageDigest getMessageDigest(String algorithmName) {
        try {
            return MessageDigest.getInstance(algorithmName);
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        }
    }

    private final String algorithm;
    private final int length;

    MessageDigestPasswordEncoder(String algorithm) {
        this.algorithm = algorithm;
        MessageDigest prototype = getMessageDigest(algorithm);
        this.length = prototype.getDigestLength() << 1;
    }

    @Override
    public String encode(CharSequence password) {
        MessageDigest digest = getMessageDigest(algorithm);
        return new String(Hex.encode(digest.digest(password != null ? password.toString().getBytes(StandardCharsets.UTF_8) : new byte[0])));
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return length == encodedPassword.length()
                && encode(rawPassword).equalsIgnoreCase(encodedPassword);
    }

    private static class MD5Holder {

        private static MessageDigestPasswordEncoder INSTANCE = new MessageDigestPasswordEncoder("MD5");

    }

    private static class SHAHolder {

        private static MessageDigestPasswordEncoder INSTANCE = new MessageDigestPasswordEncoder("SHA1");

    }

    private static class SHA256Holder {

        private static MessageDigestPasswordEncoder INSTANCE = new MessageDigestPasswordEncoder("SHA-256");

    }

    private static class SHA512Holder {

        private static MessageDigestPasswordEncoder INSTANCE = new MessageDigestPasswordEncoder("SHA-512");

    }

}
