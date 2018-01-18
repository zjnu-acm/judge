package cn.edu.zjnu.acm.judge.controller.user;

import cn.edu.zjnu.acm.judge.Application;
import cn.edu.zjnu.acm.judge.service.MockDataService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
public class RegisterControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private MockDataService mockDataService;

    /**
     * Test of register method, of class RegisterController.
     *
     * @see RegisterController#register(HttpServletRequest, String, String,
     * String, String, String, String)
     */
    @Test
    public void testRegister() throws Exception {
        log.info("register");
        String userId = mockDataService.user(false).getId();
        String school = "";
        String nick = "";
        String password = userId;
        String email = "";
        String rptPassword = password;
        MvcResult result = mvc.perform(post("/register")
                .param("user_id", userId)
                .param("school", school)
                .param("nick", nick)
                .param("password", password)
                .param("email", email)
                .param("rptPassword", rptPassword))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("register"))
                .andReturn();
    }

}
