package cn.edu.zjnu.acm.judge.controller.api;

import cn.edu.zjnu.acm.judge.Application;
import cn.edu.zjnu.acm.judge.data.form.ContestOnlyForm;
import cn.edu.zjnu.acm.judge.data.form.SystemInfoForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
@WithMockUser(roles = "ADMIN")
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
     *
     * @see MiscController#fix()
     */
    @Test
    public void testFix() throws Exception {
        log.info("fix");
        MvcResult result = mvc.perform(post("/api/misc/fix.json"))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }

    /**
     * Test of setSystemInfo method, of class MiscController.
     *
     * @see MiscController#setSystemInfo(SystemInfoForm)
     */
    @Test
    public void testSetSystemInfo() throws Exception {
        log.info("setSystemInfo");
        SystemInfoForm request = new SystemInfoForm("test", false);
        MvcResult result = mvc.perform(put("/api/misc/systemInfo.json")
                .content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }

    /**
     * Test of systemInfo method, of class MiscController.
     *
     * @see MiscController#systemInfo()
     */
    @Test
    public void testSystemInfo() throws Exception {
        log.info("systemInfo");
        MvcResult result = mvc.perform(get("/api/misc/systemInfo.json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    private Long getContestOnly() throws Exception {
        return objectMapper.readValue(mvc.perform(get("/api/misc/contestOnly.json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString(), ContestOnlyForm.class).getValue();
    }

    private void setContestOnly(Long value) throws Exception {
        ContestOnlyForm request = ContestOnlyForm.builder().value(value).build();
        MvcResult result = mvc.perform(put("/api/misc/contestOnly.json")
                .content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }

    /**
     * Test of contestOnly method, of class MiscController.
     *
     * @see MiscController#contestOnly()
     * @see MiscController#setContestOnly(ContestOnlyForm)
     */
    @Test
    public void testContestOnly() throws Exception {
        log.info("getContestOnly");
        Long old = getContestOnly();
        try {
            setContestOnly(null);
            request(HttpStatus.OK);

            setContestOnly(Long.MIN_VALUE);
            assertEquals(Long.valueOf(Long.MIN_VALUE), getContestOnly());
            request(HttpStatus.BAD_REQUEST);

            setContestOnly(Long.MIN_VALUE);
            request(HttpStatus.BAD_REQUEST);
            assertEquals(Long.valueOf(Long.MIN_VALUE), getContestOnly());

            setContestOnly(null);
            request(HttpStatus.OK);
            assertEquals(null, getContestOnly());
        } finally {
            setContestOnly(old);
        }
    }

    private void request(HttpStatus status) throws Exception {
        mvc.perform(get("/registerpage"))
                .andDo(print())
                .andExpect(status().is(status.value()));
        mvc.perform(get("/register"))
                .andDo(print())
                .andExpect(status().is(status.value()));
    }

}
