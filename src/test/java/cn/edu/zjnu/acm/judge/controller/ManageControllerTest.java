package cn.edu.zjnu.acm.judge.controller;

import cn.edu.zjnu.acm.judge.Application;
import java.util.Map;
import java.util.Objects;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
public class ManageControllerTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = webAppContextSetup(context).build();
    }

    /**
     * Test of manage method, of class ManageController.
     * {@link ManageController#manage()}
     */
    @Test
    public void testManage() throws Exception {
        System.out.println("manage");
        MvcResult result = mvc.perform(get("/admin/"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    /**
     * Test of index method, of class ManageController.
     * {@link ManageController#index(RedirectAttributes, Map)}
     */
    @Test
    public void testIndex() throws Exception {
        System.out.println("index");
        Map arg1 = null;
        MvcResult result = mvc.perform(get("/admin").param("arg1", Objects.toString(arg1, "")))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

}
