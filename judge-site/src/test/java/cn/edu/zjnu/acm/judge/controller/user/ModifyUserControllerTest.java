package cn.edu.zjnu.acm.judge.controller.user;

import cn.edu.zjnu.acm.judge.Application;
import cn.edu.zjnu.acm.judge.domain.User;
import cn.edu.zjnu.acm.judge.service.MockDataService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@AutoConfigureMockMvc
@RunWith(JUnitPlatform.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
public class ModifyUserControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private MockDataService mockDataService;

    /**
     * Test of updatePage method, of class ModifyUserController.
     *
     * {@link ModifyUserController#updatePage(Model)}
     */
    @Test
    public void testUpdatePage() throws Exception {
        log.info("updatePage");
        String userId = mockDataService.user().getId();
        MvcResult result = mvc.perform(get("/modifyuserpage").with(user(userId)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn();
    }

    /**
     * Test of update method, of class ModifyUserController.
     *
     * {@link ModifyUserController#update(Model, String, String, String, String, String, String)}
     */
    @Test
    public void testUpdate() throws Exception {
        log.info("update");
        User user = mockDataService.user();
        String userId = user.getId();
        String oldPassword = user.getPassword();
        String newPassword = ""; // at least 6 characters or empty
        String rptPassword = newPassword;
        String email = "";
        String nick = "";
        String school = "";
        MvcResult result = mvc.perform(post("/modifyuser").with(user(userId))
                .param("oldPassword", oldPassword)
                .param("newPassword", newPassword)
                .param("rptPassword", rptPassword)
                .param("email", email)
                .param("nick", nick)
                .param("school", school))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("users/modifySuccess"))
                .andReturn();
    }

}
