package cn.edu.zjnu.acm.judge.controller.submission;

import cn.edu.zjnu.acm.judge.Application;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
public class RejudgeControllerTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = webAppContextSetup(context).build();
    }

    /**
     * Test of rejudgeSolution method, of class RejudgeController.
     *
     * @see RejudgeController#rejudgeSolution(long)
     */
    @Test
    public void testRejudgeSolution() throws Exception {
        log.info("rejudgeSolution");
        long solution_id = 0;
        MvcResult result = mvc.perform(get("/admin.rejudge").param("solution_id", Long.toString(solution_id)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    /**
     * Test of rejudgeProblem method, of class RejudgeController.
     *
     * @see RejudgeController#rejudgeProblem(long)
     */
    @Test
    public void testRejudgeProblem() throws Exception {
        log.info("rejudgeProblem");
        long problem_id = 0;
        MvcResult result = mvc.perform(get("/admin.rejudge").param("problem_id", Long.toString(problem_id)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

}
