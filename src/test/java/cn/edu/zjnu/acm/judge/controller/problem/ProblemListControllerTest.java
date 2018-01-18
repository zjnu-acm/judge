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
public class ProblemListControllerTest {

    @Autowired
    private MockMvc mvc;

    /**
     * Test of problemList method, of class ProblemListController.
     *
     * @see ProblemListController#problemList(ProblemForm, Model, Locale,
     * Authentication, Pageable, HttpServletRequest)
     */
    @Test
    public void testProblemList() throws Exception {
        log.info("problemList");
        String sstr = "";
        Boolean disabled = null;
        Locale locale = Locale.getDefault();
        MvcResult result = mvc.perform(get("/problemlist")
                .param("sstr", sstr)
                .param("disabled", Objects.toString(disabled, ""))
                .locale(locale))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn();
    }

}
