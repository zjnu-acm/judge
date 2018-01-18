package cn.edu.zjnu.acm.judge.controller.problem;

import cn.edu.zjnu.acm.judge.Application;
import java.util.Locale;
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
public class SearchProblemControllerTest {

    @Autowired
    private MockMvc mvc;

    /**
     * Test of searchProblem method, of class SearchProblemController.
     *
     * @see SearchProblemController#searchProblem(ProblemForm, Model, Locale,
     * Authentication, Pageable, HttpServletRequest)
     */
    @Test
    public void testSearchProblem() throws Exception {
        log.info("searchProblem");
        String sstr = "test";
        Boolean disabled = null;
        Locale locale = Locale.getDefault();
        MvcResult result = mvc.perform(get("/searchproblem")
                .param("sstr", sstr)
                .param("disabled", Objects.toString(disabled, ""))
                .locale(locale))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn();
    }

}
