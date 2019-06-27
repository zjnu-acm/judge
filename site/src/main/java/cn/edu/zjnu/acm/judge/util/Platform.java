/*
 * Copyright 2016 ZJNU ACM.
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
import java.nio.charset.UnsupportedCharsetException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhanhb
 */
@Slf4j
@SuppressWarnings("UtilityClassWithoutPrivateConstructor")
public class Platform {

    private static final Charset platformCharset = determinCharset();

    private static Charset determinCharset() {
        try {
            return Charset.forName(System.getProperty("sun.jnu.encoding"));
        } catch (UnsupportedCharsetException ex) {
            log.error("fail to got system charset, use file.encoding {} instead", Charset.defaultCharset(), ex);
        }
        return Charset.defaultCharset();
    }

    /**
     * There are two usage of this charset now. 1. write user submitted source
     * code to file. 2. input stream of special judge process. For these two
     * case, we should not depends file.encoding, which might be changed to
     * UTF-8 in the future.
     *
     * @return the platform charset
     */
    public static Charset getCharset() {
        return platformCharset;
    }

    public static boolean isWindows() {
        return jnc.foreign.Platform.getNativePlatform().getOS().isWindows();
    }

}
