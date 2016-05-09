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

import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.concurrent.ExecutionException;
import javax.annotation.Nullable;
import org.springframework.stereotype.Service;

/**
 *
 * @author zhanhb
 */
@Service
public class PasswordConfuser {

    private static final Cache<Integer, String> CACHE
            = CacheBuilder.newBuilder().weakValues().build();

    private static String repeat(int n) {
        try {
            return CACHE.get(n, () -> Strings.repeat("*", n));
        } catch (ExecutionException impossible) {
            throw new Error(impossible);
        }
    }

    @Nullable
    public String confuse(@Nullable String rawPassword) {
        if (rawPassword == null) {
            return null;
        }
        int len = rawPassword.length();
        if (len <= 4) {
            return repeat(len);
        }
        char s = rawPassword.charAt(0);
        char e = rawPassword.charAt(len - 1);
        if (len > 16) {
            return s + "......" + e + "(total " + len + " characters)";
        }
        return s + repeat(len - 2) + e;
    }

}
