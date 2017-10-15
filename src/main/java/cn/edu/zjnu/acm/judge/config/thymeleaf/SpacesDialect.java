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
package cn.edu.zjnu.acm.judge.config.thymeleaf;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.templatemode.TemplateMode;

/**
 *
 * @author zhanhb
 */
class SpacesDialect extends AbstractProcessorDialect {

    private final TemplateMode templateMode;

    SpacesDialect(String name, String prefix, int precedence, TemplateMode templateMode) {
        super(name, prefix, precedence);
        this.templateMode = templateMode;
    }

    SpacesDialect() {
        this("spaces", "spaces", 100000, TemplateMode.HTML);
    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
        return ImmutableSet.of(
                new EmptyTextProcessor(templateMode, getDialectProcessorPrecedence()),
                new AttributesInnerWhitespacesProcessor(templateMode, getDialectProcessorPrecedence()));
    }

}
