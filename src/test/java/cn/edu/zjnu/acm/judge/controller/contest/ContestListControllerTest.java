package cn.edu.zjnu.acm.judge.controller.contest;

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
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.hamcrest.Matchers.isIn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
public class ContestListControllerTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = webAppContextSetup(context).build();
    }

    /**
     * Test of contests method, of class ContestListController.
     *
     * @see ContestListController#contests(Model, RedirectAttributes)
     */
    @Test
    public void testContests() throws Exception {
        log.info("contests");
        MvcResult result = mvc.perform(get("/contests"))
                .andDo(print())
                .andExpect(isOkOrFound())
                .andReturn();
    }

    /**
     * Test of scheduledContests method, of class ContestListController.
     *
     * @see ContestListController#scheduledContests(Model, RedirectAttributes)
     */
    @Test
    public void testScheduledContests() throws Exception {
        log.info("scheduledContests");
        MvcResult result = mvc.perform(get("/scheduledcontests"))
                .andDo(print())
                .andExpect(isOkOrFound())
                .andReturn();
    }

    /**
     * Test of pastContests method, of class ContestListController.
     *
     * @see ContestListController#pastContests(Model, RedirectAttributes)
     */
    @Test
    public void testPastContests() throws Exception {
        log.info("pastContests");
        MvcResult result = mvc.perform(get("/pastcontests"))
                .andDo(print())
                .andExpect(isOkOrFound())
                .andReturn();
    }

    /**
     * Test of currentContests method, of class ContestListController.
     *
     * @see ContestListController#currentContests(Model, RedirectAttributes)
     */
    @Test
    public void testCurrentContests() throws Exception {
        log.info("currentContests");
        MvcResult result = mvc.perform(get("/currentcontests"))
                .andDo(print())
                .andExpect(isOkOrFound())
                .andReturn();
    }

    private ResultMatcher isOkOrFound() {
        return status().is(isIn(new Integer[]{200, 302}));
    }

}
