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
import cn.edu.zjnu.acm.judge.data.form.AccountForm;
import cn.edu.zjnu.acm.judge.data.form.AccountImportForm;
import cn.edu.zjnu.acm.judge.data.form.UserPasswordForm;
import cn.edu.zjnu.acm.judge.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Locale;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 *
 * @author zhanhb
 */
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
@WithMockUser(roles = "ADMIN")
public class AccountControllerTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        mvc = webAppContextSetup(context).build();
    }

    /**
     * Test of findAll method, of class AccountController.
     * {@link AccountController#findAll(AccountForm, Pageable)}
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
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    /**
     * Test of update method, of class AccountController.
     * {@link AccountController#update(String, User)}
     */
    @Test
    public void testUpdate() throws Exception {
        log.info("update");
        String userId = "";
        User request = null;
        MvcResult result = mvc.perform(patch("/api/accounts/{userId}", userId)
                .content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    /**
     * Test of updatePassword method, of class AccountController.
     * {@link AccountController#updatePassword(String, UserPasswordForm)}
     */
    @Test
    public void testUpdatePassword() throws Exception {
        log.info("updatePassword");
        String userId = "";
        UserPasswordForm request = null;
        MvcResult result = mvc.perform(patch("/api/accounts/{userId}/password", userId)
                .content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    /**
     * Test of findAllXlsx method, of class AccountController.
     * {@link AccountController#findAllXlsx(AccountForm, Pageable, Locale)}
     * {@link AccountController#parseExcel(MultipartFile, Locale)}
     */
    @Test
    public void testFindAllXlsx() throws Exception {
        log.info("findAllXlsx");
        test("/api/accounts.xlsx");
    }

    /**
     * Test of findAllXls method, of class AccountController.
     * {@link AccountController#findAllXls(AccountForm, Pageable, Locale)}
     * {@link AccountController#parseExcel(MultipartFile, Locale)}
     */
    @Test
    public void testFindAllXls() throws Exception {
        log.info("findAllXls");
        test("/api/accounts.xls");
    }

    private void test(String url) throws Exception {
        byte[] content = mvc.perform(get(url))
                .andExpect(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsByteArray();
        MockMultipartFile file = new MockMultipartFile("file", content);
        mvc.perform(fileUpload("/api/accounts.json").file(file))
                .andExpect(status().is2xxSuccessful());
    }

    /**
     * Test of importUsers method, of class AccountController.
     * {@link AccountController#importUsers(AccountImportForm)}
     */
    @Test
    public void testImportUsers() throws Exception {
        log.info("importUsers");
        AccountImportForm request = null;
        MvcResult result = mvc.perform(post("/api/accounts/import")
                .content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

}
