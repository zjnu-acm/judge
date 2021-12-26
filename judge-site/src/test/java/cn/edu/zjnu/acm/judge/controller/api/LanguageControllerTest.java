package cn.edu.zjnu.acm.judge.controller.api;

import cn.edu.zjnu.acm.judge.Application;
import cn.edu.zjnu.acm.judge.domain.Language;
import cn.edu.zjnu.acm.judge.service.MockDataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
public class LanguageControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockDataService mockDataService;

    /**
     * Test of findAll method, of class LanguageController.
     *
     * {@link LanguageController#findAll()}
     */
    @Test
    public void testFindAll() throws Exception {
        log.info("findAll");
        MvcResult result = mvc.perform(get("/api/languages?_format=json"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    /**
     * Test of save method, of class LanguageController.
     *
     * {@link LanguageController#save(Language)}
     */
    @Test
    public void testSave() throws Exception {
        log.info("save");
        Language language = Language.builder()
                .name("mock language")
                .sourceExtension("tmp")
                .executableExtension("dummy")
                .description("test description")
                .build();
        MvcResult result = mvc.perform(post("/api/languages?_format=json")
                .content(objectMapper.writeValueAsString(language)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    private int anyId() {
        return mockDataService.anyLanguage().getId();
    }

    /**
     * Test of findOne method, of class LanguageController.
     *
     * {@link LanguageController#findOne(long)}
     */
    @Test
    public void testFindOne() throws Exception {
        log.info("findOne");
        long id = anyId();
        MvcResult result = mvc.perform(get("/api/languages/{id}?_format=json", id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    /**
     * Test of update method, of class LanguageController.
     *
     * {@link LanguageController#update(long, Language)}
     */
    @Test
    public void testUpdate() throws Exception {
        log.info("update");
        long id = anyId();
        Language request = Language.builder()
                .name("mock language")
                .sourceExtension("tmp")
                .executableExtension("dummy")
                .build();
        MvcResult result = mvc.perform(put("/api/languages/{id}?_format=json", id)
                .content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    /**
     * Test of delete method, of class LanguageController.
     *
     * {@link LanguageController#delete(long)}
     */
    @Test
    public void testDelete() throws Exception {
        log.info("delete");
        long id = anyId();
        MvcResult result = mvc.perform(delete("/api/languages/{id}?_format=json", id))
                .andExpect(status().isNoContent())
                .andReturn();
    }

}
