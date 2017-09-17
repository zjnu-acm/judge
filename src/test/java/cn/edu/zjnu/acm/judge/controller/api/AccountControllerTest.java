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
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 *
 * @author zhanhb
 */
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@WithMockUser(roles = "ADMIN")
public class AccountControllerTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = webAppContextSetup(context).build();
    }

    /**
     * Test of findAllXlsx method, of class AccountController.
     */
    @Test
    public void testFindAllXlsx() throws Exception {
        log.info("findAllXlsx");
        test("/api/accounts.xlsx");
    }

    /**
     * Test of findAllXls method, of class AccountController.
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
        mvc.perform(MockMvcRequestBuilders.fileUpload("/api/accounts.json")
                .file(file))
                .andExpect(status().is2xxSuccessful());
    }

}
