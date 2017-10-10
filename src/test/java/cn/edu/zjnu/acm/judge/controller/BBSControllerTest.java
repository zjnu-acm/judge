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
package cn.edu.zjnu.acm.judge.controller;

import cn.edu.zjnu.acm.judge.Application;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
public class BBSControllerTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = webAppContextSetup(context).build();
    }

    /**
     * Test of bbs method, of class BBSController.
     * {@link BBSController#bbs(Long, int, long)}
     */
    @Test
    public void testBbs() throws Exception {
        log.info("bbs");
        Long problem_id = null;
        int size = 50;
        long top = 99999999;
        MvcResult result = mvc.perform(get("/bbs")
                .param("problem_id", Objects.toString(problem_id, ""))
                .param("size", Integer.toString(size))
                .param("top", Long.toString(top)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    /**
     * Test of postpage method, of class BBSController.
     * {@link BBSController#postpage(Model, Long)}
     */
    @Test
    public void testPostpage() throws Exception {
        log.info("postpage");
        Long problem_id = null;
        MvcResult result = mvc.perform(get("/postpage").param("problem_id", Objects.toString(problem_id, "")))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    /**
     * Test of post method, of class BBSController.
     * {@link BBSController#post(Long, Long, String, String, RedirectAttributes, Authentication)}
     */
    @Test
    public void testPost() throws Exception {
        log.info("post");
        Long problem_id = null;
        Long parent_id = null;
        String content = "";
        String title = "";
        MvcResult result = mvc.perform(post("/post")
                .param("problem_id", Objects.toString(problem_id, ""))
                .param("parent_id", Objects.toString(parent_id, ""))
                .param("content", content)
                .param("title", title))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    /**
     * Test of showMessage method, of class BBSController.
     * {@link BBSController#showMessage(long)}
     */
    @Test
    public void testShowMessage() throws Exception {
        log.info("showMessage");
        long message_id = 0;
        MvcResult result = mvc.perform(get("/showmessage").param("message_id", Long.toString(message_id)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

}
