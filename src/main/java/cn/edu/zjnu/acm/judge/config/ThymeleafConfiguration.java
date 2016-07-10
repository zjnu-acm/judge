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
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.Set;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MimeType;
import org.springframework.util.StringUtils;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.model.IText;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.processor.text.AbstractTextProcessor;
import org.thymeleaf.processor.text.ITextStructureHandler;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
@EnableConfigurationProperties(ThymeleafProperties.class)
public class ThymeleafConfiguration {

    @Bean
    public SpringTemplateEngine templateEngine(Collection<ITemplateResolver> templateResolvers,
            ObjectProvider<Collection<IDialect>> dialectsProvider) {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        for (ITemplateResolver templateResolver : templateResolvers) {
            engine.addTemplateResolver(templateResolver);
        }
        Collection<IDialect> dialects = dialectsProvider.getIfAvailable();
        if (!CollectionUtils.isEmpty(dialects)) {
            for (IDialect dialect : dialects) {
                engine.addDialect(dialect);
            }
        }
        return engine;
    }

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
                        if (!StringUtils.hasText(content)) {
                            structureHandler.removeText();
                        }
                    }
                });
            }
        };
    }

    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }

    @Bean
    @ConditionalOnWebApplication
    public ThymeleafViewResolver thymeleafViewResolver(ThymeleafProperties properties, SpringTemplateEngine templateEngine) {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine);
        resolver.setCharacterEncoding(properties.getEncoding().name());
        resolver.setContentType(appendCharset(properties.getContentType(),
                resolver.getCharacterEncoding()));
        resolver.setExcludedViewNames(properties.getExcludedViewNames());
        resolver.setViewNames(properties.getViewNames());
        // This resolver acts as a fallback resolver (e.g. like a
        // InternalResourceViewResolver) so it needs to have low precedence
        resolver.setOrder(Ordered.LOWEST_PRECEDENCE - 5);
        resolver.setCache(properties.isCache());
        return resolver;
    }

    private String appendCharset(MimeType type, String charset) {
        if (type.getCharset() != null) {
            return type.toString();
        }
        LinkedHashMap<String, String> parameters = new LinkedHashMap<>(type.getParameters().size() + 1);
        parameters.put("charset", charset);
        parameters.putAll(type.getParameters());
        return new MimeType(type, parameters).toString();
    }

    @Bean
    public ITemplateResolver defaultTemplateResolver(ThymeleafProperties properties, ApplicationContext applicationContext) {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(applicationContext);
        resolver.setPrefix(properties.getPrefix());
        resolver.setSuffix(properties.getSuffix());
        resolver.setTemplateMode(properties.getMode());
        Optional.ofNullable(properties.getEncoding())
                .map(Charset::name)
                .ifPresent(resolver::setCharacterEncoding);
        resolver.setCacheable(properties.isCache());
        Optional.ofNullable(properties.getTemplateResolverOrder())
                .ifPresent(resolver::setOrder);
        return resolver;
    }

}
