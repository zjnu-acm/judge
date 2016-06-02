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
package cn.edu.zjnu.acm.judge.user;

import javax.annotation.Nullable;
import org.springframework.stereotype.Service;

/**
 *
 * @author zhanhb
 */
@Service
public class PasswordConfuser {

    private static final String[] STARS;

    static {
        STARS = new String[17];
        String t = "****************";
        STARS[16] = t;
        for (int i = 0; i < 16; i++) {
            STARS[i] = t.substring(0, i);
        }
    }

    @Nullable
    public String confuse(@Nullable String rawPassword) {
        if (rawPassword == null) {
            return null;
        }
        int len = rawPassword.length();
        if (len <= 4) {
            return STARS[len];
        }
        char s = rawPassword.charAt(0);
        char e = rawPassword.charAt(len - 1);
        if (len > 16) {
            return s + "......" + e + "(total " + len + " characters)";
        }
        return s + STARS[len - 2] + e;
    }

}
