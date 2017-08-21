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
package cn.edu.zjnu.acm.judge.rest;

import cn.edu.zjnu.acm.judge.Application;
import cn.edu.zjnu.acm.judge.domain.Problem;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 *
 * @author zhanhb
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@WithMockUser(roles = "ADMIN")
public class ProblemControllerTest {

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper mapper;
    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = webAppContextSetup(context).build();
    }

    /**
     * Test of save method, of class ProblemController.
     */
    @Test
    public void testSave() throws Exception {
        log.info("save");
        Problem problem = Problem.builder()
                .title("")
                .description("")
                .input("")
                .output("")
                .sampleInput("")
                .sampleOutput("")
                .hint("")
                .source("")
                .build();
        mvc.perform(post("/api/problems")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.writeValueAsBytes(problem))
        )
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    /**
     * Test of delete method, of class ProblemController.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDelete() throws Exception {
        log.info("delete");
        long id = 0L;
        mvc.perform(delete("/api/problems/{id}", id))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    /**
     * Test of resume method, of class ProblemController.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testResume() throws Exception {
        log.info("resume");
        long id = 0L;
        mvc.perform(post("/api/problems/{id}/resume", id))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

}
