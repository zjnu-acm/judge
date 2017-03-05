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
import cn.edu.zjnu.acm.judge.util.SpecialCall;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
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

    @Autowired
    private LanguageMapper languageMapper;

    public Map<Integer, Language> getAvailableLanguages() {
        return languageMapper.findAll().stream()
                .collect(Collectors.toMap(Language::getId, Function.identity(), throwOnMerge(), LinkedHashMap::new));
    }

    public Language getAvailableLanguage(int languageId) {
        return Optional.ofNullable(languageMapper.findOne(languageId))
                .orElseThrow(() -> new NoSuchLanguageException("no such language " + languageId));
    }

    @SpecialCall
    public String getLanguageName(int languageId) {
        return Optional.ofNullable(languageMapper.findOne(languageId))
                .map(Language::getName).orElse("unknown language " + languageId);
    }

}
