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
import com.github.zhanhb.ckfinder.connector.configuration.IBasePathBuilder;
import com.github.zhanhb.ckfinder.connector.configuration.IConfiguration;
import com.github.zhanhb.ckfinder.connector.configuration.XmlConfigurationParser;
import javax.servlet.MultipartConfigElement;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
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
    public IBasePathBuilder pathBuilder(JudgeConfiguration judgeConfiguration, ServletContext servletContext) {
        return new ConfigurationPathBuilder(judgeConfiguration, servletContext);
    }

    @Bean
    public IConfiguration configuration(ApplicationContext applicationContext, IBasePathBuilder basePathBuilder) throws Exception {
        return XmlConfigurationParser.INSTANCE.parse(applicationContext, basePathBuilder, "/WEB-INF/config.xml");
    }

    @Bean
    public ServletRegistrationBean connectorServlet(MultipartConfigElement multipartConfigElement,
            IConfiguration configuration) {
        Servlet servlet = new ConnectorServlet(configuration);
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(servlet, "/ckfinder/core/connector/java/connector.java");
        servletRegistrationBean.setMultipartConfig(multipartConfigElement);
        return servletRegistrationBean;
    }

}
