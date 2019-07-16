package cn.edu.zjnu.acm.judge.controller.legacy;

import cn.edu.zjnu.acm.judge.Application;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(JUnitPlatform.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
public class LegacyControllerTest {

    @Autowired
    private MockMvc mvc;

    /**
     * Test of contestStanding method, of class LegacyController.
     *
     * {@link LegacyController#contestStanding(long, RedirectAttributes)}
     */
    @Test
    public void testContestStanding() throws Exception {
        log.info("contestStanding");
        long contestId = 0;
        MvcResult result = mvc.perform(get("/conteststanding").param("contest_id", Long.toString(contestId)))
                .andExpect(redirectedUrl("/contests/" + contestId + "/standing.html"))
                .andReturn();
    }

    /**
     * Test of showContest method, of class LegacyController.
     *
     * {@link LegacyController#showContest(long, RedirectAttributes)}
     */
    @Test
    public void testShowContest() throws Exception {
        log.info("showContest");
        long contestId = 1058;
        MvcResult result = mvc.perform(get("/showcontest").param("contest_id", Long.toString(contestId)))
                .andExpect(redirectedUrl("/contests/" + contestId + "/problems.html"))
                .andReturn();
    }

    /**
     * Test of ckfinder method, of class LegacyController.
     *
     * {@link LegacyController#ckfinder(HttpServletRequest, HttpServletResponse, String)}
     */
    @Test
    public void testCkfinder() throws Exception {
        log.info("ckfinder");
        String requestUri = "/support/ckfinder.action";
        mvc.perform(get("/ctx" + requestUri).param("path", "files/a.jpg").contextPath("/ctx"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/ctx/userfiles/files/a.jpg"))
                .andReturn();
        mvc.perform(get(requestUri).param("path", "/files/a.jpg"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/userfiles/files/a.jpg"))
                .andReturn();
        mvc.perform(get(requestUri).param("path", "/files/%A.jpg"))
                .andExpect(status().isNotFound())
                .andReturn();
        mvc.perform(get(requestUri).param("path", "http://localhost/files/a.jpg").contextPath(""))
                .andExpect(status().isNotFound())
                .andReturn();
    }

}
