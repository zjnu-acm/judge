package cn.edu.zjnu.acm.judge.controller;

import cn.edu.zjnu.acm.judge.Application;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
@WithMockUser(roles = "ADMIN")
public class ManageControllerTest {

    @Autowired
    private MockMvc mvc;

    /**
     * Test of manage method, of class ManageController.
     *
     * @see ManageController#manage()
     */
    @Test
    public void testManage() throws Exception {
        log.info("manage");
        MvcResult result = mvc.perform(get("/admin/"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn();
    }

    /**
     * Test of index method, of class ManageController.
     *
     * @see ManageController#index(RedirectAttributes, Map)
     */
    @Test
    public void testIndex() throws Exception {
        log.info("index");
        MvcResult result = mvc.perform(get("/admin").param("testKey", "testValue"))
                .andExpect(redirectedUrl("/admin/?testKey=testValue"))
                .andReturn();
    }

}
