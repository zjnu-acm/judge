package cn.edu.zjnu.acm.judge.controller;

import cn.edu.zjnu.acm.judge.Application;
import cn.edu.zjnu.acm.judge.config.JudgeConfiguration;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
public class CKFinderControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private JudgeConfiguration judgeConfiguration;

    /**
     * Test of ckfinder method, of class CKFinderController.
     *
     * @see CKFinderController#ckfinder(String)
     */
    @Test
    public void testCkfinder() throws Exception {
        log.info("ckfinder");
        Path dir = Files.createDirectories(judgeConfiguration.getUploadDirectory());
        String pathName = "test.jpg";
        Path path = dir.resolve(pathName);
        byte[] content = "test content".getBytes(StandardCharsets.UTF_8);
        Files.write(path, content);
        try {
            MvcResult result = mvc.perform(get("/support/ckfinder").param("path", pathName))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.IMAGE_JPEG))
                    .andExpect(content().bytes(content))
                    .andReturn();
        } finally {
            Files.delete(path);
        }
    }

}
