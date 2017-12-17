/*
 * Copyright 2017 ZJNU ACM.
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
package com.github.zhanhb.judge.win32;

import com.google.common.annotations.VisibleForTesting;
import java.nio.charset.StandardCharsets;
import jnc.foreign.Pointer;

/**
 *
 * @author zhanhb
 */
@SuppressWarnings("UtilityClassWithoutPrivateConstructor")
public class WString {

    public static byte[] toNative(String string) {
        return string == null ? null : (string + '\0').getBytes(StandardCharsets.UTF_16LE);
    }

    @VisibleForTesting
    static String fromNative(Pointer ptr) {
        int length = LibC.INSTANCE.wcslen(ptr);
        byte[] bytes = new byte[length << 1];
        ptr.getBytes(0, bytes, 0, bytes.length);
        return new String(bytes, StandardCharsets.UTF_16LE);
    }

}
