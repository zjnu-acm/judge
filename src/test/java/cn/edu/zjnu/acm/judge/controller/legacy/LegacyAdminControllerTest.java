package cn.edu.zjnu.acm.judge.controller.legacy;

import cn.edu.zjnu.acm.judge.Application;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
@WithMockUser(roles = "ADMIN")
public class LegacyAdminControllerTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = webAppContextSetup(context).build();
    }

    /**
     * Test of showProblem method, of class LegacyAdminController.
     *
     * @see LegacyAdminController#showProblem(long, RedirectAttributes)
     */
    @Test
    public void testShowProblem() throws Exception {
        log.info("showProblem");
        long problem_id = 0;
        MvcResult result = mvc.perform(get("/admin.showproblem").param("problem_id", Long.toString(problem_id)))
                .andDo(print())
                .andExpect(redirectedUrl("/admin/problems/" + problem_id + ".html"))
                .andReturn();
    }

    /**
     * Test of showContest method, of class LegacyAdminController.
     *
     * @see LegacyAdminController#showContest(long, RedirectAttributes)
     */
    @Test
    public void testShowContest() throws Exception {
        log.info("showContest");
        long contest_id = 0;
        MvcResult result = mvc.perform(get("/admin.showcontest").param("contest_id", Long.toString(contest_id)))
                .andDo(print())
                .andExpect(redirectedUrl("/admin/contests/" + contest_id + ".html"))
                .andReturn();
    }

}
