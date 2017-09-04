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
import cn.edu.zjnu.acm.judge.exception.BusinessCode;
import cn.edu.zjnu.acm.judge.exception.BusinessException;
import cn.edu.zjnu.acm.judge.service.ProblemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
@WebAppConfiguration
@WithMockUser(roles = "ADMIN")
public class ProblemControllerTest {

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private ProblemService problemService;
    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = webAppContextSetup(context).build();
    }

    @Test
    public void testFindAll() throws Exception {
        mvc.perform(get("/api/problems.json"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void test() throws Exception {
        Problem problem = Problem.builder()
                .title("")
                .description("")
                .input("")
                .output("")
                .sampleInput("")
                .sampleOutput("")
                .hint("")
                .source("")
                .timeLimit(1000L)
                .memoryLimit(65536 * 1024L)
                .build();
        Long id = mapper.readValue(mvc.perform(post("/api/problems.json")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(problem))
        )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString(), Problem.class).getId();
        assertNotNull(id);
        assertNotNull(problemService.findOne(id));

        assertFalse(problemService.findOne(id).getDisabled());
        mvc.perform(patch("/api/problems/{id}.json", id).content("{\"disabled\":true}").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
        assertTrue(problemService.findOne(id).getDisabled());

        mvc.perform(patch("/api/problems/{id}.json", id).content("{\"disabled\":false}").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
        assertFalse(problemService.findOne(id).getDisabled());

        mvc.perform(delete("/api/problems/{id}.json", id))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        try {
            Problem p = problemService.findOne(id);
            assertNull(p);
        } catch (BusinessException ex) {
            assertEquals(BusinessCode.NOT_FOUND, ex.getCode());
        }
        try {
            mvc.perform(get("/api/problems/{id}.json", id))
                    .andExpect(status().isNotFound());
        } catch (NestedServletException ex) {
            assertTrue(ex.getCause() instanceof BusinessException);
            assertEquals(BusinessCode.NOT_FOUND, ((BusinessException) ex.getCause()).getCode());
        }
        try {
            mvc.perform(delete("/api/problems/{id}.json", id))
                    .andExpect(status().isNotFound());
        } catch (NestedServletException ex) {
            assertTrue(ex.getCause() instanceof BusinessException);
            assertEquals(BusinessCode.NOT_FOUND, ((BusinessException) ex.getCause()).getCode());
        }
        try {
            mvc.perform(patch("/api/problems/{id}.json", id).contentType(MediaType.APPLICATION_JSON).content("{}"))
                    .andExpect(status().isNotFound());
        } catch (NestedServletException ex) {
            assertTrue(ex.getCause() instanceof BusinessException);
            assertEquals(BusinessCode.NOT_FOUND, ((BusinessException) ex.getCause()).getCode());
        }
    }

}
