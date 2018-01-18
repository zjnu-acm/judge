package cn.edu.zjnu.acm.judge.controller.user;

import cn.edu.zjnu.acm.judge.Application;
import java.util.Locale;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
public class ResetPasswordControllerTest {

    @Autowired
    private MockMvc mvc;

    /**
     * Test of doGet method, of class ResetPasswordController.
     *
     * @see ResetPasswordController#doGet(HttpServletRequest)
     */
    @Test
    public void testDoGet() throws Exception {
        log.info("doGet");
        MvcResult result = mvc.perform(get("/resetPassword"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn();
    }

    // TODO
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
                .andExpect(status().isOk())
                .andReturn();
    }

    // TODO
    /**
     * Test of changePassword method, of class ResetPasswordController.
     *
     * @see ResetPasswordController#changePassword(HttpServletRequest,
     * HttpServletResponse, String, String)
     */
    @Test
    public void testChangePassword() throws Exception {
        log.info("changePassword");
        MvcResult result = mvc.perform(post("/resetPassword"))
                .andExpect(status().isOk())
                .andReturn();
    }

}
