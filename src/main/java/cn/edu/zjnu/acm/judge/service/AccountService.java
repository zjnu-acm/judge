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
package cn.edu.zjnu.acm.judge.service;

import cn.edu.zjnu.acm.judge.data.excel.Account;
import cn.edu.zjnu.acm.judge.data.form.AccountForm;
import cn.edu.zjnu.acm.judge.domain.User;
import cn.edu.zjnu.acm.judge.exception.BusinessCode;
import cn.edu.zjnu.acm.judge.exception.BusinessException;
import cn.edu.zjnu.acm.judge.mapper.UserMapper;
import cn.edu.zjnu.acm.judge.mapper.UserRoleMapper;
import java.time.Instant;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author zhanhb
 */
@Service
public class AccountService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRoleMapper userRoleMapper;

    public Page<User> findAll(AccountForm form, Pageable pageable) {
        List<User> list = userMapper.findAll(form, pageable);
        long count = userMapper.count(form);
        return new PageImpl<>(list, pageable, count);
    }

    public List<Account> findAllByExport(AccountForm form, Pageable pageable) {
        return userMapper.findAllByExport(form, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort()));
    }

    public Page<User> findAll(Pageable pageable) {
        AccountForm form = AccountForm.builder().disabled(false).build();
        return findAll(form, pageable);
    }

    public void update(String userId, User user) {
        String password = user.getPassword();
        if (password != null) {
            List<String> permissions = userRoleMapper.findAllByUserId(userId);
            for (String permission : permissions) {
                if ("administrator".equalsIgnoreCase(permission)) {
                    throw new BusinessException(BusinessCode.RESET_PASSWORD_FORBIDDEN);
                }
            }
            User build = user.toBuilder().password(passwordEncoder.encode(password)).modifiedTime(Instant.now()).build();
            userMapper.updateSelective(userId, build);
        } else {
            userMapper.updateSelective(userId, user.toBuilder().modifiedTime(Instant.now()).build());
        }
    }

}
