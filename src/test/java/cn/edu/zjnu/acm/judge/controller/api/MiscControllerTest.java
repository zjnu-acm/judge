package cn.edu.zjnu.acm.judge.controller.api;

import cn.edu.zjnu.acm.judge.Application;
import cn.edu.zjnu.acm.judge.data.form.ContestOnlyForm;
import cn.edu.zjnu.acm.judge.data.form.SystemInfoForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
public class MiscControllerTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        mvc = webAppContextSetup(context).build();
    }

    /**
     * Test of fix method, of class MiscController.
     * {@link MiscController#fix()}
     */
    @Test
    public void testFix() throws Exception {
        System.out.println("fix");
        MvcResult result = mvc.perform(post("/api/misc/fix"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    /**
     * Test of setSystemInfo method, of class MiscController.
     * {@link MiscController#setSystemInfo(SystemInfoForm)}
     */
    @Test
    public void testSetSystemInfo() throws Exception {
        System.out.println("setSystemInfo");
        SystemInfoForm request = null;
        MvcResult result = mvc.perform(put("/api/misc/systemInfo")
                .content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    /**
     * Test of systemInfo method, of class MiscController.
     * {@link MiscController#systemInfo()}
     */
    @Test
    public void testSystemInfo() throws Exception {
        System.out.println("systemInfo");
        MvcResult result = mvc.perform(get("/api/misc/systemInfo"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    /**
     * Test of contestOnly method, of class MiscController.
     * {@link MiscController#contestOnly()}
     */
    @Test
    public void testContestOnly() throws Exception {
        System.out.println("contestOnly");
        MvcResult result = mvc.perform(get("/api/misc/contestOnly"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    /**
     * Test of setContestOnly method, of class MiscController.
     * {@link MiscController#setContestOnly(ContestOnlyForm)}
     */
    @Test
    public void testSetContestOnly() throws Exception {
        System.out.println("setContestOnly");
        ContestOnlyForm request = null;
        MvcResult result = mvc.perform(put("/api/misc/contestOnly")
                .content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

}
