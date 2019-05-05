package cn.edu.zjnu.acm.judge.controller;

import cn.edu.zjnu.acm.judge.Application;
import cn.edu.zjnu.acm.judge.service.SystemService;
import cn.edu.zjnu.acm.judge.util.DeleteHelper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
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
    private SystemService systemService;
    private Path path;
    private String pathName;
    private byte[] content;

    @Before
    public void setUp() throws IOException {
        Path dir = Files.createDirectories(systemService.getUploadDirectory());
        pathName = "test.jpg";
        path = dir.resolve(pathName);
        content = "Hello! But I'm not a picture!".getBytes(StandardCharsets.UTF_8);
        Files.write(path, content);
    }

    @After
    public void tearDown() throws IOException {
        DeleteHelper.delete(path);
    }

    /**
     * Test of legacySupport method, of class CKFinderController.
     *
     * @see CKFinderController#legacySupport(HttpServletRequest,
     * HttpServletResponse, String)
     */
    @Test
    public void testLegacySupport() throws Exception {
        log.info("legacySupport");
        mvc.perform(get("/support/ckfinder.action").param("path", pathName))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.IMAGE_JPEG))
                .andExpect(content().bytes(content))
                .andReturn();
    }

    /**
     * Test of attachment method, of class CKFinderController.
     *
     * @see CKFinderController#attachment(HttpServletRequest,
     * HttpServletResponse, String)
     */
    @Test
    public void testAttachment() throws Exception {
        log.info("attachment");
        mvc.perform(get("/userfiles/" + pathName))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.IMAGE_JPEG))
                .andExpect(content().bytes(content))
                .andReturn();
        for (String uri : new String[]{
            "/userfiles/images",
            "/userfiles/images/",
            "/userfiles/images/sample.png",
            "/userfiles/images/folder/sample.png"
        }) {
            mvc.perform(get(uri))
                    .andExpect(handler().handlerType(CKFinderController.class));
        }
    }

}
