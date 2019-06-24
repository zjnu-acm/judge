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
package cn.edu.zjnu.acm.judge.controller;

import cn.edu.zjnu.acm.judge.Application;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.util.AssertionErrors.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

/**
 *
 * @author zhanhb
 */
@AutoConfigureMockMvc
@RunWith(JUnitPlatform.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class KaptchaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test of service method, of class KaptchaController.
     *
     * {@link KaptchaController#service(HttpServletRequest, HttpServletResponse)}
     */
    @Test
    public void testService() throws Exception {
        log.info("doGet");
        MvcResult result = mockMvc.perform(get("/images/rand.jpg"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.IMAGE_JPEG))
                .andReturn();
        HttpSession session = result.getRequest().getSession(false);
        assertThat(session).describedAs("no session").isNotNull();
        MockHttpServletResponse response = result.getResponse();
        byte[] body = response.getContentAsByteArray();
        assertThat(body).describedAs("body").isNotNull().isNotEmpty();
        assertNotEquals("empty body", 0, body.length);
        assertThat(session.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY)).isNotNull();
    }

}
