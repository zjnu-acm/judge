package cn.edu.zjnu.acm.judge.controller.user;

import cn.edu.zjnu.acm.judge.Application;
import cn.edu.zjnu.acm.judge.service.MockDataService;
import com.google.code.kaptcha.Constants;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(JUnitPlatform.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
public class ResetPasswordControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private MockDataService mockDataService;

    /**
     * Test of doGet method, of class ResetPasswordController.
     * {@link ResetPasswordController#doGet(String, String)}
     */
    @Test
    public void testDoGet() throws Exception {
        log.info("doGet");
        MvcResult result = mvc.perform(get("/resetPassword"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn();
    }

    /**
     * Test of methods doPost, changePassword, of class ResetPasswordController.
     *
     * {@link ResetPasswordController#doPost(HttpServletRequest, String, String, Locale)}
     * {@link ResetPasswordController#changePassword(HttpServletRequest, String, String)}
     */
    @Test
    public void testChangePassword() throws Exception {
        Locale locale = Locale.getDefault();
        MvcResult result = mvc.perform(get("/images/rand.jpg"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.IMAGE_JPEG))
                .andReturn();
        HttpSession session = result.getRequest().getSession(false);
        assertThat(session).isNotNull();
        String verify = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
        String username = mockDataService.user(
                builder -> builder.email("admin@local.host")
        ).getId();
        mvc.perform(post("/resetPassword?_format=json")
                .param("verify", verify + "1")
                .param("username", username)
                .locale(locale))
                .andExpect(status().isBadRequest())
                .andReturn();
        mvc.perform(post("/resetPassword?_format=json")
                .session((MockHttpSession) session)
                .param("verify", verify + "1")
                .param("username", username)
                .locale(locale))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertThat(session.getAttribute(Constants.KAPTCHA_SESSION_KEY)).isNull();

        mvc.perform(get("/images/rand.jpg")
                .session((MockHttpSession) session))
                .andExpect(content().contentTypeCompatibleWith(MediaType.IMAGE_JPEG))
                .andReturn();
        verify = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
        mvc.perform(post("/resetPassword?_format=json")
                .session((MockHttpSession) session)
                .param("verify", verify)
                .param("username", username)
                .locale(locale))
                .andExpect(status().isInternalServerError())
                .andReturn();
        assertThat(session.getAttribute(Constants.KAPTCHA_SESSION_KEY)).isNull();
        mvc.perform(post("/resetPassword?_format=json")
                .param("action", "changePassword"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

}
