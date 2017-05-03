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

import cn.edu.zjnu.acm.judge.util.SpecialCall;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

@Service
@Slf4j
public class JudgeConfiguration {

    public static final String VALIDATE_FILE_NAME = "compare.exe";

    private static final String SERVER_CONFIG_PROPERTIES = "serverConfig";
    private static final String WEB_PROPERTIES = "web";
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle(SERVER_CONFIG_PROPERTIES, Locale.US);

    private Path uploadDirectory;

    private Path dataFilesPath;
    private volatile String systemInfo;
    private String contextPath;
    private Path workingPath;
    private boolean deleteTempFile;

    @Autowired
    private ServletContext servletContext;

    public Path getUploadDirectory() {
        return uploadDirectory;
    }

    public String getValue(String key) {
        String property = BUNDLE.getString(key);
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

    @SpecialCall("notice.html")
    public String getSystemInfo() {
        return systemInfo;
    }

    public void setSystemInfo(String aSystemInfo) {
        systemInfo = aSystemInfo;
    }

    @PostConstruct
    public void init() {
        {
            ResourceBundle bundle = ResourceBundle.getBundle(WEB_PROPERTIES, Locale.US);
            bundle.keySet().forEach(key -> servletContext.setAttribute(key, bundle.getObject(key)));
        }

        contextPath = fixContextPath(servletContext.getContextPath());
        dataFilesPath = Paths.get(getValue("DataFilesPath"));
        workingPath = Paths.get(getValue("WorkingPath"));
        uploadDirectory = Paths.get(getValue("UploadPath"));
        deleteTempFile = Boolean.parseBoolean(getValue("DeleteTempFile"));
    }

}
