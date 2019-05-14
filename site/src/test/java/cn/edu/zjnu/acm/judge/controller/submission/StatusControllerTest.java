package cn.edu.zjnu.acm.judge.controller.submission;

import cn.edu.zjnu.acm.judge.Application;
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
import org.springframework.security.core.Authentication;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(JUnitPlatform.class)
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
     * {@link StatusController#status(HttpServletRequest, String, Long, int, int, Long, Integer, String, Long, Authentication, Model)}
     */
    @Test
    public void testStatus() throws Exception {
        log.info("status");
        String problemId = "";
        Long contestId = null;
        int language = 0;
        int size = 20;
        Long[] bottoms = {null, 2000L};
        Integer score = null;
        String userId = "";
        Long top = null;
        for (Long bottom : bottoms) {
            MvcResult result = mvc.perform(get("/status")
                    .param("problem_id", problemId)
                    .param("contest_id", Objects.toString(contestId, ""))
                    .param("language", Integer.toString(language))
                    .param("size", Integer.toString(size))
                    .param("bottom", Objects.toString(bottom, ""))
                    .param("score", Objects.toString(score, ""))
                    .param("user_id", userId)
                    .param("top", Objects.toString(top, "")))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                    .andReturn();
        }

    }

}
