package cn.edu.zjnu.acm.judge.controller.user;

import cn.edu.zjnu.acm.judge.Application;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
public class ResetPasswordControllerTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = webAppContextSetup(context).build();
    }

    /**
     * Test of doGet method, of class ResetPasswordController.
     *
     * @see ResetPasswordController#doGet(HttpServletRequest)
     */
    @Test
    public void testDoGet() throws Exception {
        log.info("doGet");
        MvcResult result = mvc.perform(get("/resetPassword"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    /**
     * Test of doPost method, of class ResetPasswordController.
     *
     * @see ResetPasswordController#doPost(HttpServletRequest,
     * HttpServletResponse, String, String, String, Locale)
     */
    @Test
    public void testDoPost() throws Exception {
        log.info("doPost");
        String action = "";
        String verify = "";
        String username = "";
        Locale locale = Locale.getDefault();
        MvcResult result = mvc.perform(post("/resetPassword")
                .param("action", action)
                .param("verify", verify)
                .param("username", username)
                .locale(locale))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    /**
     * Test of changePassword method, of class ResetPasswordController.
     *
     * @see ResetPasswordController#changePassword(HttpServletRequest,
     * HttpServletResponse)
     */
    @Test
    public void testChangePassword() throws Exception {
        log.info("changePassword");
        MvcResult result = mvc.perform(post("/resetPassword"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

}
