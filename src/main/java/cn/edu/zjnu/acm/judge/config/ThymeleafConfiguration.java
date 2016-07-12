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

import java.util.Collections;
import java.util.Set;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.model.IText;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.processor.text.AbstractTextProcessor;
import org.thymeleaf.processor.text.ITextStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

@Configuration
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
                        if (!StringUtils.hasText(content)) {
                            structureHandler.removeText();
                        }
                    }
                });
            }
        };
    }

}
