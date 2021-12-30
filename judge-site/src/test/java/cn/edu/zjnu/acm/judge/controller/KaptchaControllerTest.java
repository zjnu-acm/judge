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
import java.io.ByteArrayInputStream;
import javax.imageio.ImageIO;
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
import org.springframework.test.web.servlet.RequestBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.util.AssertionErrors.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    private void testRequest(RequestBuilder builder) throws Exception {
        MvcResult result = mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.IMAGE_JPEG))
                .andReturn();
        HttpSession session = result.getRequest().getSession(false);
        assertThat(session).describedAs("no session").isNotNull();
        MockHttpServletResponse response = result.getResponse();
        byte[] body = response.getContentAsByteArray();
        assertThat(body).describedAs("body").isNotNull().isNotEmpty();
        assertNotEquals("empty body", 0, body.length);
        ImageIO.read(new ByteArrayInputStream(body));
        assertThat((String) session.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY)).isNotEmpty();
    }

    /**
     * Test of doGet method, of class KaptchaController.
     *
     * {@link KaptchaController#doGet(HttpServletRequest, HttpServletResponse)}
     */
    @Test
    public void testDoGet() throws Exception {
        log.info("doGet");
        testRequest(get("/images/rand?_format=jpg"));
        testRequest(get("/images/rand").accept(MediaType.valueOf("image/*")));
        testRequest(get("/images/rand").accept(MediaType.IMAGE_JPEG));
    }

    /**
     * Test of doGetJpg method, of class KaptchaController.
     *
     * {@link KaptchaController#doGetJpg(HttpServletRequest, HttpServletResponse)}
     */
    @Test
    public void testDoGetJpg() throws Exception {
        log.info("doGetJpg");
        testRequest(get("/images/rand.jpg"));
    }

}
