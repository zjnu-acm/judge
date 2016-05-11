/*
 * Copyright 2014 zhanhb.
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

import cn.edu.zjnu.acm.judge.exception.MessageException;
import com.google.common.base.Strings;
import org.springframework.util.StringUtils;

public class ValueCheck {

    public static final String EMAIL_PATTERN = "[a-z0-9!#$%&'*+/=?^_`{|}~-]++(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]++)*+@[a-z0-9](?:[a-z0-9-]*[a-z0-9])?+(?:\\.[a-z0-9](?:[a-z0-9-]*[a-z0-9])?+)++";

    public static void checkUserId(String userId) {
        if (Strings.isNullOrEmpty(userId)) {
            throw new MessageException("User ID can not be NULL");
        }
        if (userId.length() < 6) {
            throw new MessageException("User ID is too short");
        }
        if (userId.length() > 20) {
            throw new MessageException("User ID is too long");
        }
        if (!userId.matches("(?i)[a-z0-9_]+")) {
            throw new MessageException("User ID can only contain number, letter and '_'");
        }
    }

    public static void checkPassword(String password) {
        if (Strings.isNullOrEmpty(password)) {
            throw new MessageException("Password can not be NULL");
        }
        if (password.length() > 20) {
            throw new MessageException("Password is too long");
        }
        if (password.length() < 6) {
            throw new MessageException("Password is too short");
        }
        for (int i = 0; i < password.length(); ++i) {
            if (password.charAt(i) == ' ') {
                throw new MessageException("Password can not contain spaces");
            }
        }
        for (int i = 0; i < password.length(); ++i) {
            char ch = password.charAt(i);
            if (ch >= 127 || ch < 32) {
                throw new MessageException("Password contains invalid character");
            }
        }
    }

    public static void checkNick(String nick) {
        if (Strings.isNullOrEmpty(nick)) {
            throw new MessageException("nick can not be NULL");
        }
        if (nick.length() > 64) {
            throw new MessageException("nick is too long");
        }
    }

    public static void checkEmail(String email) {
        if (StringUtils.hasLength(email) && !email.matches(EMAIL_PATTERN)) {
            throw new MessageException("email format incorrect");
        }
    }

    private ValueCheck() {
        throw new AssertionError();
    }

}
