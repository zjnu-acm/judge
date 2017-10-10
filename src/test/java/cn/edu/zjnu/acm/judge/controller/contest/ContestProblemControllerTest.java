package cn.edu.zjnu.acm.judge.controller.contest;

import cn.edu.zjnu.acm.judge.Application;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
public class ContestProblemControllerTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = webAppContextSetup(context).build();
    }

    /**
     * Test of problems method, of class ContestProblemController.
     * {@link ContestProblemController#problems(long, RedirectAttributes)}
     */
    @Test
    public void testProblems() throws Exception {
        log.info("problems");
        long contestId = 0;
        MvcResult result = mvc.perform(get("/contests/{contestId}/problems", contestId))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    /**
     * Test of showProblem method, of class ContestProblemController.
     * {@link ContestProblemController#showProblem(long, long, Model, Locale)}
     */
    @Test
    public void testShowProblem() throws Exception {
        log.info("showProblem");
        long contestId = 0;
        long pid = 0;
        Locale locale = Locale.getDefault();
        MvcResult result = mvc.perform(get("/contests/{contestId}/problems/{pid}", contestId, pid)
                .locale(locale))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    /**
     * Test of status method, of class ContestProblemController.
     * {@link ContestProblemController#status(long, int, Pageable, Model, Authentication, HttpServletRequest)}
     */
    @Test
    public void testStatus() throws Exception {
        log.info("status");
        long contestId = 0;
        int pid = 0;
        MvcResult result = mvc.perform(get("/contests/{contestId}/problems/{pid}/status", contestId, pid))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

}
