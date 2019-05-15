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
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
@RunWith(JUnitPlatform.class)
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
     * {@link BBSController#bbs(HttpServletRequest, Long, int, long, Model)}
     */
    @Test
    public void testBbs() throws Exception {
        log.info("bbs");
        Long problemId = null;
        int size = 50;
        long top = 99999999;
        MvcResult result = mvc.perform(get("/bbs")
                .param("problem_id", Objects.toString(problemId, ""))
                .param("size", Integer.toString(size))
                .param("top", Long.toString(top)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn();
    }

    private ResultActions testPostpage0(RequestPostProcessor postProcessor) throws Exception {
        log.info("postpage");
        Long problemId = null;
        return mvc.perform(get("/postpage").with(postProcessor)
                .param("problem_id", Objects.toString(problemId, "")));
    }

    /**
     * Test of postpage method, of class BBSController.
     *
     * {@link BBSController#postpage(Model, Long)}
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
     * {@link BBSController#postpage(Model, Long)}
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
     * {@link BBSController#post(Long, Long, String, String, RedirectAttributes, Authentication)}
     */
    @Test
    public void testPost() throws Exception {
        log.info("post");
        Long[] problemIds = {null, mockDataService.problem().getId()};
        Long parentId = null;
        String content = "test";
        String title = "title";
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user = user(createUser());
        for (Long problemId : problemIds) {
            String redirectedUrl = "/bbs" + (problemId != null ? "?problem_id=" + problemId : "");
            MvcResult result = mvc.perform(post("/post").with(user)
                    .param("problem_id", Objects.toString(problemId, ""))
                    .param("parent_id", Objects.toString(parentId, ""))
                    .param("content", content)
                    .param("title", title))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl(redirectedUrl))
                    .andReturn();
        }
        mvc.perform(get("/bbs").with(user))
                .andExpect(status().is2xxSuccessful());
    }

    /**
     * Test of showMessage method, of class BBSController.
     *
     * {@link BBSController#showMessage(long, Model)}
     */
    @Test
    public void testShowMessage() throws Exception {
        log.info("showMessage");
        String userId = createUser();
        long messageId = messageMapper.nextId();
        messageService.save(null, null, userId, "title", "content");
        mvc.perform(get("/showmessage").with(user(userId))
                .param("message_id", Long.toString(messageId)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn();
    }

    private String createUser() {
        return mockDataService.user().getId();
    }

}
