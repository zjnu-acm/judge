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

import javax.annotation.Nullable;

/**
 *
 * @author zhanhb
 */
public class Strings {

    @Nullable
    public static String slice(String string, int beginIndex) {
        return string == null ? null : string.substring(fixIndex(string, beginIndex));
    }

    @Nullable
    public static String slice(String string, int beginIndex, int endIndex) {
        if (string == null) {
            return null;
        }
        int fixedBeginIndex = fixIndex(string, beginIndex);
        int fixedEndIndex = fixIndex(string, endIndex);
        return (fixedBeginIndex >= fixedEndIndex) ? "" : string.substring(fixedBeginIndex, fixedEndIndex);
    }

    private static int fixIndex(String str, int index) {
        return (index >= 0) ? Math.min(str.length(), index) : Math.max(0, str.length() + index);
    }

    private Strings() {
        throw new AssertionError();
    }

}
