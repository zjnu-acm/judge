package cn.edu.zjnu.acm.judge.controller.contest;

import cn.edu.zjnu.acm.judge.Application;
import cn.edu.zjnu.acm.judge.service.MockDataService;
import java.util.Locale;
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
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
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
    @Autowired
    private MockDataService mockDataService;

    @Before
    public void setUp() {
        mvc = webAppContextSetup(context).build();
    }

    /**
     * Test of problems method, of class ContestProblemController.
     *
     * @see ContestProblemController#problems(Model, Locale, long, Authentication)
     */
    @Test
    public void testProblems() throws Exception {
        log.info("problems");
        long contestId = mockDataService.contest().getId();
        Locale locale = Locale.getDefault();
        MvcResult result = mvc.perform(get("/contests/{contestId}/problems", contestId)
                .locale(locale))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("contests/problems"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn();
    }

    /**
     * Test of showProblem method, of class ContestProblemController.
     *
     * @see ContestProblemController#showProblem(long, long, Model, Locale)
     */
    @Test
    public void testShowProblem() throws Exception {
        log.info("showProblem");
        long contestId = mockDataService.contest().getId();
        long pid = mockDataService.problem(builder -> builder.contests(new long[]{contestId})).getId();
        Locale locale = Locale.getDefault();
        mvc.perform(get("/contests/{contestId}/problems/{pid}", contestId, 1000)
                .locale(locale))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("contests/problem"))
                .andReturn();
    }

    /**
     * Test of status method, of class ContestProblemController.
     *
     * @see ContestProblemController#status(long, int, Pageable, Model,
     * Authentication, HttpServletRequest)
     */
    @Test
    public void testStatus() throws Exception {
        log.info("status");
        long contestId = mockDataService.contest().getId();
        long pid = mockDataService.problem(builder -> builder.contests(new long[]{contestId})).getId();
        mvc.perform(get("/contests/{contestId}/problems/{pid}/status", contestId, 1000))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn();
    }

}
