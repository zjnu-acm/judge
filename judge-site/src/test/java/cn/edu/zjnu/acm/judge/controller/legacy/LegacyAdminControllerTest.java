package cn.edu.zjnu.acm.judge.controller.legacy;

import cn.edu.zjnu.acm.judge.Application;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@AutoConfigureMockMvc
@RunWith(JUnitPlatform.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
@WithMockUser(roles = "ADMIN")
public class LegacyAdminControllerTest {

    @Autowired
    private MockMvc mvc;

    /**
     * Test of showProblem method, of class LegacyAdminController.
     *
     * {@link LegacyAdminController#showProblem(long, RedirectAttributes)}
     */
    @Test
    public void testShowProblem() throws Exception {
        log.info("showProblem");
        long problemId = 0;
        MvcResult result = mvc.perform(get("/admin.showproblem").param("problem_id", Long.toString(problemId)))
                .andExpect(redirectedUrl("/admin/problems/" + problemId + "?_format=html"))
                .andReturn();
    }

    /**
     * Test of showContest method, of class LegacyAdminController.
     *
     * {@link LegacyAdminController#showContest(long, RedirectAttributes)}
     */
    @Test
    public void testShowContest() throws Exception {
        log.info("showContest");
        long contestId = 0;
        MvcResult result = mvc.perform(get("/admin.showcontest").param("contest_id", Long.toString(contestId)))
                .andExpect(redirectedUrl("/admin/contests/" + contestId + "?_format=html"))
                .andReturn();
    }

    /**
     * Test of rejudge method, of class LegacyAdminController.
     *
     * {@link LegacyAdminController#rejudge(HttpServletRequest)}
     */
    @Test
    public void testRejudge() throws Exception {
        log.info("rejudge");
        MvcResult result = mvc.perform(get("/admin.rejudge").param("contest_id", Long.toString(1)))
                .andExpect(redirectedUrl("/admin/rejudge?contest_id=1"))
                .andReturn();
    }

}
