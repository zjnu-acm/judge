package cn.edu.zjnu.acm.judge.controller.api;

import cn.edu.zjnu.acm.judge.Application;
import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.service.MockDataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Locale;
import java.util.Objects;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
@WithMockUser(roles = "ADMIN")
public class ContestControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockDataService mockDataService;

    /**
     * Test of save method, of class ContestController.
     *
     * @see ContestController#save(Contest)
     */
    @Test
    public void testSave() throws Exception {
        log.info("save");
        Contest contest = mockDataService.contest(false);
        MvcResult result = mvc.perform(post("/api/contests.json")
                .content(objectMapper.writeValueAsString(contest)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    /**
     * Test of delete method, of class ContestController.
     *
     * @see ContestController#delete(long)
     */
    @Test
    public void testDelete() throws Exception {
        log.info("delete");
        long id = mockDataService.contest().getId();
        MvcResult result = mvc.perform(delete("/api/contests/{id}.json", id))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    /**
     * Test of list method, of class ContestController.
     *
     * @see ContestController#list(ContestForm)
     */
    @Test
    public void testList() throws Exception {
        log.info("list");
        boolean includeDisabled = false;
        String[] exclude = null;
        String[] include = null;
        MvcResult result = mvc.perform(get("/api/contests.json")
                .param("includeDisabled", Boolean.toString(includeDisabled))
                .param("exclude", Objects.toString(exclude, ""))
                .param("include", Objects.toString(include, "")))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    /**
     * Test of findOne method, of class ContestController.
     *
     * @see ContestController#findOne(long, Locale)
     */
    @Test
    public void testFindOne() throws Exception {
        log.info("findOne");
        long id = mockDataService.contest().getId();
        Locale locale = Locale.getDefault();
        MvcResult result = mvc.perform(get("/api/contests/{id}.json", id)
                .locale(locale))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    /**
     * Test of update method, of class ContestController.
     *
     * @see ContestController#update(long, Contest)
     */
    @Test
    public void testUpdate() throws Exception {
        log.info("update");
        long id = mockDataService.contest().getId();
        Contest request = new Contest();
        MvcResult result = mvc.perform(patch("/api/contests/{id}.json", id)
                .content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    /**
     * Test of submittedProblems method, of class ContestController.
     *
     * @see ContestController#submittedProblems(long)
     */
    @Test
    public void testSubmittedProblems() throws Exception {
        log.info("submittedProblems");
        long id = mockDataService.contest().getId();
        MvcResult result = mvc.perform(get("/api/contests/{id}/problems/submitted.json", id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    /**
     * Test of standing method, of class ContestController.
     *
     * @see ContestController#standing(long)
     */
    @Test
    public void testStanding() throws Exception {
        log.info("standing");
        long id = mockDataService.contest().getId();
        MvcResult result = mvc.perform(get("/api/contests/{id}/standing.json", id))
                .andExpect(request().asyncStarted())
                .andReturn();
        MvcResult asyncResult = mvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
    }

}
