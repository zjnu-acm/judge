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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.TreeSet;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author zhanhb
 */
@Configuration
public class LocaleFactory {

    private final Collection<String> allLanguages;

    public LocaleFactory() {
        Collection<String> set = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        set.addAll(Arrays.asList("en", "zh"));
        this.allLanguages = set;
    }

    public Collection<String> getAllLanguages() {
        return Collections.unmodifiableCollection(allLanguages);
    }

}
