package cn.edu.zjnu.acm.judge.controller.problem;

import cn.edu.zjnu.acm.judge.Application;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.service.ProblemService;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(JUnitPlatform.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
public class ProblemStatusControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ProblemService problemService;

    /**
     * Test of gotoProblem method, of class ProblemStatusController.
     *
     * {@link ProblemStatusController#gotoProblem(String, RedirectAttributes)}
     */
    @Test
    public void testGotoProblem() throws Exception {
        log.info("gotoProblem");
        String pid = "test";
        mvc.perform(get("/gotoproblem").param("pid", pid))
                .andExpect(redirectedUrl("/searchproblem?sstr=" + pid))
                .andReturn();
        pid = "1000";
        mvc.perform(get("/gotoproblem").param("pid", pid))
                .andExpect(redirectedUrl("/showproblem?problem_id=" + pid))
                .andReturn();
    }

    /**
     * Test of status method, of class ProblemStatusController.
     *
     * {@link ProblemStatusController#status(HttpServletRequest, long, Integer, Pageable)}
     */
    @Test
    public void testStatus() throws Exception {
        log.info("status");
        Problem problem = Problem.builder().contests(new long[]{0}).timeLimit(1000L).memoryLimit(65536L).build();
        problemService.save(problem);
        long problemId = problem.getId();
        MvcResult result = mvc.perform(get("/problemstatus").param("problem_id", Long.toString(problemId)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn();
    }

}
