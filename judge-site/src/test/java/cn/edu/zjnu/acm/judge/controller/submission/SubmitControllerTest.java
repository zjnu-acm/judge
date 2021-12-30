package cn.edu.zjnu.acm.judge.controller.submission;

import cn.edu.zjnu.acm.judge.Application;
import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.domain.User;
import cn.edu.zjnu.acm.judge.service.MockDataService;
import com.google.common.base.Strings;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@RunWith(JUnitPlatform.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
public class SubmitControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private MockDataService mockDataService;

    /**
     * Test of submitPage method, of class SubmitController.
     *
     * {@link SubmitController#submitPage(Model, Long)}
     */
    @Test
    public void testSubmitPage() throws Exception {
        log.info("submitPage");
        Long problem_id = null;
        Long contest_id = null;
        String userId = mockDataService.user().getId();
        MvcResult result = mvc.perform(get("/submitpage").with(user(userId))
                .param("problem_id", Objects.toString(problem_id, ""))
                .param("contest_id", Objects.toString(contest_id, "")))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn();
    }

    /**
     * Test of contestSubmitPage method, of class SubmitController.
     *
     * {@link SubmitController#contestSubmitPage(Model, Long, long)}
     */
    @Test
    public void testContestSubmitPage() throws Exception {
        log.info("contestSubmitPage");
        User user = mockDataService.user();
        Problem problem = mockDataService.problem();
        {
            mvc.perform(get("/problems/{problemId}/submit", problem.getId()).with(anonymous()))
                    .andExpect(forwardedUrl("/unauthorized"));
            mvc.perform(get("/problems/{problemId}/submit", problem.getId()).with(user(user.getId())))
                    .andExpect(status().isOk())
                    .andReturn();
        }
        {
            Contest contest = mockDataService.contest();

            long problemId = problem.getId();
            long contestId = contest.getId();
            mvc.perform(get("/contests/{contestId}/problems/{problemId}/submit", contestId, problemId).with(user(user.getId())))
                    .andExpect(status().isOk())
                    .andReturn();
        }
    }

    /**
     * Test of submit method, of class SubmitController.
     *
     * {@link SubmitController#submit(HttpServletRequest, Long, long, int, String, RedirectAttributes)}
     */
    @Test
    public void testSubmit() throws Exception {
        log.info("submit");
        int language = mockDataService.anyLanguage().getId();
        long problemId = mockDataService.problem().getId();
        String source = Strings.repeat(" ", 20);
        String userId = mockDataService.user().getId();
        MvcResult result = mvc.perform(post("/problems/{problemId}/submit", problemId).with(user(userId))
                .param("language", Integer.toString(language))
                .param("source", source))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/status?user_id=" + userId))
                .andReturn();
    }

}
