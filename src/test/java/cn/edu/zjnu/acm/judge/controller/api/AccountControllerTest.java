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
package cn.edu.zjnu.acm.judge.controller.api;

import cn.edu.zjnu.acm.judge.Application;
import cn.edu.zjnu.acm.judge.data.excel.Account;
import cn.edu.zjnu.acm.judge.data.form.AccountImportForm;
import cn.edu.zjnu.acm.judge.data.form.UserPasswordForm;
import cn.edu.zjnu.acm.judge.domain.User;
import cn.edu.zjnu.acm.judge.service.AccountService;
import cn.edu.zjnu.acm.judge.service.MockDataService;
import cn.edu.zjnu.acm.judge.util.Utility;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * @author zhanhb
 */
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
@WithMockUser(roles = "ADMIN")
public class AccountControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AccountService accountService;
    @Autowired
    private MockDataService mockDataService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Test of findAll method, of class AccountController.
     *
     * @see AccountController#findAll(AccountForm, Pageable)
     */
    @Test
    public void testFindAll() throws Exception {
        log.info("findAll");
        String userId = "";
        String nick = "";
        String query = "";
        Boolean disabled = null;
        MvcResult result = mvc.perform(get("/api/accounts.json")
                .param("userId", userId)
                .param("nick", nick)
                .param("query", query)
                .param("disabled", Objects.toString(disabled, "")))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    /**
     * Test of update method, of class AccountController.
     *
     * @see AccountController#update(String, User)
     */
    @Test
    public void testUpdate() throws Exception {
        log.info("update");
        User user = mockDataService.user();
        String userId = user.getId();
        assertEquals(user.getSchool(), accountService.findOne(userId).getSchool());
        user = user.toBuilder().school("test school").password("empty").build();
        mvc.perform(patch("/api/accounts/{userId}.json", userId)
                .content(objectMapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
        assertEquals("test school", accountService.findOne(userId).getSchool());
        // password not changed, for annotation JsonIgnore is present on field password
        assertTrue("password should not be changed", passwordEncoder.matches(userId, accountService.findOne(userId).getPassword()));
    }

    /**
     * Test of updatePassword method, of class AccountController.
     *
     * @see AccountController#updatePassword(String, UserPasswordForm)
     */
    @Test
    public void testUpdatePassword() throws Exception {
        log.info("updatePassword");
        String userId = mockDataService.user().getId();
        UserPasswordForm form = new UserPasswordForm();
        String newPassword = Utility.getRandomString(16);
        form.setPassword(newPassword);

        MvcResult result = mvc.perform(patch("/api/accounts/{userId}/password.json", userId)
                .content(objectMapper.writeValueAsString(form)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
        assertTrue(passwordEncoder.matches(newPassword, accountService.findOne(userId).getPassword()));
    }

    /**
     * Test of findAllXlsx method, of class AccountController.
     *
     * @see AccountController#findAllXlsx(AccountForm, Pageable, Locale)
     * @see AccountController#parseExcel(MultipartFile, Locale)
     */
    @Test
    public void testFindAllXlsx() throws Exception {
        log.info("findAllXlsx");
        test("/api/accounts.xlsx");
    }

    /**
     * Test of findAllXls method, of class AccountController.
     *
     * @see AccountController#findAllXls(AccountForm, Pageable, Locale)
     * @see AccountController#parseExcel(MultipartFile, Locale)
     */
    @Test
    public void testFindAllXls() throws Exception {
        log.info("findAllXls");
        test("/api/accounts.xls");
    }

    private void test(String url) throws Exception {
        byte[] content = mvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();
        MockMultipartFile file = new MockMultipartFile("file", content);
        mvc.perform(multipart("/api/accounts.json").file(file))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    /**
     * Test of importUsers method, of class AccountController.
     *
     * @see AccountController#importUsers(AccountImportForm)
     */
    @Test
    public void testImportUsers() throws Exception {
        log.info("importUsers");
        AccountImportForm form = new AccountImportForm();
        Account account = toAccount(mockDataService.user(false));
        Account account2 = toAccount(mockDataService.user(false));
        form.setContent(Arrays.asList(account, account2));
        expect(form, HttpStatus.OK);
        account.setExists(true);
        account2.setExists(true);
        expect(form, HttpStatus.BAD_REQUEST);
        form.setExistsPolicy(Arrays.asList(AccountImportForm.ExistPolicy.ENABLE));
        expect(form, HttpStatus.OK);
    }

    private MvcResult expect(AccountImportForm form, HttpStatus status) throws Exception {
        return mvc.perform(post("/api/accounts/import.json")
                .content(objectMapper.writeValueAsString(form)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(status.value()))
                .andReturn();
    }

    private Account toAccount(User user) {
        Account account = new Account();
        account.setId(user.getId());
        account.setPassword(user.getPassword());
        account.setEmail(user.getEmail());
        account.setSchool(user.getSchool());
        return account;
    }

    /**
     * Test of passwordStatus method, of class AccountController.
     *
     * @see AccountController#passwordStatus()
     */
    @Test
    public void testPasswordStatus() throws Exception {
        log.info("passwordStatus");
        MvcResult result = mvc.perform(get("/api/accounts/password/status.json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isMap())
                .andExpect(jsonPath("$.stats").isMap())
                .andReturn();
    }

}
