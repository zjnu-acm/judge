package cn.edu.zjnu.acm.judge.controller.api;

import cn.edu.zjnu.acm.judge.Application;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
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
@WithMockUser(roles = "ADMIN")
public class LocaleControllerTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = webAppContextSetup(context).build();
    }

    /**
     * Test of current method, of class LocaleController.
     *
     * @see LocaleController#current(Locale)
     */
    @Test
    public void testCurrent() throws Exception {
        log.info("current");
        Locale locale = Locale.getDefault();
        MvcResult result = mvc.perform(get("/api/locales/current")
                .locale(locale))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    /**
     * Test of findOne method, of class LocaleController.
     *
     * @see LocaleController#findOne(String, String)
     */
    @Test
    public void testFindOne() throws Exception {
        log.info("findOne");
        String id = "";
        String support = "";
        MvcResult result = mvc.perform(get("/api/locales/{id}", id).param("support", support))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    /**
     * Test of findAll method, of class LocaleController.
     *
     * @see LocaleController#findAll()
     */
    @Test
    public void testFindAll() throws Exception {
        log.info("findAll");
        MvcResult result = mvc.perform(get("/api/locales"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    /**
     * Test of supported method, of class LocaleController.
     *
     * @see LocaleController#supported(boolean)
     */
    @Test
    public void testSupported() throws Exception {
        log.info("supported");
        boolean all = false;
        MvcResult result = mvc.perform(get("/api/locales").param("all", Boolean.toString(all)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

}
