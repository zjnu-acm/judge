/*
 * Copyright 2019 ZJNU ACM.
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
package cn.edu.zjnu.acm.judge.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;
import java.util.Objects;

/**
 *
 * @author zhanhb
 */
public enum URLEncoder {

    /**
     * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/encodeURIComponent
     */
    URI_COMPONENT("!'()*-._~"),
    /**
     * pchar="!$&'()*+,-.:;=@_~"
     * https://tools.ietf.org/html/rfc3986#section-3.4
     */
    QUERY("!$'()*,-./:;?@_~"),
    PATH("!$'()*,-./:;@_~");

    private static final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();

    private static StringBuilder append(StringBuilder out, char[] buf, byte[] bytes) {
        char[] hexChars = HEX_CHARS;
        for (byte b : bytes) {
            buf[1] = hexChars[b >> 4 & 15];
            buf[2] = hexChars[b & 15];
            out.append(buf);
        }
        return out;
    }

    private final BitSet dontNeedEncoding;

    URLEncoder(String dontNeedEncoding) {
        BitSet bs = new BitSet(128);
        bs.set('a', 'z' + 1);
        bs.set('A', 'Z' + 1);
        bs.set('0', '9' + 1);
        for (int i = 0, len = dontNeedEncoding.length(); i < len; ++i) {
            bs.set(dontNeedEncoding.charAt(i));
        }
        this.dontNeedEncoding = bs;
    }

    /**
     *
     * @param s {@code String} to be translated.
     * @param charset the encoding to use.
     * @return the translated {@code String}.
     * @see java.net.URLEncoder#encode(java.lang.String, java.lang.String)
     * @throws NullPointerException s or charset is null
     */
    @SuppressWarnings({"AssignmentToForLoopParameter", "ValueOfIncrementOrDecrementUsed"})
    private String encode(String s, Charset charset) {
        final int length = s.length();
        Objects.requireNonNull(charset, "charset");
        BitSet bs = dontNeedEncoding;

        for (int i = 0; i < length; ++i) {
            if (!bs.get(s.charAt(i))) {
                char[] buf = new char[]{'%', 0, 0};
                StringBuilder out = new StringBuilder(length + 20).append(s, 0, i);
                for (int k = i;;) {
                    do {
                        if (++k == length) {
                            return append(out, buf, s.substring(i).getBytes(charset)).toString();
                        }
                    } while (!bs.get(s.charAt(k)));
                    append(out, buf, s.substring(i, k).getBytes(charset));
                    i = k;
                    do {
                        if (++k == length) {
                            return out.append(s, i, k).toString();
                        }
                    } while (bs.get(s.charAt(k)));
                    out.append(s, i, k);
                    i = k;
                }
            }
        }

        return s;
    }

    /**
     *
     * @param s {@code String} to be translated.
     * @return the translated {@code String}.
     * @throws NullPointerException s is null
     */
    public String encode(String s) {
        return encode(s, StandardCharsets.UTF_8);
    }

}
