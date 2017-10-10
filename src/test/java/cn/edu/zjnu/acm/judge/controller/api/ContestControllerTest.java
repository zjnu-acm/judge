package cn.edu.zjnu.acm.judge.controller.api;

import cn.edu.zjnu.acm.judge.Application;
import cn.edu.zjnu.acm.judge.data.form.ContestForm;
import cn.edu.zjnu.acm.judge.domain.Contest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Locale;
import java.util.Objects;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
public class ContestControllerTest {

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
     * Test of save method, of class ContestController.
     * {@link ContestController#save(Contest)}
     */
    @Test
    public void testSave() throws Exception {
        System.out.println("save");
        Contest request = null;
        MvcResult result = mvc.perform(post("/api/contests")
                .content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    /**
     * Test of delete method, of class ContestController.
     * {@link ContestController#delete(long)}
     */
    @Test
    public void testDelete() throws Exception {
        System.out.println("delete");
        long id = 0;
        MvcResult result = mvc.perform(delete("/api/contests/{id}", id))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    /**
     * Test of list method, of class ContestController.
     * {@link ContestController#list(ContestForm)}
     */
    @Test
    public void testList() throws Exception {
        System.out.println("list");
        boolean includeDisabled = false;
        String[] exclude = null;
        String[] include = null;
        MvcResult result = mvc.perform(get("/api/contests")
                .param("includeDisabled", Boolean.toString(includeDisabled))
                .param("exclude", Objects.toString(exclude, ""))
                .param("include", Objects.toString(include, "")))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    /**
     * Test of findOne method, of class ContestController.
     * {@link ContestController#findOne(long, Locale)}
     */
    @Test
    public void testFindOne() throws Exception {
        System.out.println("findOne");
        long id = 0;
        Locale locale = Locale.getDefault();
        MvcResult result = mvc.perform(get("/api/contests/{id}", id)
                .locale(locale))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    /**
     * Test of update method, of class ContestController.
     * {@link ContestController#update(long, Contest)}
     */
    @Test
    public void testUpdate() throws Exception {
        System.out.println("update");
        long id = 0;
        Contest request = null;
        MvcResult result = mvc.perform(patch("/api/contests/{id}", id)
                .content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    /**
     * Test of submittedProblems method, of class ContestController.
     * {@link ContestController#submittedProblems(long)}
     */
    @Test
    public void testSubmittedProblems() throws Exception {
        System.out.println("submittedProblems");
        long id = 0;
        MvcResult result = mvc.perform(get("/api/contests/{id}/problems/submitted", id))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    /**
     * Test of standing method, of class ContestController.
     * {@link ContestController#standing(long)}
     */
    @Test
    public void testStanding() throws Exception {
        System.out.println("standing");
        long id = 0;
        MvcResult result = mvc.perform(get("/api/contests/{id}/standing", id))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

}
