package cn.edu.zjnu.acm.judge.controller.contest;

import cn.edu.zjnu.acm.judge.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
     * {@link ContestListController#contests(Model, RedirectAttributes)}
     */
    @Test
    public void testContests() throws Exception {
        System.out.println("contests");
        MvcResult result = mvc.perform(get("/contests"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    /**
     * Test of scheduledContests method, of class ContestListController.
     * {@link ContestListController#scheduledContests(Model, RedirectAttributes)}
     */
    @Test
    public void testScheduledContests() throws Exception {
        System.out.println("scheduledContests");
        MvcResult result = mvc.perform(get("/scheduledcontests"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    /**
     * Test of pastContests method, of class ContestListController.
     * {@link ContestListController#pastContests(Model, RedirectAttributes)}
     */
    @Test
    public void testPastContests() throws Exception {
        System.out.println("pastContests");
        MvcResult result = mvc.perform(get("/pastcontests"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    /**
     * Test of currentContests method, of class ContestListController.
     * {@link ContestListController#currentContests(Model, RedirectAttributes)}
     */
    @Test
    public void testCurrentContests() throws Exception {
        System.out.println("currentContests");
        MvcResult result = mvc.perform(get("/currentcontests"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

}
