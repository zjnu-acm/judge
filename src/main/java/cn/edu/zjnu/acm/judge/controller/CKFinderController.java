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
package cn.edu.zjnu.acm.judge.controller;

import cn.edu.zjnu.acm.judge.config.JudgeConfiguration;
import com.github.zhanhb.download.spring.ToDownload;
import java.io.IOException;
import java.nio.file.Path;
import javax.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class CKFinderController {

    @Autowired
    private JudgeConfiguration judgeConfiguration;

    @Nullable
    @ToDownload
    @GetMapping("/support/ckfinder")
    public Resource ckfinder(@RequestParam("path") String path) throws IOException {
        try {
            int indexOf = path.indexOf('?');
            Path parent = judgeConfiguration.getUploadDirectory();
            Path imagePath = parent.resolve(indexOf > 0 ? path.substring(0, indexOf) : path).normalize();
            if (!imagePath.startsWith(parent)) {
                log.debug("absolute path parent='{}' path='{}'", parent, imagePath);
                return null;
            }
            return new PathResource(imagePath);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

}
