package cn.edu.zjnu.acm.judge.controller;

import cn.edu.zjnu.acm.judge.Application;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
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
     * @see MainController#index()
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
     * @see MainController#faq()
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
     * @see MainController#findPassword()
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
     * @see MainController#registerPage()
     */
    @Test
    public void testRegisterPage() throws Exception {
        log.info("registerPage");
        MvcResult result = mvc.perform(get("/registerpage"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn();
    }

    /**
     * Test of unauthorizedHtml method, of class MainController.
     *
     * @see MainController#unauthorizedHtml(HttpServletRequest)
     */
    @Test
    public void testUnauthorizedHtml() throws Exception {
        log.info("unauthorizedHtml");
        MvcResult result = mvc.perform(get("/unauthorized").accept(MediaType.TEXT_HTML))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login"))
                .andReturn();
    }

    /**
     * Test of unauthorized method, of class MainController.
     *
     * @see MainController#unauthorized()
     */
    @Test
    public void testUnauthorized() throws Exception {
        log.info("unauthorized");
        MvcResult result = mvc.perform(get("/unauthorized").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
    }

}
