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
import org.springframework.http.HttpStatus;
import org.thymeleaf.util.StringUtils;

public interface ValueCheck {

    String EMAIL_PATTERN = "[a-z0-9!#$%&'*+/=?^_`{|}~-]++(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]++)*+@[a-z0-9](?:[a-z0-9-]*[a-z0-9])?+(?:\\.[a-z0-9](?:[a-z0-9-]*[a-z0-9])?+)++";

    static void checkUserId(String userId) {
        if (StringUtils.isEmpty(userId)) {
            throw new MessageException("User ID can not be NULL", HttpStatus.BAD_REQUEST);
        }
        if (userId.length() < 6) {
            throw new MessageException("User ID is too short", HttpStatus.BAD_REQUEST);
        }
        if (userId.length() > 20) {
            throw new MessageException("User ID is too long", HttpStatus.BAD_REQUEST);
        }
        if (!userId.matches("(?i)[a-z0-9_]+")) {
            throw new MessageException("User ID can only contain number, letter and '_'", HttpStatus.BAD_REQUEST);
        }
    }

    static void checkPassword(String password) {
        if (StringUtils.isEmpty(password)) {
            throw new MessageException("Password can not be NULL", HttpStatus.BAD_REQUEST);
        }
        if (password.length() > 20) {
            throw new MessageException("Password is too long", HttpStatus.BAD_REQUEST);
        }
        if (password.length() < 6) {
            throw new MessageException("Password is too short", HttpStatus.BAD_REQUEST);
        }
        for (int i = 0; i < password.length(); ++i) {
            if (password.charAt(i) == ' ') {
                throw new MessageException("Password can not contain spaces", HttpStatus.BAD_REQUEST);
            }
        }
        for (int i = 0; i < password.length(); ++i) {
            char ch = password.charAt(i);
            if (ch >= 127 || ch < 32) {
                throw new MessageException("Password contains invalid character", HttpStatus.BAD_REQUEST);
            }
        }
    }

    static void checkNick(String nick) {
        if (StringUtils.isEmpty(nick)) {
            throw new MessageException("nick can not be NULL", HttpStatus.BAD_REQUEST);
        }
        if (nick.length() > 64) {
            throw new MessageException("nick is too long", HttpStatus.BAD_REQUEST);
        }
    }

    static void checkEmail(String email) {
        if (!StringUtils.isEmptyOrWhitespace(email) && !email.matches(EMAIL_PATTERN)) {
            throw new MessageException("email format incorrect", HttpStatus.BAD_REQUEST);
        }
    }

}
