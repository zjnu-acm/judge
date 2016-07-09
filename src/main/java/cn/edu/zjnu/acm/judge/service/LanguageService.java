/*
 * Copyright 2014 zhanhb.
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
package cn.edu.zjnu.acm.judge.service;

import cn.edu.zjnu.acm.judge.domain.Language;
import cn.edu.zjnu.acm.judge.exception.NoSuchLanguageException;
import cn.edu.zjnu.acm.judge.mapper.LanguageMapper;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author zhanhb
 */
@Service
public class LanguageService {

    private static <T> BinaryOperator<T> throwOnMerge() {
        return (u, v) -> {
            throw new IllegalStateException();
        };
    }

    private Map<Integer, Language> languages;

    @Autowired
    public void setLanguageMapper(LanguageMapper languageMapper) {
        List<Language> allLanguages = languageMapper.findAll();
        languages = allLanguages.stream()
                .collect(Collectors.toMap(Language::getId, Function.identity(), throwOnMerge(), LinkedHashMap::new));
    }

    public Map<Integer, Language> getLanguages() {
        return Collections.unmodifiableMap(languages);
    }

    public Language getLanguage(int languageId) {
        return languages.computeIfAbsent(languageId, x -> {
            throw new NoSuchLanguageException("no such language " + x);
        });
    }

}
