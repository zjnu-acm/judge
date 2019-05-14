package cn.edu.zjnu.acm.judge.controller.submission;

import cn.edu.zjnu.acm.judge.Application;
import cn.edu.zjnu.acm.judge.domain.Submission;
import cn.edu.zjnu.acm.judge.service.AccountService;
import cn.edu.zjnu.acm.judge.service.DeleteService;
import cn.edu.zjnu.acm.judge.service.MockDataService;
import cn.edu.zjnu.acm.judge.service.SubmissionService;
import cn.edu.zjnu.acm.judge.service.SystemService;
import cn.edu.zjnu.acm.judge.util.CopyHelper;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@RunWith(JUnitPlatform.class)
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
    private SystemService systemService;
    private Submission submission;
    @Autowired
    private AccountService accountService;
    @Autowired
    private DeleteService deleteService;

    @BeforeEach
    public void setUp() throws IOException, URISyntaxException {
        submission = mockDataService.submission(false);
        Path dataDir = systemService.getDataDirectory(submission.getProblem());
        CopyHelper.copy(Paths.get(RejudgeControllerTest.class.getResource("/sample/data").toURI()), dataDir, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
    }

    @AfterEach
    public void tearDown() throws IOException {
        if (submission != null) {
            deleteService.delete(systemService.getDataDirectory(submission.getProblem()));
            submissionService.delete(submission.getId());
            accountService.delete(submission.getUser());
        }
    }

    /**
     * Test of rejudgeSolution method, of class RejudgeController.
     *
     * {@link RejudgeController#rejudgeSolution(long)}
     */
    @Test
    public void testRejudgeSolution() throws Exception {
        assumeTrue(Arrays.asList(environment.getActiveProfiles()).contains("appveyor"), "not appveyor");
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
     * {@link RejudgeController#rejudgeProblem(long)}
     */
    @Test
    public void testRejudgeProblem() throws Exception {
        log.info("rejudgeProblem");
        long problemId = 0;
        MvcResult result = mvc.perform(get("/admin/rejudge")
                .accept(MediaType.TEXT_HTML, MediaType.APPLICATION_JSON)
                .param("problem_id", Long.toString(problemId)))
                .andExpect(status().isAccepted())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
    }

}
