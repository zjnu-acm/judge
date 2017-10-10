package cn.edu.zjnu.acm.judge.controller.user;

import cn.edu.zjnu.acm.judge.Application;
import javax.servlet.http.HttpServletRequest;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
public class RegisterControllerTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = webAppContextSetup(context).build();
    }

    /**
     * Test of register method, of class RegisterController.
     * {@link RegisterController#register(HttpServletRequest, String, String, String, String, String, String)}
     */
    @Test
    public void testRegister() throws Exception {
        System.out.println("register");
        String user_id = "";
        String school = "";
        String nick = "";
        String password = "";
        String email = "";
        String rptPassword = "";
        MvcResult result = mvc.perform(post("/register")
                .param("user_id", user_id)
                .param("school", school)
                .param("nick", nick)
                .param("password", password)
                .param("email", email)
                .param("rptPassword", rptPassword))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

}
