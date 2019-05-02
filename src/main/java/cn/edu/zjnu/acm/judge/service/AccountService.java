/*
 * Copyright 2019 ZJNU ACM.
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
import cn.edu.zjnu.acm.judge.data.form.AccountImportForm;
import cn.edu.zjnu.acm.judge.domain.User;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author zhanhb
 */
public interface AccountService {

    void delete(String id);

    Page<User> findAll(AccountForm form, Pageable pageable);

    /**
     * find all users not disabled
     */
    Page<User> findAll(Pageable pageable);

    List<Account> findAllForExport(AccountForm form, Pageable pageable);

    @Nonnull
    User findOne(String id);

    void importUsers(AccountImportForm form);

    List<Account> parseExcel(InputStream inputStream, @Nullable Locale locale);

    void save(@Nonnull User user);

    void updatePassword(String userId, String password);

    void updateSelective(String userId, User user);

    String getExcelName(@Nullable Locale locale);

}
