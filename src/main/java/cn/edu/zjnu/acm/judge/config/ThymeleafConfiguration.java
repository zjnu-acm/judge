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

import java.io.IOException;
import java.io.Writer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Text;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templatemode.ITemplateModeHandler;
import org.thymeleaf.templatemode.StandardTemplateModeHandlers;
import org.thymeleaf.templatemode.TemplateModeHandler;
import org.thymeleaf.templatewriter.AbstractGeneralTemplateWriter;
import org.thymeleaf.util.StringUtils;

/**
 * Hacking Thymeleaf to minimize white space.
 *
 * @author zhanhb
 * @see cn.edu.zjnu.acm.judge.config.WhiteSpaceNormalizedTemplateWriter
 * @see
 * <a href="https://distigme.wordpress.com/2012/10/11/hacking-thymeleaf-to-minimize-white-space/">Hacking
 * Thymeleaf to minimize white space</a>
 */
@Configuration
public class ThymeleafConfiguration {

    @Autowired
    public void addTemplateModeHandlers(SpringTemplateEngine templateEngine) {
        ITemplateModeHandler html5 = StandardTemplateModeHandlers.HTML5;
        templateEngine.addTemplateModeHandler(new TemplateModeHandler(html5.getTemplateModeName(),
                html5.getTemplateParser(),
                new WhiteSpaceNormalizedTemplateWriter()));
    }

    private static class WhiteSpaceNormalizedTemplateWriter extends AbstractGeneralTemplateWriter {

        @Override
        protected boolean shouldWriteXmlDeclaration() {
            return false;
        }

        @Override
        protected boolean useXhtmlTagMinimizationRules() {
            return true;
        }

        @Override
        protected void writeText(final Arguments arguments, final Writer writer, final Text text)
                throws IOException {
            final String content = text.getEscapedContent();
            if (!StringUtils.isEmptyOrWhitespace(content)) {
                writer.write(content);
            }
        }

    }

}
