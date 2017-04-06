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
package cn.edu.zjnu.acm.judge.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 *
 * @author zhanhb
 */
class URLEncoder {

    private static final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();

    private final BitSet dontNeedEncoding;

    URLEncoder() {
        BitSet bs = new BitSet(128);
        bs.set('a', 'z' + 1);
        bs.set('A', 'Z' + 1);
        bs.set('0', '9' + 1);
        dontNeedEncoding = bs;
    }

    URLEncoder(@Nonnull String dontNeedEncoding) {
        this();
        for (int i = 0, len = dontNeedEncoding.length(); i < len; ++i) {
            this.dontNeedEncoding.set(dontNeedEncoding.charAt(i));
        }
    }

    /**
     *
     * @param s {@code String} to be translated.
     * @param charset the encoding to use.
     * @return the translated {@code String}.
     * @see java.net.URLEncoder#encode(java.lang.String, java.lang.String)
     */
    public String encode(String s, Charset charset) {
        boolean needToChange = false;
        final int length = s.length();
        Objects.requireNonNull(charset, "charset");
        StringBuilder out = new StringBuilder(length);

        for (int cur = 0; cur < length;) {
            int start = cur;
            while (cur < length && dontNeedEncoding.get(s.charAt(cur))) {
                ++cur;
            }
            if (start != cur) {
                out.append(s, start, cur);
                start = cur;
            }
            while (cur < length && !dontNeedEncoding.get(s.charAt(cur))) {
                ++cur;
            }
            if (start != cur) {
                // convert to external encoding before hex conversion
                byte[] bytes = s.substring(start, cur).getBytes(charset);
                for (int j = 0, limit = bytes.length; j < limit; ++j) {
                    byte b = bytes[j];
                    out.append('%').append(HEX_CHARS[b >> 4 & 0xF]).append(HEX_CHARS[b & 0xF]);
                }
                needToChange = true;
            }
        }
        return needToChange ? out.toString() : s;
    }

    public String encode(String s) {
        return encode(s, StandardCharsets.UTF_8);
    }

}
