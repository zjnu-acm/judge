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

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.Set;
import javax.servlet.Servlet;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.util.MimeType;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.model.IText;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.processor.text.AbstractTextProcessor;
import org.thymeleaf.processor.text.ITextStructureHandler;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

@AutoConfigureAfter({WebMvcAutoConfiguration.class})
@AutoConfigureBefore(ThymeleafAutoConfiguration.class)
@Configuration
@ConditionalOnClass({SpringTemplateEngine.class})
@EnableConfigurationProperties({ThymeleafProperties.class})  //no sense rolling our own.
@SuppressWarnings({"ProtectedInnerClass", "PublicInnerClass"})
public class ThymeleafConfiguration {

    @Bean
    public AbstractProcessorDialect whiteSpaceNormalizedDialect() {
        TemplateMode templateMode = TemplateMode.HTML;
        int processorPrecedence = 100000;
        String dialectName = "spaces";
        String dialectPrefix = dialectName;
        return new AbstractProcessorDialect(dialectName, dialectPrefix, processorPrecedence) {
            @Override
            public Set<IProcessor> getProcessors(String dialectPrefix) {
                return Collections.singleton(new AbstractTextProcessor(templateMode, processorPrecedence) {
                    @Override
                    public void doProcess(ITemplateContext context, IText text, ITextStructureHandler structureHandler) {
                        String content = text.getText();
                        if (StringUtils.isBlank(content)) {
                            structureHandler.removeText();
                        }
                    }
                });
            }
        };
    }

    @Configuration
    @ConditionalOnClass({Servlet.class})
    @ConditionalOnWebApplication
    protected static class ThymeleafViewResolverConfiguration {

        @Autowired
        private ThymeleafProperties properties;

        @Autowired
        private SpringTemplateEngine templateEngine;

        @Bean
        public ThymeleafViewResolver thymeleafViewResolver() {
            ThymeleafViewResolver resolver = new ThymeleafViewResolver();
            resolver.setTemplateEngine(this.templateEngine);
            resolver.setCharacterEncoding(this.properties.getEncoding().name());
            resolver.setContentType(appendCharset(this.properties.getContentType(),
                    resolver.getCharacterEncoding()));
            resolver.setExcludedViewNames(this.properties.getExcludedViewNames());
            resolver.setViewNames(this.properties.getViewNames());
            // This resolver acts as a fallback resolver (e.g. like a
            // InternalResourceViewResolver) so it needs to have low precedence
            resolver.setOrder(Ordered.LOWEST_PRECEDENCE - 5);
            resolver.setCache(this.properties.isCache());
            return resolver;
        }

        private String appendCharset(MimeType type, String charset) {
            if (type.getCharSet() != null) {
                return type.toString();
            }
            LinkedHashMap<String, String> parameters = new LinkedHashMap<>(type.getParameters().size() + 1);
            parameters.put("charset", charset);
            parameters.putAll(type.getParameters());
            return new MimeType(type, parameters).toString();
        }
    }

    @Configuration
    public static class DefaultTemplateResolverConfiguration {

        @Autowired
        private ThymeleafProperties properties;

        @Bean
        public SpringResourceTemplateResolver defaultTemplateResolver() {
            SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
            // resolver.setResourceResolver(thymeleafResourceResolver());
            resolver.setPrefix(this.properties.getPrefix());
            resolver.setSuffix(this.properties.getSuffix());
            resolver.setTemplateMode(this.properties.getMode());
            Optional.ofNullable(this.properties.getEncoding())
                    .map(Charset::name)
                    .ifPresent(resolver::setCharacterEncoding);
            resolver.setCacheable(this.properties.isCache());
            Optional.ofNullable(this.properties.getTemplateResolverOrder())
                    .ifPresent(resolver::setOrder);
            return resolver;
        }

    }

}
