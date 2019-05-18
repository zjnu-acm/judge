package cn.edu.zjnu.acm.judge.controller;

import cn.edu.zjnu.acm.judge.Application;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@AutoConfigureMockMvc
@RunWith(JUnitPlatform.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
public class MainControllerTest {

    @Autowired
    private MockMvc mvc;

    /**
     * Test of index method, of class MainController.
     *
     * {@link MainController#index(Model)}
     */
    @Test
    public void testIndex() throws Exception {
        log.info("index");
        MvcResult result = mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andReturn();
    }

    /**
     * Test of faq method, of class MainController.
     *
     * {@link MainController#faq()}
     */
    @Test
    public void testFaq() throws Exception {
        log.info("faq");
        MvcResult result = mvc.perform(get("/faq"))
                .andExpect(status().isOk())
                .andExpect(view().name("faq"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn();
    }

    /**
     * Test of findPassword method, of class MainController.
     *
     * {@link MainController#findPassword()}
     */
    @Test
    public void testFindPassword() throws Exception {
        log.info("findPassword");
        MvcResult result = mvc.perform(get("/findpassword"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn();
    }

    /**
     * Test of registerPage method, of class MainController.
     *
     * {@link MainController#registerPage()}
     */
    @Test
    public void testRegisterPage() throws Exception {
        log.info("registerPage");
        MvcResult result = mvc.perform(get("/registerpage"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn();
    }

}
