package cn.edu.zjnu.acm.judge.controller.submission;

import cn.edu.zjnu.acm.judge.Application;
import cn.edu.zjnu.acm.judge.service.MockDataService;
import com.google.common.base.Strings;
import java.util.Objects;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
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
     * Test of submitPage method, of class SubmitPageController.
     *
     * @see SubmitController#submitPage(Model, Long, Authentication)
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
     * Test of submit method, of class SubmitController.
     *
     * @see SubmitController#submit(HttpServletRequest, Long, long, int, String,
     * RedirectAttributes, Authentication)
     */
    @Test
    public void testSubmit() throws Exception {
        log.info("submit");
        int language = 0;
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
