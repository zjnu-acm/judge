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
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.exception.BusinessCode;
import cn.edu.zjnu.acm.judge.exception.BusinessException;
import cn.edu.zjnu.acm.judge.service.MockDataService;
import cn.edu.zjnu.acm.judge.service.ProblemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
@WithMockUser(roles = "ADMIN")
public class ProblemControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProblemService problemService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private MockDataService mockDataService;

    /**
     * Test of list method, of class ProblemController.
     *
     * @see ProblemController#list(ProblemForm, Pageable, Locale)
     */
    @Test
    public void testList() throws Exception {
        mvc.perform(get("/api/problems.json"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    /**
     * @see ProblemController#save(Problem)
     * @see ProblemController#findOne(long, String)
     * @see ProblemController#update(long, Problem, String)
     * @see ProblemController#delete(long)
     */
    @Test
    public void test() throws Exception {
        Problem problem = mockDataService.problem(false);
        Long id = objectMapper.readValue(mvc.perform(post("/api/problems.json")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(problem))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString(), Problem.class).getId();
        assertNotNull(id);

        assertFalse(findOne(id).getDisabled());
        mvc.perform(patch("/api/problems/{id}.json", id).content("{\"disabled\":true}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        assertTrue(findOne(id).getDisabled());

        mvc.perform(patch("/api/problems/{id}.json", id).content("{\"disabled\":false}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        assertFalse(findOne(id).getDisabled());

        mvc.perform(delete("/api/problems/{id}.json", id))
                .andExpect(status().isNoContent());

        try {
            Problem p = problemService.findOne(id);
            fail("should throw a BusinessException");
        } catch (BusinessException ex) {
            assertEquals(BusinessCode.PROBLEM_NOT_FOUND, ex.getCode());
        }
        mvc.perform(get("/api/problems/{id}.json", id))
                .andExpect(status().isNotFound());
        mvc.perform(delete("/api/problems/{id}.json", id))
                .andExpect(status().isNotFound());
        mvc.perform(patch("/api/problems/{id}.json", id)
                .content("{}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private Problem findOne(long id) throws Exception {
        return objectMapper.readValue(mvc.perform(get("/api/problems/{id}.json", id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString(), Problem.class);
    }

}
