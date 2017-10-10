package cn.edu.zjnu.acm.judge.controller.submission;

import cn.edu.zjnu.acm.judge.Application;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
public class StatusControllerTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = webAppContextSetup(context).build();
    }

    /**
     * Test of status method, of class StatusController.
     * {@link StatusController#status(HttpServletRequest, String, Long, int, int, Long, Integer, String, Long, Authentication)}
     */
    @Test
    public void testStatus() throws Exception {
        log.info("status");
        String problem_id = "";
        Long contest_id = null;
        int language = 0;
        int size = 0;
        Long bottom = null;
        Integer score = null;
        String user_id = "";
        Long top = null;
        MvcResult result = mvc.perform(get("/status")
                .param("problem_id", problem_id)
                .param("contest_id", Objects.toString(contest_id, ""))
                .param("language", Integer.toString(language))
                .param("size", Integer.toString(size))
                .param("bottom", Objects.toString(bottom, ""))
                .param("score", Objects.toString(score, ""))
                .param("user_id", user_id)
                .param("top", Objects.toString(top, "")))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

}
