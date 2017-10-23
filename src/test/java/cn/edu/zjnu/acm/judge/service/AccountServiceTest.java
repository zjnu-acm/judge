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

import cn.edu.zjnu.acm.judge.Application;
import cn.edu.zjnu.acm.judge.data.form.AccountForm;
import cn.edu.zjnu.acm.judge.util.Pageables;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author zhanhb
 */
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
public class AccountServiceTest {

    public static void delete(AccountService accountService, String userId) {
        accountService.delete(userId);
    }

    @Autowired
    private AccountService accountService;

    /**
     * Test of findAll method, of class AccountService.
     */
    @Test
    public void testFindAll() {
        log.info("findAll");
        for (Pageable pageable : Pageables.users()) {
            accountService.findAll(pageable);
            for (AccountForm accountForm : buildForms()) {
                accountService.findAll(accountForm, pageable);
                accountService.findAll(accountForm.toBuilder().disabled(true).build(), pageable);
                accountService.findAll(accountForm.toBuilder().disabled(false).build(), pageable);
            }
        }
    }

    private AccountForm[] buildForms() {
        String test = "test";
        return new AccountForm[]{
            AccountForm.builder().userId(test).build(),
            AccountForm.builder().nick(test).build(),
            AccountForm.builder().build(),
            AccountForm.builder().nick(test).userId(test).build(),
            AccountForm.builder().query("%").build(),
            AccountForm.builder().query(test).nick(test).build()
        };
    }
}
