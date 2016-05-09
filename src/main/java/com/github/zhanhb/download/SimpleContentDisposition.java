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
package com.github.zhanhb.download;

/**
 *
 * @author zhanhb
 * @see https://tools.ietf.org/html/rfc6266#section-4.1
 */
public enum SimpleContentDisposition implements ContentDisposition {

    ATTACHMENT("attachment"),
    INLINE("inline"),
    NONE(null) {

        @Override
        public String getContentDisposition(String filename) {
            return null;
        }

    };

    // https://tools.ietf.org/html/rfc5987#section-3.2.1
    // we will encoding + for some browser will decode + to a space
    static final URLEncoder CONTENT_DISPOSITION = new URLEncoder("!#$&-.^_`|~");
    private final String type;

    SimpleContentDisposition(String type) {
        this.type = type;
    }

    @Override
    public String getContentDisposition(String filename) {
        if (filename == null || filename.length() == 0) {
            return type;
        } else if (isToken(filename)) { // already a token
            return type + "; filename=" + filename;
        } else {
            String encoded = CONTENT_DISPOSITION.encode(filename);
            return type + "; filename=\"" + encoded + "\"; filename*=utf-8''" + encoded;
        }
    }

    private boolean isToken(String filename) {
        if (filename == null || filename.length() == 0) {
            return false;
        }
        for (int i = 0, len = filename.length(); i < len; ++i) {
            char ch = filename.charAt(i);
            if (ch >= 0x7F || ch < ' ') { // CHAR predicate
                return false;
            }
            switch (ch) {
                // token separators
                // @see https://tools.ietf.org/html/rfc2616#section-2.2
                case '(':
                case ')':
                case '<':
                case '>':
                case '@':
                case ',':
                case ';':
                case ':':
                case '\\':
                case '"':
                case '/':
                case '[':
                case ']':
                case '?':
                case '=':
                case '{':
                case '}':
                case ' ':
                case '\t':
                // should percent be a valid token???
                // here we are different from the rfc
                case '%':
                    return false;
            }
        }
        return true;
    }

}
