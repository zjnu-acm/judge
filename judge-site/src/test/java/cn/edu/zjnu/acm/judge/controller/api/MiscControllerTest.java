package cn.edu.zjnu.acm.judge.controller.api;

import cn.edu.zjnu.acm.judge.Application;
import cn.edu.zjnu.acm.judge.data.dto.ValueHolder;
import cn.edu.zjnu.acm.judge.data.form.SystemInfoForm;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(JUnitPlatform.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
@WithMockUser(roles = "ADMIN")
public class MiscControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test of fix method, of class MiscController.
     *
     * {@link MiscController#fix()}
     */
    @Test
    public void testFix() throws Exception {
        log.info("fix");
        MvcResult result = mvc.perform(post("/api/misc/fix?_format=json"))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    /**
     * Test of setSystemInfo method, of class MiscController.
     *
     * {@link MiscController#setSystemInfo(SystemInfoForm)}
     */
    @Test
    public void testSetSystemInfo() throws Exception {
        log.info("setSystemInfo");
        SystemInfoForm request = new SystemInfoForm("test", false);
        MvcResult result = mvc.perform(put("/api/misc/systemInfo?_format=json")
                .content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    /**
     * Test of systemInfo method, of class MiscController.
     *
     * {@link MiscController#systemInfo()}
     */
    @Test
    public void testSystemInfo() throws Exception {
        log.info("systemInfo");
        MvcResult result = mvc.perform(get("/api/misc/systemInfo?_format=json"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    private Long getContestOnly() throws Exception {
        JavaType type = objectMapper.getTypeFactory().constructParametricType(ValueHolder.class, Long.class);
        ValueHolder<Long> result = objectMapper.readValue(mvc.perform(get("/api/misc/contestOnly?_format=json"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString(), type);
        return result.getValue();
    }

    private void setContestOnly(Long value) throws Exception {
        ValueHolder<Long> request = new ValueHolder<>(value);
        MvcResult result = mvc.perform(put("/api/misc/contestOnly?_format=json")
                .content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    /**
     * Test of contestOnly method, of class MiscController.
     *
     * {@link MiscController#contestOnly()}
     * {@link MiscController#setContestOnly(ContestOnlyForm)}
     */
    @Test
    public void testContestOnly() throws Exception {
        log.info("getContestOnly");
        Long old = getContestOnly();
        try {
            setContestOnly(null);
            assertThat(getContestOnly()).isNull();
            request(HttpStatus.OK);

            setContestOnly(Long.MIN_VALUE);
            assertThat(getContestOnly()).isEqualTo(Long.valueOf(Long.MIN_VALUE));
            request(HttpStatus.FORBIDDEN);

            setContestOnly(Long.MIN_VALUE);
            request(HttpStatus.FORBIDDEN);
            assertThat(getContestOnly()).isEqualTo(Long.valueOf(Long.MIN_VALUE));

            setContestOnly(null);
            request(HttpStatus.OK);
            assertThat(getContestOnly()).isEqualTo(null);
        } finally {
            setContestOnly(old);
        }
    }

    private void request(HttpStatus status) throws Exception {
        mvc.perform(get("/registerpage"))
                .andExpect(status().is(status.value()));
        mvc.perform(get("/register"))
                .andExpect(status().is(status.value()));
    }

}
