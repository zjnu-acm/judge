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

import cn.edu.zjnu.acm.judge.config.PasswordConfiguration;
import cn.edu.zjnu.acm.judge.data.excel.Account;
import cn.edu.zjnu.acm.judge.data.form.AccountForm;
import cn.edu.zjnu.acm.judge.data.form.AccountImportForm;
import cn.edu.zjnu.acm.judge.domain.User;
import cn.edu.zjnu.acm.judge.exception.BusinessCode;
import cn.edu.zjnu.acm.judge.exception.BusinessException;
import cn.edu.zjnu.acm.judge.mapper.UserMapper;
import cn.edu.zjnu.acm.judge.mapper.UserProblemMapper;
import cn.edu.zjnu.acm.judge.mapper.UserRoleMapper;
import cn.edu.zjnu.acm.judge.util.EnumUtils;
import cn.edu.zjnu.acm.judge.util.ValueCheck;
import cn.edu.zjnu.acm.judge.util.excel.ExcelUtil;
import com.google.common.annotations.VisibleForTesting;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
    @Autowired
    private UserProblemMapper userProblemMapper;

    public Page<User> findAll(AccountForm form, Pageable pageable) {
        List<User> list = userMapper.findAll(form, pageable);
        long count = userMapper.count(form);
        return new PageImpl<>(list, pageable, count);
    }

    public List<Account> findAllByExport(AccountForm form, Pageable pageable) {
        return userMapper.findAllByExport(form, PageRequest.of(0, Integer.MAX_VALUE, pageable.getSort()));
    }

    public List<Account> parseExcel(InputStream inputStream, @Nullable Locale locale) throws IOException {
        List<Account> accounts = ExcelUtil.parse(inputStream, Account.class, locale).stream()
                .filter(account -> StringUtils.hasText(account.getId()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(accounts)) {
            return accounts;
        }
        Map<String, List<Account>> groupBy = accounts.stream().collect(Collectors.groupingBy(Account::getId, () -> new TreeMap<>(String.CASE_INSENSITIVE_ORDER), Collectors.toList()));
        List<String> exists = userMapper.findAllByUserIds(groupBy.keySet());
        for (String exist : exists) {
            for (Account account : groupBy.get(exist)) {
                account.setExists(true);
            }
        }
        return accounts;
    }

    public Page<User> findAll(Pageable pageable) {
        AccountForm form = AccountForm.builder().disabled(false).build();
        return findAll(form, pageable);
    }

    public void updateSelective(String userId, User user) {
        if (userMapper.updateSelective(userId, user.toBuilder().modifiedTime(Instant.now())
                .password(Optional.ofNullable(user.getPassword()).map(passwordEncoder::encode).orElse(null))
                .build()) == 0) {
            throw new BusinessException(BusinessCode.USER_NOT_FOUND, userId);
        }
    }

    public void updatePassword(String userId, String password) {
        if (userRoleMapper.countAdmin(userId) != 0) {
            throw new BusinessException(BusinessCode.RESET_PASSWORD_FORBIDDEN);
        }
        if (0 == userMapper.updateSelective(userId,
                User.builder().password(passwordEncoder.encode(password)).build())) {
            throw new BusinessException(BusinessCode.USER_NOT_FOUND, userId);
        }
    }

    @Transactional
    public void importUsers(AccountImportForm form) {
        List<Account> accounts = form.getContent();
        if (CollectionUtils.isEmpty(accounts)) {
            throw new BusinessException(BusinessCode.IMPORT_USER_EMPTY);
        }
        List<Account> exists = accounts.stream()
                .filter(account -> Boolean.TRUE.equals(account.getExists()))
                .collect(Collectors.toList());
        if (!exists.isEmpty()) {
            int mask = EnumUtils.toMask(form.getExistsPolicy());
            if (mask == 0) {
                throw new BusinessException(BusinessCode.IMPORT_USER_EXISTS);
            }
            boolean hasAdmin = userRoleMapper.countAdmin(exists.stream().map(Account::getId).toArray(String[]::new)) != 0;
            if (hasAdmin && (mask & (1 << AccountImportForm.ExistPolicy.RESET_PASSWORD.ordinal())) != 0) {
                throw new BusinessException(BusinessCode.IMPORT_USER_RESET_PASSWORD_FORBIDDEN);
            }
            long current = userMapper.countAllByUserIds(exists.stream().map(Account::getId).collect(Collectors.toList()));
            if (current != exists.size()) {
                throw new BusinessException(BusinessCode.IMPORT_USER_EXISTS_CHANGE);
            }
            prepare(exists);
            userMapper.batchUpdate(exists, mask);
        }
        List<Account> notExists = accounts.stream()
                .filter(account -> !Boolean.TRUE.equals(account.getExists()))
                .collect(Collectors.toList());
        if (!notExists.isEmpty()) {
            long current = userMapper.countAllByUserIds(notExists.stream().map(Account::getId).collect(Collectors.toList()));
            if (current != 0) {
                throw new BusinessException(BusinessCode.IMPORT_USER_EXISTS_CHANGE);
            }
            prepare(notExists);
            userMapper.insert(notExists);
        }
    }

    private void prepare(Collection<Account> accounts) {
        for (Account account : accounts) {
            if (!StringUtils.hasText(account.getId())) {
                throw new BusinessException(BusinessCode.EMPTY_USER_ID);
            }
            String password = account.getPassword();
            if (StringUtils.isEmpty(password)) {
                throw new BusinessException(BusinessCode.EMPTY_PASSWORD);
            }
            if (password.length() <= PasswordConfiguration.MAX_PASSWORD_LENGTH) {
                ValueCheck.checkPassword(password);
                account.setPassword(passwordEncoder.encode(password));
            }
            if (!StringUtils.hasText(account.getEmail())) {
                account.setEmail(null);
            }
            if (!StringUtils.hasText(account.getNick())) {
                account.setNick(account.getId());
            }
            if (account.getSchool() == null) {
                account.setSchool("");
            }
        }
    }

    public void save(@Nonnull User user) {
        userMapper.save(user);
    }

    @Nonnull
    public User findOne(String id) {
        User user = userMapper.findOne(id);
        if (user == null) {
            throw new BusinessException(BusinessCode.USER_NOT_FOUND, id);
        }
        return user;
    }

    @Transactional
    @VisibleForTesting
    public void delete(String id) {
        long result = userProblemMapper.deleteByUser(id) + userMapper.delete(id);
        if (result == 0) {
            throw new BusinessException(BusinessCode.USER_NOT_FOUND, id);
        }
    }

}
