package cn.edu.zjnu.acm.judge.controller;

import cn.edu.zjnu.acm.judge.Application;
import cn.edu.zjnu.acm.judge.service.SystemService;
import cn.edu.zjnu.acm.judge.util.DeleteHelper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import static cn.edu.zjnu.acm.judge.test.PlatformAssuming.assumingNotWindows;
import static cn.edu.zjnu.acm.judge.test.PlatformAssuming.assumingWindows;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(JUnitPlatform.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
public class CKFinderControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private SystemService systemService;
    private String pathName = "test.jpg";
    private Path dir;
    private Path path;
    private byte[] content;

    public void setUp() throws IOException {
        dir = Files.createDirectories(systemService.getUploadDirectory());
        path = dir.getFileSystem().getPath(dir.toString(), pathName);
        content = "Hello! But I'm not a picture!".getBytes(StandardCharsets.UTF_8);
        Path parent = path.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.write(path, content);
    }

    public void tearDown() throws IOException {
        DeleteHelper.delete(path);
    }

    /**
     * Test of legacySupport method, of class CKFinderController.
     *
     * {@link CKFinderController#legacySupport(HttpServletRequest, HttpServletResponse, String)}
     */
    @Test
    public void testLegacySupport() throws Exception {
        log.info("legacySupport");
        setUp();
        mvc.perform(get("/support/ckfinder.action").param("path", pathName))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.IMAGE_JPEG))
                .andExpect(content().bytes(content))
                .andReturn();
        mvc.perform(get("/support/ckfinder.action?path=" + pathName + "?hash-1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.IMAGE_JPEG))
                .andExpect(content().bytes(content))
                .andReturn();
        tearDown();
    }

    @Test
    public void testGetParent() throws Exception {
        pathName = "../../hello.txt";
        setUp();
        mvc.perform(get("/support/ckfinder.action?path=" + pathName))
                .andExpect(status().isNotFound())
                .andExpect(handler().handlerType(CKFinderController.class))
                .andReturn();
        tearDown();
    }

    @Test
    public void testSlashRoot() throws Exception {
        for (String s : new String[]{
            "/aj.txt",
            "//ba.txt",
            "///cc.txt"
        }) {
            pathName = s;
            setUp();
            assertTrue(path.startsWith(dir));
            mvc.perform(get("/support/ckfinder.action?path=" + pathName))
                    .andExpect(status().isOk())
                    .andExpect(handler().handlerType(CKFinderController.class))
                    .andReturn();
            tearDown();
        }
    }

    /**
     * Test of attachment method, of class CKFinderController.
     *
     * {@link CKFinderController#attachment(HttpServletRequest, HttpServletResponse, String)}
     */
    @Test
    public void testAttachment() throws Exception {
        log.info("attachment");
        setUp();
        mvc.perform(get("/userfiles/" + pathName))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.IMAGE_JPEG))
                .andExpect(content().bytes(content))
                .andReturn();
        // query handled by framework
        mvc.perform(get("/userfiles/" + pathName + "?hash=1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.IMAGE_JPEG))
                .andExpect(content().bytes(content))
                .andReturn();
        for (String uri : new String[]{
            "/userfiles/images",
            "/userfiles/images/",
            "/userfiles/images/sample.png",
            "/userfiles/images/folder/sample.png",
            "/userfiles/images/f1/f2/sample.png"
        }) {
            mvc.perform(get(uri))
                    .andExpect(handler().handlerType(CKFinderController.class));
        }
        tearDown();
    }

    @Test
    public void testDotDirectory() throws Exception {
        pathName = ".git/head";
        setUp();
        mvc.perform(get("/userfiles/" + pathName))
                .andExpect(status().isNotFound())
                .andExpect(handler().handlerType(CKFinderController.class))
                .andReturn();
        tearDown();
    }

    @Test
    @SuppressWarnings("ThrowableResultIgnored")
    public void testIaeOnWindows() throws Exception {
        assumingWindows();
        pathName = "::::::";
        assertThrows(IllegalArgumentException.class, this::setUp);
        mvc.perform(get("/userfiles/" + pathName))
                .andExpect(status().isNotFound())
                .andExpect(handler().handlerType(CKFinderController.class))
                .andReturn();
    }

    @Test
    public void testColonOnUnix() throws Exception {
        assumingNotWindows();
        pathName = "::::::";
        setUp();
        mvc.perform(get("/userfiles/" + pathName))
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(CKFinderController.class))
                .andReturn();
        tearDown();
    }

    private MvcResult testConfigJsBase(String requestLang, String expect) throws Exception {
        MockHttpServletRequestBuilder builder = get("/webjars/ckfinder/2.6.2.1/config.js");
        if (requestLang != null) {
            builder.param("lang", requestLang);
        }
        return mvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/javascript"))
                .andExpect(content().string(containsString(expect)))
                .andReturn();
    }

    /**
     * Test of configJs method, of class CKFinderController.
     *
     * {@link CKFinderController#configJs(Locale)}
     */
    @Test
    public void testConfigJs() throws Exception {
        log.info("configJs");
        MvcResult result = testConfigJsBase("pt-BR", "pt-br");
        result = testConfigJsBase("zh", "zh-cn");
        result = testConfigJsBase("zh-TW-x-lvariant-Hant", "zh-tw");
        result = testConfigJsBase(null, "en");
        result = testConfigJsBase("", "en");
    }

}
