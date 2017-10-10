package cn.edu.zjnu.acm.judge.controller;

import cn.edu.zjnu.acm.judge.Application;
import javax.servlet.http.HttpServletRequest;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
public class MainControllerTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = webAppContextSetup(context).build();
    }

    /**
     * Test of index method, of class MainController.
     *
     * @see MainController#index()
     */
    @Test
    public void testIndex() throws Exception {
        log.info("index");
        MvcResult result = mvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
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
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
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
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
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
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
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
        MvcResult result = mvc.perform(get("/unauthorized"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
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
        MvcResult result = mvc.perform(get("/unauthorized"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

}
