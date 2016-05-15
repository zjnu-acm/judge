/*
 * Copyright 2014 zhanhb.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.edu.zjnu.acm.judge.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

@Service
@Slf4j
public class JudgeConfiguration {

    private static final Properties PROPS = new Properties();
    private static final String SERVER_CONFIG_PROPERTIES = "serverConfig.properties";
    private static final String WEB_PROPERTIES = "web.properties";

    public static final String VALIDATE_FILE_NAME = "compare.exe";
    private static Path uploadDirectory; // TODO

    static {
        log.info("<clinit> {}", JudgeConfiguration.class.getClassLoader());
        try (InputStream in = JudgeConfiguration.class.getClassLoader().getResourceAsStream(SERVER_CONFIG_PROPERTIES)) {
            in.getClass();
            PROPS.load(in);
        } catch (IOException ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Path getUploadDirectory() {
        return uploadDirectory;
    }

    private Path dataFilesPath;
    private volatile String systemInfo;
    private String contextPath;
    private Path workingPath;
    private boolean deleteTempFile;

    @Autowired
    private ServletContext servletContext;

    public String getValue(String key) {
        String property = PROPS.getProperty(key);
        String ctx = getContextPath();
        if (property != null && ctx != null) {
            property = property.replace("%CONTEXT_PATH%", ctx);
        }
        log.info("{}={}", key, property);
        return property;
    }

    public boolean isDeleteTempFile() {
        return deleteTempFile;
    }

    public Path getDataDirectory(long problemId) {
        return dataFilesPath.resolve(String.valueOf(problemId));
    }

    public Path getWorkDirectory(long submissionId) {
        return workingPath.resolve(String.valueOf(submissionId));
    }

    public String getContextPath() {
        return contextPath;
    }

    private String fixContextPath(String contextPath) {
        return StringUtils.isEmpty(contextPath) || contextPath.equals("/") ? "ROOT"
                : contextPath.replace("/", "");
    }

    public String getSystemInfo() {
        return systemInfo;
    }

    public void setSystemInfo(String aSystemInfo) {
        systemInfo = aSystemInfo;
    }

    @PostConstruct
    public void init() {
        try {
            {
                Properties properties = new Properties();
                try (InputStream in = getClass().getClassLoader().getResourceAsStream(WEB_PROPERTIES)) {
                    properties.load(in);
                }
                properties.forEach((key, value) -> servletContext.setAttribute((String) key, value));
            }

            contextPath = fixContextPath(servletContext.getContextPath());
            dataFilesPath = Paths.get(Objects.requireNonNull(getValue("DataFilesPath"), "Data Files Path not set."));
            workingPath = Paths.get(getValue("WorkingPath"));
            uploadDirectory = Paths.get(getValue("UploadPath"));
            deleteTempFile = Boolean.parseBoolean(getValue("DeleteTempFile"));
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

}
