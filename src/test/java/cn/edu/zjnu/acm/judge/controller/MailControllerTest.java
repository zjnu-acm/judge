package cn.edu.zjnu.acm.judge.controller;

import cn.edu.zjnu.acm.judge.Application;
import cn.edu.zjnu.acm.judge.domain.Mail;
import cn.edu.zjnu.acm.judge.mapper.MailMapper;
import cn.edu.zjnu.acm.judge.service.MockDataService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
public class MailControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private MockDataService mockDataService;
    @Autowired
    private MailMapper mailMapper;

    /**
     * Test of delete method, of class MailController.
     *
     * @see MailController#delete(long, Authentication)
     */
    @Test
    public void testDelete() throws Exception {
        log.info("delete");
        String userId = mockDataService.user().getId();
        long mailId = newMail(mockDataService.user().getId(), userId).getId();
        MvcResult result = mvc.perform(get("/deletemail").with(user(userId))
                .param("mail_id", Long.toString(mailId)))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/mail"))
                .andReturn();
    }

    /**
     * Test of mail method, of class MailController.
     *
     * @see MailController#mail(Model, int, long, Authentication)
     */
    @Test
    public void testMail() throws Exception {
        log.info("mail");
        int size = 20;
        long start = 0;
        String userId = mockDataService.user().getId();
        MvcResult result = mvc.perform(get("/mail").with(user(userId))
                .param("size", Integer.toString(size))
                .param("start", Long.toString(start)))
                .andExpect(status().isOk())
                .andExpect(view().name("mails/list"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andReturn();
    }

    /**
     * Test of send method, of class MailController.
     *
     * @see MailController#send(String, String, String, Authentication)
     */
    @Test
    public void testSend() throws Exception {
        log.info("send");
        String userId = mockDataService.user().getId();
        String title = "title";
        String to = mockDataService.user().getId();
        String content = "content";
        MvcResult result = mvc.perform(post("/send").with(user(userId))
                .param("title", title)
                .param("to", to)
                .param("content", content))
                .andExpect(status().isOk())
                .andExpect(view().name("mails/sendsuccess"))
                .andReturn();
    }

    /**
     * Test of sendPage method, of class MailController.
     *
     * @see MailController#sendPage(Model, long, String, Authentication)
     */
    @Test
    public void testSendPage() throws Exception {
        log.info("sendPage");
        long reply = -1;
        String to = mockDataService.user().getId();
        String userId = mockDataService.user().getId();
        MvcResult result = mvc.perform(get("/sendpage").with(user(userId))
                .param("reply", Long.toString(reply))
                .param("to", to))
                .andExpect(status().isOk())
                .andExpect(view().name("mails/sendpage"))
                .andReturn();
    }

    /**
     * Test of showMail method, of class MailController.
     *
     * @see MailController#showMail(Model, long, Authentication)
     */
    @Test
    public void testShowMail() throws Exception {
        log.info("showMail");
        String userId = mockDataService.user().getId();
        long mailId = newMail(mockDataService.user().getId(), userId).getId();
        MvcResult result = mvc.perform(get("/showmail").with(user(userId))
                .param("mail_id", Long.toString(mailId)))
                .andExpect(status().isOk())
                .andExpect(view().name("mails/view"))
                .andReturn();
    }

    private Mail newMail(String from, String to) {
        Mail mail = Mail.builder()
                .from(from)
                .to(to)
                .title("title5")
                .content("content12")
                .build();
        mailMapper.save(mail);
        return mail;
    }

}
