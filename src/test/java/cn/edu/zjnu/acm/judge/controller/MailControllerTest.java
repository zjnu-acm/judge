package cn.edu.zjnu.acm.judge.controller;

import cn.edu.zjnu.acm.judge.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
public class MailControllerTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = webAppContextSetup(context).build();
    }

    /**
     * Test of delete method, of class MailController.
     * {@link MailController#delete(long, Authentication)}
     */
    @Test
    public void testDelete() throws Exception {
        System.out.println("delete");
        long mail_id = 0;
        MvcResult result = mvc.perform(get("/deletemail").param("mail_id", Long.toString(mail_id)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    /**
     * Test of mail method, of class MailController.
     * {@link MailController#mail(Model, int, long, Authentication)}
     */
    @Test
    public void testMail() throws Exception {
        System.out.println("mail");
        int size = 0;
        long start = 0;
        MvcResult result = mvc.perform(get("/mail")
                .param("size", Integer.toString(size))
                .param("start", Long.toString(start)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    /**
     * Test of send method, of class MailController.
     * {@link MailController#send(String, String, String, Authentication)}
     */
    @Test
    public void testSend() throws Exception {
        System.out.println("send");
        String title = "";
        String to = "";
        String content = "";
        MvcResult result = mvc.perform(post("/send")
                .param("title", title)
                .param("to", to)
                .param("content", content))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    /**
     * Test of sendPage method, of class MailController.
     * {@link MailController#sendPage(Model, long, String, Authentication)}
     */
    @Test
    public void testSendPage() throws Exception {
        System.out.println("sendPage");
        long reply = 0;
        String to = "";
        MvcResult result = mvc.perform(get("/sendpage")
                .param("reply", Long.toString(reply))
                .param("to", to))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    /**
     * Test of showMail method, of class MailController.
     * {@link MailController#showMail(Model, long, Authentication)}
     */
    @Test
    public void testShowMail() throws Exception {
        System.out.println("showMail");
        long mail_id = 0;
        MvcResult result = mvc.perform(get("/showmail").param("mail_id", Long.toString(mail_id)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

}
