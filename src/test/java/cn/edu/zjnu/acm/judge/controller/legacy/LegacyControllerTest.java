package cn.edu.zjnu.acm.judge.controller.legacy;

import cn.edu.zjnu.acm.judge.Application;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
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
     * @see LegacyController#contestStanding(long, RedirectAttributes)
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
     * @see LegacyController#showContest(long, RedirectAttributes)
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
     * Test of ga method, of class LegacyController.
     *
     * @see LegacyController#ga()
     */
    @Test
    public void testGa() throws Exception {
        log.info("ga");
        MvcResult result = mvc.perform(get("/ga"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn();
    }

    /**
     * Test of nav method, of class LegacyController.
     *
     * @see LegacyController#nav()
     */
    @Test
    public void testNav() throws Exception {
        log.info("nav");
        MvcResult result = mvc.perform(get("/nav"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn();
    }

    /**
     * Test of footer method, of class LegacyController.
     *
     * @see LegacyController#footer()
     */
    @Test
    public void testFooter() throws Exception {
        log.info("footer");
        MvcResult result = mvc.perform(get("/footer"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn();
    }

}
