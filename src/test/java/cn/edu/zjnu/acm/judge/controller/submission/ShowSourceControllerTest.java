package cn.edu.zjnu.acm.judge.controller.submission;

import cn.edu.zjnu.acm.judge.Application;
import cn.edu.zjnu.acm.judge.domain.Submission;
import cn.edu.zjnu.acm.judge.service.MockDataService;
import java.util.Objects;
import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
public class ShowSourceControllerTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;
    @Autowired
    private Filter springSecurityFilterChain;
    @Autowired
    private MockDataService mockDataService;

    @Before
    public void setUp() {
        mvc = webAppContextSetup(context).addFilter(springSecurityFilterChain).build();
    }

    /**
     * Test of showSource method, of class ShowSourceController.
     *
     * @see ShowSourceController#showSource(HttpServletRequest, long, Integer,
     * Authentication)
     */
    @Test
    public void testShowSource() throws Exception {
        log.info("showSource");
        Submission submission = mockDataService.submission();
        long solutionId = submission.getId();
        Integer style = null;
        MvcResult result = mvc.perform(get("/showsource").with(user(submission.getUser()))
                .param("solution_id", Long.toString(solutionId))
                .param("style", Objects.toString(style, "")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("submissions/source"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn();
    }

}
