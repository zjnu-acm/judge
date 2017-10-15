package cn.edu.zjnu.acm.judge.controller.submission;

import cn.edu.zjnu.acm.judge.Application;
import cn.edu.zjnu.acm.judge.domain.Submission;
import cn.edu.zjnu.acm.judge.service.MockDataService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
public class ShowCompileInfoControllerTest {

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
     * Test of showCompileInfo method, of class ShowCompileInfoController.
     *
     * @see ShowCompileInfoController#showCompileInfo(long, Model,
     * Authentication)
     */
    @Test
    public void testShowCompileInfo() throws Exception {
        log.info("showCompileInfo");
        Submission submission = mockDataService.submission();
        MvcResult result = mvc.perform(get("/showcompileinfo").with(user(submission.getUser()))
                .param("solution_id", Long.toString(submission.getId())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("showcompileinfo"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn();
    }

}
