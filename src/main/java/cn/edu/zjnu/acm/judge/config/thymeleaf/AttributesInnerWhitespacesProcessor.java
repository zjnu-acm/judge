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

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IAttribute;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.AbstractProcessor;
import org.thymeleaf.processor.element.IElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.processor.element.MatchingAttributeName;
import org.thymeleaf.processor.element.MatchingElementName;
import org.thymeleaf.templatemode.TemplateMode;

/**
 *
 * @author zhanhb
 */
class AttributesInnerWhitespacesProcessor extends AbstractProcessor implements IElementTagProcessor {

    AttributesInnerWhitespacesProcessor(TemplateMode templateMode, int precedence) {
        super(templateMode, precedence);
    }

    @Override
    public void process(ITemplateContext context, IProcessableElementTag tag, IElementTagStructureHandler structureHandler) {
        IAttribute[] attributes = tag.getAllAttributes();
        for (int i = attributes.length - 1; i >= 0; --i) {
            structureHandler.removeAttribute(attributes[i].getAttributeDefinition().getAttributeName());
        }
        for (IAttribute attribute : attributes) {
            structureHandler.replaceAttribute(attribute.getAttributeDefinition().getAttributeName(), attribute.getAttributeCompleteName(), attribute.getValue(), attribute.getValueQuotes());
        }
    }

    @Override
    public MatchingElementName getMatchingElementName() {
        return MatchingElementName.forAllElements(getTemplateMode());
    }

    @Override
    public MatchingAttributeName getMatchingAttributeName() {
        return MatchingAttributeName.forAllAttributes(getTemplateMode());
    }

}
