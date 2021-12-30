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

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import nz.net.ultraq.thymeleaf.layoutdialect.decorators.strategies.GroupingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.dialect.AbstractProcessorDialect;

/**
 * @author zhanhb
 */
@Configuration
public class ThymeleafConfiguration {

    @Bean
    public AbstractProcessorDialect whiteSpaceNormalizedDialect() {
        return new SpacesDialect();
    }

    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect(new GroupingStrategy());
    }

}
