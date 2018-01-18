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
package cn.edu.zjnu.acm.judge.config;

import cn.edu.zjnu.acm.judge.Application;
import cn.edu.zjnu.acm.judge.domain.User;
import cn.edu.zjnu.acm.judge.service.MockDataService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
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
public class SecurityConfigurationTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private MockDataService mockDataService;

    /**
     * Test of login method, of class SecurityConfiguration.
     *
     * @see SecurityConfiguration#configure(HttpSecurity)
     */
    @Test
    public void testLogin() throws Exception {
        log.info("login");
        String url = "/test";
        User user = mockDataService.user();
        MvcResult result = mvc.perform(post("/login")
                .param("user_id1", user.getId())
                .param("password1", user.getPassword())
                .param("url", url))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(url))
                .andReturn();
    }

    @Test
    public void testLoginError() throws Exception {
        log.info("login");
        String url = "/test?k=1&v=2";
        String redirectedUrl = "/login?error&url=%2Ftest%3Fk%3D1%26v%3D2";
        User user = mockDataService.user();
        MvcResult result = mvc.perform(post("/login")
                .param("user_id1", user.getId())
                .param("password1", user.getPassword() + 1)
                .param("url", url))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(redirectedUrl))
                .andReturn();
    }

    @Test
    public void testLoginErrorWithoutRedirectParam() throws Exception {
        log.info("login");
        String redirectedUrl = "/login?error";
        User user = mockDataService.user();
        mvc.perform(post("/login")
                .param("user_id1", user.getId())
                .param("password1", user.getPassword() + 1))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(redirectedUrl))
                .andReturn();
        mvc.perform(post("/login")
                .param("user_id1", user.getId())
                .param("password1", user.getPassword() + 1)
                .param("url", ""))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(redirectedUrl))
                .andReturn();
    }

}
