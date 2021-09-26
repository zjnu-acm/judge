/*
 * Copyright 2017-2019 ZJNU ACM.
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
package cn.edu.zjnu.acm.judge.service.impl;

import cn.edu.zjnu.acm.judge.config.security.PasswordConfiguration;
import cn.edu.zjnu.acm.judge.data.excel.Account;
import cn.edu.zjnu.acm.judge.data.form.AccountForm;
import cn.edu.zjnu.acm.judge.data.form.AccountImportForm;
import cn.edu.zjnu.acm.judge.domain.User;
import cn.edu.zjnu.acm.judge.domain.UsernameChangeLog;
import cn.edu.zjnu.acm.judge.exception.BusinessCode;
import cn.edu.zjnu.acm.judge.exception.BusinessException;
import cn.edu.zjnu.acm.judge.mapper.UserMapper;
import cn.edu.zjnu.acm.judge.mapper.UserProblemMapper;
import cn.edu.zjnu.acm.judge.mapper.UserRoleMapper;
import cn.edu.zjnu.acm.judge.mapper.UsernameChangeLogMapper;
import cn.edu.zjnu.acm.judge.service.AccountService;
import cn.edu.zjnu.acm.judge.util.EnumUtils;
import cn.edu.zjnu.acm.judge.util.ValueCheck;
import cn.edu.zjnu.acm.judge.util.excel.ExcelUtil;
import com.google.common.annotations.VisibleForTesting;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
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
@RequiredArgsConstructor
@Service("accountService")
public class AccountServiceImpl implements AccountService {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ISO_DATE;

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleMapper userRoleMapper;
    private final UserProblemMapper userProblemMapper;
    private final MessageSource messageSource;
    private final UsernameChangeLogMapper usernameChangeLogMapper;

    @Override
    public Page<User> findAll(AccountForm form, Pageable pageable) {
        List<User> list = userMapper.findAll(form, pageable);
        long count = userMapper.count(form);
        return new PageImpl<>(list, pageable, count);
    }

    @Override
    public List<Account> findAllForExport(AccountForm form, Pageable pageable) {
        return userMapper.findAllForExport(form, PageRequest.of(0, Integer.MAX_VALUE, pageable.getSort()));
    }

    @Override
    public List<Account> parseExcel(InputStream inputStream, @Nonnull Locale locale) {
        Objects.requireNonNull(locale, "locale");
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

    @Override
    public Page<User> findAll(Pageable pageable) {
        AccountForm form = AccountForm.builder().disabled(false).build();
        return findAll(form, pageable);
    }

    @Override
    public void updateSelective(String userId, User user) {
        String newId = user.getId();
        boolean changeUserId = newId != null && !userId.equals(newId);
        if (changeUserId) {
            ValueCheck.checkUserId(newId);
            if (userMapper.findOne(newId) != null) {
                throw new BusinessException(BusinessCode.REGISTER_PROMPT_USER_ID_EXISTS, newId);
            }
        }
        if (userMapper.updateSelective(userId, user.toBuilder()
                .createdTime(null)
                .modifiedTime(Instant.now())
                .password(Optional.ofNullable(user.getPassword()).map(passwordEncoder::encode).orElse(null))
                .email(Optional.ofNullable(user.getEmail()).filter(Objects::nonNull).orElse(null))
                .build()) == 0) {
            throw new BusinessException(BusinessCode.USER_NOT_FOUND, userId);
        }
        if (changeUserId) {
            usernameChangeLogMapper.save(UsernameChangeLog.builder().old(userId).newName(newId).build());
        }
    }

    @Override
    public void updatePassword(String userId, String password) {
        if (userRoleMapper.countAdmin(userId) != 0) {
            throw new BusinessException(BusinessCode.RESET_PASSWORD_FORBIDDEN);
        }
        if (0 == userMapper.updateSelective(userId,
                User.builder().password(passwordEncoder.encode(password)).build())) {
            throw new BusinessException(BusinessCode.USER_NOT_FOUND, userId);
        }
    }

    @Override
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
            Set<AccountImportForm.ExistPolicy> set = form.getExistsPolicy();
            if (set.isEmpty()) {
                throw new BusinessException(BusinessCode.IMPORT_USER_EXISTS);
            }
            boolean hasAdmin = userRoleMapper.countAdmin(exists.stream().map(Account::getId).toArray(String[]::new)) != 0;
            if (hasAdmin && set.contains(AccountImportForm.ExistPolicy.RESET_PASSWORD)) {
                throw new BusinessException(BusinessCode.IMPORT_USER_RESET_PASSWORD_FORBIDDEN);
            }
            long current = userMapper.countAllByUserIds(exists.stream().map(Account::getId).collect(Collectors.toList()));
            if (current != exists.size()) {
                throw new BusinessException(BusinessCode.IMPORT_USER_EXISTS_CHANGE);
            }
            prepare(exists);
            userMapper.batchUpdate(exists, EnumUtils.toMask(set));
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
                throw new BusinessException(BusinessCode.IMPORT_USER_ID_EMPTY);
            }
            String password = account.getPassword();
            if (!StringUtils.hasLength(password)) {
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

    @Override
    public void save(@Nonnull User user) {
        userMapper.save(user);
    }

    @Nonnull
    @Override
    public User findOne(String id) {
        User user = userMapper.findOne(id);
        if (user == null) {
            throw new BusinessException(BusinessCode.USER_NOT_FOUND, id);
        }
        return user;
    }

    @Override
    @Transactional
    @VisibleForTesting
    public void delete(String id) {
        long result = userProblemMapper.deleteByUser(id) + userMapper.delete(id);
        if (result == 0) {
            throw new BusinessException(BusinessCode.USER_NOT_FOUND, id);
        }
    }

    @Override
    public String getExcelName(@Nonnull Locale locale) {
        String name = messageSource.getMessage("onlinejudge.export.excel.name", new Object[0], locale);
        return name + " - " + dtf.format(LocalDateTime.now());
    }

}
