package cn.edu.zjnu.acm.judge.controller.contest;

import cn.edu.zjnu.acm.judge.Application;
import cn.edu.zjnu.acm.judge.data.form.ContestStatus;
import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.service.ContestService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.hamcrest.Matchers.isIn;
import static org.springframework.http.MediaType.ALL;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_HTML;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
public class ContestListControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ContestService contestService;

    private void clearPending() throws Exception {
        for (Contest contest : contestService.findAll(ContestStatus.PENDING)) {
            contestService.delete(contest.getId());
        }
    }

    @Test
    public void testContentType() throws Exception {
        clearPending();
        MvcResult result;
        result = mvc.perform(get("/scheduledcontests").accept(TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(TEXT_HTML))
                .andReturn();
        result = mvc.perform(get("/scheduledcontests").accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        result = mvc.perform(get("/scheduledcontests").accept(TEXT_HTML, APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(TEXT_HTML))
                .andReturn();
        result = mvc.perform(get("/scheduledcontests").accept(APPLICATION_JSON, TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        result = mvc.perform(get("/scheduledcontests").accept(ALL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(TEXT_HTML))
                .andReturn();
    }

    @Test
    public void testRedirect() throws Exception {
        clearPending();
        Instant start = Instant.now().plus(-1, ChronoUnit.HOURS);
        Instant end = Instant.now().plus(4, ChronoUnit.HOURS);

        contestService.save(Contest.builder().createdTime(Instant.now()).disabled(false)
                .startTime(start)
                .endTime(end)
                .title("sample contest")
                .description("no description")
                .build());
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
                .andExpect(isOkOrFound())
                .andReturn();
    }

    private ResultMatcher isOkOrFound() {
        return status().is(isIn(new Integer[]{200, 302}));
    }

}
