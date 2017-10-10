package cn.edu.zjnu.acm.judge.controller.submission;

import cn.edu.zjnu.acm.judge.Application;
import cn.edu.zjnu.acm.judge.service.MockDataService;
import java.util.Objects;
import javax.servlet.Filter;
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
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
public class SubmitPageControllerTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;
    @Autowired
    private MockDataService mockDataService;
    @Autowired
    private Filter springSecurityFilterChain;

    @Before
    public void setUp() {
        mvc = webAppContextSetup(context).addFilter(springSecurityFilterChain).build();
    }

    /**
     * Test of submitPage method, of class SubmitPageController.
     *
     * @see SubmitPageController#submitPage(Model, Long, Long, Authentication)
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
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn();
    }

}
