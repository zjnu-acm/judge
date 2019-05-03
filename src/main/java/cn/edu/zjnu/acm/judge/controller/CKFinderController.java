/*
 * Copyright 2014-2019 zhanhb.
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
package cn.edu.zjnu.acm.judge.controller;

import cn.edu.zjnu.acm.judge.service.SystemService;
import com.github.zhanhb.ckfinder.connector.api.BasePathBuilder;
import com.github.zhanhb.ckfinder.connector.support.DefaultPathBuilder;
import com.github.zhanhb.download.spring.ToDownload;
import java.nio.file.Path;
import javax.annotation.Nullable;
import javax.servlet.ServletContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CKFinderController {

    private final SystemService systemService;
    private final ApplicationContext applicationContext;

    @Bean
    public BasePathBuilder pathBuilder() {
        String url = applicationContext.getBean(ServletContext.class).getContextPath().concat("/support/ckfinder.action?path=");
        Path path = systemService.getUploadDirectory();
        return DefaultPathBuilder.builder().baseUrl(url)
                .basePath(path).build();
    }

    @Nullable
    @ToDownload
    @GetMapping("/support/ckfinder")
    public Path ckfinder(@RequestParam("path") String path) {
        log.info(path);
        try {
            int indexOf = path.indexOf('?');
            Path parent = systemService.getUploadDirectory();
            Path imagePath = parent.getFileSystem().getPath(parent.toString(), indexOf > 0 ? path.substring(0, indexOf) : path).normalize();
            if (!imagePath.startsWith(parent)) {
                log.debug("absolute path parent='{}' path='{}'", parent, imagePath);
                return null;
            }
            return imagePath;
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

}
