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

import cn.edu.zjnu.acm.judge.support.ckfinder.ConfigurationPathBuilder;
import com.github.zhanhb.ckfinder.connector.configuration.ConfigurationFactory;
import com.github.zhanhb.ckfinder.connector.configuration.IBasePathBuilder;
import com.github.zhanhb.ckfinder.connector.configuration.IConfiguration;
import com.github.zhanhb.ckfinder.connector.utils.AccessControl;
import javax.servlet.MultipartConfigElement;
import javax.servlet.Servlet;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
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
    public IConfiguration configuration(ApplicationContext applicationContext) throws Exception {
        return new com.github.zhanhb.ckfinder.connector.configuration.Configuration(applicationContext, "/WEB-INF/config.xml");
    }

    @Bean
    public AccessControl accessControl(IConfiguration configuration) {
        return new AccessControl(configuration);
    }

    @Bean
    public ConfigurationFactory configurationFactory(IConfiguration configuration) {
        return new ConfigurationFactory(configuration);
    }

    @Bean
    public IBasePathBuilder pathBuilder(JudgeConfiguration judgeConfiguration) {
        return new ConfigurationPathBuilder(judgeConfiguration);
    }

    @Bean
    public ServletRegistrationBean connectorServlet(MultipartConfigElement multipartConfigElement,
            ConfigurationFactory configurationFactory) {
        Servlet servlet = new ConnectorServlet(configurationFactory);
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(servlet, "/ckfinder/core/connector/java/connector.java");
        servletRegistrationBean.setMultipartConfig(multipartConfigElement);
        return servletRegistrationBean;
    }

}
