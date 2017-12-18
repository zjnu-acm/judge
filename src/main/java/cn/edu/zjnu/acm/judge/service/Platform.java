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
package cn.edu.zjnu.acm.judge.service;

import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 *
 * @author zhanhb
 */
@SuppressWarnings("UtilityClassWithoutPrivateConstructor")
class Platform {

    private static String getProperty(String name) {
        PrivilegedAction<String> action = () -> System.getProperty(name);
        return AccessController.doPrivileged(action);
    }

    static Charset getCharset() {
        try {
            return Charset.forName(getProperty("sun.jnu.encoding"));
        } catch (UnsupportedCharsetException | SecurityException ignored) {
        }
        return Charset.defaultCharset();
    }

    static boolean isWindows() {
        return jnc.foreign.Platform.getNativePlatform().getOS().isWindows();
    }

}
