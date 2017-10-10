package cn.edu.zjnu.acm.judge.controller.problem;

import cn.edu.zjnu.acm.judge.Application;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.service.ProblemService;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
public class ProblemStatusControllerTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;
    @Autowired
    private ProblemService problemService;

    @Before
    public void setUp() {
        mvc = webAppContextSetup(context).build();
    }

    /**
     * Test of gotoProblem method, of class ProblemStatusController.
     *
     * @see ProblemStatusController#gotoProblem(String, RedirectAttributes)
     */
    @Test
    public void testGotoProblem() throws Exception {
        log.info("gotoProblem");
        String pid = "test";
        mvc.perform(get("/gotoproblem").param("pid", pid))
                .andDo(print())
                .andExpect(redirectedUrl("/searchproblem?sstr=" + pid))
                .andReturn();
        pid = "1000";
        mvc.perform(get("/gotoproblem").param("pid", pid))
                .andDo(print())
                .andExpect(redirectedUrl("/showproblem?problem_id=" + pid))
                .andReturn();
    }

    /**
     * Test of status method, of class ProblemStatusController.
     *
     * @see ProblemStatusController#status(HttpServletRequest, long, Pageable,
     * Authentication)
     */
    @Test
    public void testStatus() throws Exception {
        log.info("status");
        Problem problem = Problem.builder().contests(new long[]{0}).timeLimit(1000L).memoryLimit(65536L).build();
        problemService.save(problem);
        long problem_id = problem.getId();
        MvcResult result = mvc.perform(get("/problemstatus").param("problem_id", Long.toString(problem_id)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn();
    }

}
