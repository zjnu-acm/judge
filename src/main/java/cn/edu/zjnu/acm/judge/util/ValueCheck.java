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

import cn.edu.zjnu.acm.judge.exception.BusinessCode;
import cn.edu.zjnu.acm.judge.exception.BusinessException;
import javax.annotation.Nullable;
import org.springframework.util.StringUtils;

public interface ValueCheck {

    String EMAIL_PATTERN = "[a-z0-9!#$%&'*+/=?^_`{|}~-]++(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]++)*+@[a-z0-9](?:[a-z0-9-]*[a-z0-9])?+(?:\\.[a-z0-9](?:[a-z0-9-]*[a-z0-9])?+)++";

    static void checkUserId(@Nullable String userId) {
        if (StringUtils.isEmpty(userId)) {
            throw new BusinessException(BusinessCode.REGIST_USER_ID_EMPTY);
        }
        assert userId != null;
        if (userId.length() < 6) {
            throw new BusinessException(BusinessCode.REGIST_USER_ID_SHORT);
        }
        if (userId.length() > 20) {
            throw new BusinessException(BusinessCode.REGIST_USER_ID_LONG);
        }
        if (!userId.matches("(?i)[a-z0-9_]+")) {
            throw new BusinessException(BusinessCode.REGIST_USER_ID_INVALID);
        }
    }

    static void checkPassword(@Nullable String password) {
        if (StringUtils.isEmpty(password)) {
            throw new BusinessException(BusinessCode.EMPTY_PASSWORD);
        }
        assert password != null;
        if (password.length() > 20) {
            throw new BusinessException(BusinessCode.PASSWORD_TOO_LONG);
        }
        if (password.length() < 6) {
            throw new BusinessException(BusinessCode.PASSWORD_TOO_SHORT);
        }
        for (int i = 0; i < password.length(); ++i) {
            if (password.charAt(i) == ' ') {
                throw new BusinessException(BusinessCode.PASSWORD_HAS_SPACE);
            }
        }
        for (int i = 0; i < password.length(); ++i) {
            char ch = password.charAt(i);
            if (ch >= 127 || ch < 32) {
                throw new BusinessException(BusinessCode.PASSWORD_INVALID_CHARACTER);
            }
        }
    }

    static void checkNick(@Nullable String nick) {
        if (StringUtils.isEmpty(nick)) {
            throw new BusinessException(BusinessCode.NICK_EMPTY);
        }
        assert nick != null;
        if (nick.length() > 64) {
            throw new BusinessException(BusinessCode.NICK_LONG);
        }
    }

    static void checkEmail(@Nullable String email) {
        if (email != null && !email.isEmpty() && !email.matches(EMAIL_PATTERN)) {
            throw new BusinessException(BusinessCode.EMAIL_FORMAT_INCORRECT);
        }
    }

}
