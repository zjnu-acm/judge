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
import cn.edu.zjnu.acm.judge.mapper.MessageMapper;
import cn.edu.zjnu.acm.judge.service.MessageService;
import cn.edu.zjnu.acm.judge.service.MockDataService;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
public class BBSControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private MessageService messageService;
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private MockDataService mockDataService;

    /**
     * Test of bbs method, of class BBSController.
     *
     * @see BBSController#bbs(Long, int, long)
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
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn();
    }

    private ResultActions testPostpage0(RequestPostProcessor postProcessor) throws Exception {
        log.info("postpage");
        Long problem_id = null;
        return mvc.perform(get("/postpage").with(postProcessor)
                .param("problem_id", Objects.toString(problem_id, "")));
    }

    /**
     * Test of postpage method, of class BBSController.
     *
     * @see BBSController#postpage(Model, Long)
     */
    @Test
    public void testPostpage() throws Exception {
        String userId = createUser();
        testPostpage0(user(userId))
                .andExpect(status().isOk())
                .andExpect(view().name("bbs/postpage"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn();
    }

    /**
     * Test of postpage method, of class BBSController.
     *
     * @see BBSController#postpage(Model, Long)
     */
    @Test
    public void testPostpageAnonymous() throws Exception {
        testPostpage0(anonymous())
                .andExpect(forwardedUrl("/unauthorized"))
                .andReturn();
    }

    /**
     * Test of post method, of class BBSController.
     *
     * @see BBSController#post(Long, Long, String, String, RedirectAttributes,
     * Authentication)
     */
    @Test
    public void testPost() throws Exception {
        log.info("post");
        Long problem_id = null;
        Long parent_id = null;
        String content = "test";
        String title = "title";
        MvcResult result = mvc.perform(post("/post").with(user(createUser()))
                .param("problem_id", Objects.toString(problem_id, ""))
                .param("parent_id", Objects.toString(parent_id, ""))
                .param("content", content)
                .param("title", title))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/bbs"))
                .andReturn();
    }

    /**
     * Test of showMessage method, of class BBSController.
     *
     * @see BBSController#showMessage(long)
     */
    @Test
    public void testShowMessage() throws Exception {
        log.info("showMessage");
        String userId = createUser();
        long message_id = messageMapper.nextId();
        messageService.save(null, null, userId, "title", "content");
        mvc.perform(get("/showmessage").with(user(userId))
                .param("message_id", Long.toString(message_id)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn();
    }

    private String createUser() {
        return mockDataService.user().getId();
    }

}
