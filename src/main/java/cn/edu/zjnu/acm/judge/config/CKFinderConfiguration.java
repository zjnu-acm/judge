/*
 * Copyright 2016 ZJNU ACM.
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

import com.github.zhanhb.ckfinder.connector.configuration.DefaultPathBuilder;
import com.github.zhanhb.ckfinder.connector.configuration.IBasePathBuilder;
import java.nio.file.Path;
import javax.servlet.ServletContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author zhanhb
 */
@Configuration
public class CKFinderConfiguration {

    @Bean
    public IBasePathBuilder pathBuilder(JudgeConfiguration judgeConfiguration, ApplicationContext context) {
        String url = context.getBean(ServletContext.class).getContextPath().concat("/support/ckfinder.action?path=");
        Path path = judgeConfiguration.getUploadDirectory();
        return DefaultPathBuilder.builder().baseUrl(url)
                .basePath(path).build();
    }

}
