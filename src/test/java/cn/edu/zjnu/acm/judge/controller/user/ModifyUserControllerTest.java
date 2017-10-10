package cn.edu.zjnu.acm.judge.controller.user;

import cn.edu.zjnu.acm.judge.Application;
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
import org.springframework.ui.Model;
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
public class ModifyUserControllerTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = webAppContextSetup(context).build();
    }

    /**
     * Test of updatePage method, of class ModifyUserController.
     * {@link ModifyUserController#updatePage(Model, Authentication)}
     */
    @Test
    public void testUpdatePage() throws Exception {
        log.info("updatePage");
        MvcResult result = mvc.perform(get("/modifyuserpage"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    /**
     * Test of update method, of class ModifyUserController.
     * {@link ModifyUserController#update(Model, String, String, String, String, String, String, Authentication)}
     */
    @Test
    public void testUpdate() throws Exception {
        log.info("update");
        String oldPassword = "";
        String newPassword = "";
        String rptPassword = "";
        String email = "";
        String nick = "";
        String school = "";
        MvcResult result = mvc.perform(post("/modifyuser")
                .param("oldPassword", oldPassword)
                .param("newPassword", newPassword)
                .param("rptPassword", rptPassword)
                .param("email", email)
                .param("nick", nick)
                .param("school", school))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

}
