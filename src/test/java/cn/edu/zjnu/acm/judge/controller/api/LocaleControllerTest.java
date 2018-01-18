package cn.edu.zjnu.acm.judge.controller.api;

import cn.edu.zjnu.acm.judge.Application;
import java.util.Locale;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
@WithMockUser(roles = "ADMIN")
public class LocaleControllerTest {

    @Autowired
    private MockMvc mvc;

    /**
     * Test of current method, of class LocaleController.
     *
     * @see LocaleController#current(Locale)
     */
    @Test
    public void testCurrent() throws Exception {
        log.info("current");
        Locale locale = Locale.getDefault();
        MvcResult result = mvc.perform(get("/api/locales/current.json")
                .locale(locale))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    /**
     * Test of findOne method, of class LocaleController.
     *
     * @see LocaleController#findOne(String, boolean)
     */
    @Test
    public void testFindOne() throws Exception {
        log.info("findOne");
        String id = "en";
        boolean support = false;
        MvcResult result = mvc.perform(get("/api/locales/{id}.json", id)
                .param("support", Boolean.toString(support)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
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
        MvcResult result = mvc.perform(get("/api/locales.json"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
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
        MvcResult result = mvc.perform(get("/api/locales.json").param("all", Boolean.toString(all)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
    }

}
