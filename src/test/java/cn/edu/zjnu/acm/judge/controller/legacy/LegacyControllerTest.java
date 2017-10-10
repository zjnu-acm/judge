package cn.edu.zjnu.acm.judge.controller.legacy;

import cn.edu.zjnu.acm.judge.Application;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
public class LegacyControllerTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = webAppContextSetup(context).build();
    }

    /**
     * Test of contestStanding method, of class LegacyController.
     *
     * @see LegacyController#contestStanding(long)
     */
    @Test
    public void testContestStanding() throws Exception {
        log.info("contestStanding");
        long contestId = 0;
        MvcResult result = mvc.perform(get("/conteststanding").param("contest_id", Long.toString(contestId)))
                .andDo(print())
                .andExpect(forwardedUrl("/contests/" + contestId + "/standing"))
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
                .andDo(print())
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
                .andDo(print())
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
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn();
    }

}
