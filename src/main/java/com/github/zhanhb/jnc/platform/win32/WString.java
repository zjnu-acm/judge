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
package com.github.zhanhb.jnc.platform.win32;

import jnc.foreign.ForeignProviders;
import jnc.foreign.Pointer;

/**
 *
 * @author zhanhb
 */
public interface WString {

    static Pointer toNative(String string) {
        if (string == null) {
            return null;
        }
        int len = string.length();
        char[] chars = new char[len];
        string.getChars(0, len, chars, 0);
        Pointer pointer = ForeignProviders.getDefault().getMemoryManager().allocate(len + 1 << 1);
        pointer.putCharArray(0, chars, 0, len);
        return pointer;
    }

    static String fromNative(Pointer ptr) {
        if (ptr == null) {
            return null;
        }
        int len = LibC.INSTANCE.wcslen(ptr);
        char[] chars = new char[len];
        ptr.getCharArray(0, chars, 0, len);
        return new String(chars);
    }

}
