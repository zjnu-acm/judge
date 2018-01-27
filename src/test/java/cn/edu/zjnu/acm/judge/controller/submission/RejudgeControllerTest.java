package cn.edu.zjnu.acm.judge.controller.submission;

import cn.edu.zjnu.acm.judge.Application;
import cn.edu.zjnu.acm.judge.config.JudgeConfiguration;
import cn.edu.zjnu.acm.judge.domain.Submission;
import cn.edu.zjnu.acm.judge.service.AccountService;
import cn.edu.zjnu.acm.judge.service.AccountServiceTest;
import cn.edu.zjnu.acm.judge.service.MockDataService;
import cn.edu.zjnu.acm.judge.service.SubmissionService;
import cn.edu.zjnu.acm.judge.util.CopyHelper;
import cn.edu.zjnu.acm.judge.util.DeleteHelper;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.hasItemInArray;
import static org.junit.Assume.assumeThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@WithMockUser(roles = "ADMIN")
public class RejudgeControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private MockDataService mockDataService;
    @Autowired
    private Environment environment;
    @Autowired
    private SubmissionService submissionService;
    @Autowired
    private JudgeConfiguration judgeConfiguration;
    private Submission submission;
    @Autowired
    private AccountService accountService;

    @Before
    public void setUp() throws IOException, URISyntaxException {
        submission = mockDataService.submission();
        Path dataDir = judgeConfiguration.getDataDirectory(submission.getProblem());
        CopyHelper.copy(Paths.get(RejudgeControllerTest.class.getResource("/sample/data").toURI()), dataDir, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
    }

    @After
    public void tearDown() throws IOException {
        if (submission != null) {
            DeleteHelper.delete(judgeConfiguration.getDataDirectory(submission.getProblem()));
            submissionService.delete(submission.getId());
            AccountServiceTest.delete(accountService, submission.getUser());
        }
    }

    /**
     * Test of rejudgeSolution method, of class RejudgeController.
     *
     * @see RejudgeController#rejudgeSolution(long)
     */
    @Test
    public void testRejudgeSolution() throws Exception {
        assumeThat(environment.getActiveProfiles(), hasItemInArray("appveyor"));
        log.info("rejudgeSolution");
        long solutionId = submission.getId();
        MvcResult result = mvc.perform(get("/admin/rejudge")
                .accept(MediaType.TEXT_HTML, MediaType.APPLICATION_JSON)
                .param("solution_id", Long.toString(solutionId)))
                .andExpect(request().asyncStarted())
                .andReturn();
        mvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    /**
     * Test of rejudgeProblem method, of class RejudgeController.
     *
     * @see RejudgeController#rejudgeProblem(long)
     */
    @Test
    public void testRejudgeProblem() throws Exception {
        log.info("rejudgeProblem");
        long problem_id = 0;
        MvcResult result = mvc.perform(get("/admin/rejudge")
                .accept(MediaType.TEXT_HTML, MediaType.APPLICATION_JSON)
                .param("problem_id", Long.toString(problem_id)))
                .andExpect(status().isAccepted())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
    }

}
