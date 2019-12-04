package cn.edu.zjnu.acm.judge.controller.contest;

import cn.edu.zjnu.acm.judge.Application;
import cn.edu.zjnu.acm.judge.data.form.ContestStatus;
import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.service.ContestService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.hamcrest.Matchers.in;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.MediaType.ALL;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.IMAGE_JPEG;
import static org.springframework.http.MediaType.TEXT_HTML;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@RunWith(JUnitPlatform.class)
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

    private ResultActions contestTestBase(ResultMatcher matcher, MediaType expect, MediaType... accept) throws Exception {
        MockHttpServletRequestBuilder builder = get("/scheduledcontests");
        if (accept.length != 0) {
            builder = builder.accept(accept);
        }
        ResultActions resultActions = mvc.perform(builder).andExpect(matcher);
        if (expect != null) {
            resultActions.andExpect(content().contentTypeCompatibleWith(expect));
        }
        return resultActions;
    }

    @Test
    public void testContentType() throws Exception {
        clearPending();
        // first is matched
        contestTestBase(status().isOk(), TEXT_HTML, TEXT_HTML);
        contestTestBase(status().isOk(), TEXT_HTML, TEXT_HTML, APPLICATION_JSON);
        // the matching controller produces TEXT_HTML, but then we goto the error page, it supports produces APPLICATION_JSON,
        // so the content type is set by the controller advice class
        ResultActions result = contestTestBase(status().isOk(), APPLICATION_JSON, APPLICATION_JSON, TEXT_HTML);
        result.andExpect(handler().handlerType(ContestListController.class))
                .andExpect(jsonPath("message", notNullValue()));
        // non matched
        contestTestBase(status().isNotAcceptable(), null, IMAGE_JPEG, APPLICATION_JSON);
        contestTestBase(status().isNotAcceptable(), null, IMAGE_JPEG);
        contestTestBase(status().isOk(), TEXT_HTML);
        // match all
        contestTestBase(status().isOk(), TEXT_HTML, ALL);
    }

    @Test
    public void testRedirect() throws Exception {
        clearPending();
        Instant start = Instant.now().plus(1, ChronoUnit.HOURS);
        Instant end = Instant.now().plus(4, ChronoUnit.HOURS);

        contestService.save(Contest.builder().createdTime(Instant.now()).disabled(false)
                .startTime(start)
                .endTime(end)
                .title("sample contest")
                .description("no description")
                .build());
        mvc.perform(get("/scheduledcontests"))
                .andExpect(status().isFound())
                .andReturn();
        contestService.save(Contest.builder().createdTime(Instant.now()).disabled(false)
                .startTime(start)
                .endTime(end)
                .title("sample contest2")
                .description("no description2")
                .build());
        mvc.perform(get("/scheduledcontests"))
                .andExpect(status().isOk())
                .andExpect(view().name("contests/index"))
                .andReturn();
    }

    /**
     * Test of contests method, of class ContestListController.
     *
     * {@link ContestListController#contests(Model, RedirectAttributes)}
     */
    @Test
    public void testContests() throws Exception {
        log.info("contests");
        mvc.perform(get("/contests"))
                .andExpect(isOkOrFound())
                .andReturn();
    }

    /**
     * Test of scheduledContests method, of class ContestListController.
     *
     * {@link ContestListController#scheduledContests(Model, RedirectAttributes)}
     */
    @Test
    public void testScheduledContests() throws Exception {
        log.info("scheduledContests");
        mvc.perform(get("/scheduledcontests"))
                .andExpect(isOkOrFound())
                .andReturn();
    }

    /**
     * Test of pastContests method, of class ContestListController.
     *
     * {@link ContestListController#pastContests(Model, RedirectAttributes)}
     */
    @Test
    public void testPastContests() throws Exception {
        log.info("pastContests");
        mvc.perform(get("/pastcontests"))
                .andExpect(isOkOrFound())
                .andReturn();
    }

    /**
     * Test of currentContests method, of class ContestListController.
     *
     * {@link ContestListController#currentContests(Model, RedirectAttributes)}
     */
    @Test
    public void testCurrentContests() throws Exception {
        log.info("currentContests");
        mvc.perform(get("/currentcontests"))
                .andExpect(isOkOrFound())
                .andReturn();
    }

    private ResultMatcher isOkOrFound() {
        return status().is(in(new Integer[]{200, 302}));
    }

}
