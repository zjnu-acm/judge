package cn.edu.zjnu.acm.judge.controller.submission;

import cn.edu.zjnu.acm.judge.Application;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
public class StatusControllerTest {

    @Autowired
    private MockMvc mvc;

    /**
     * Test of status method, of class StatusController.
     *
     * @see StatusController#status(HttpServletRequest, String, Long, int, int,
     * Long, Integer, String, Long, Authentication)
     */
    @Test
    public void testStatus() throws Exception {
        log.info("status");
        String problem_id = "";
        Long contest_id = null;
        int language = 0;
        int size = 20;
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
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn();
    }

}
