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
import cn.edu.zjnu.acm.judge.exception.BusinessCode;
import cn.edu.zjnu.acm.judge.exception.BusinessException;
import cn.edu.zjnu.acm.judge.mapper.LanguageMapper;
import cn.edu.zjnu.acm.judge.util.SpecialCall;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import javax.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author zhanhb
 */
@Service
@SpecialCall({"contests/problems-status", "problems/status"})
public class LanguageService {

    @Autowired
    private LanguageMapper languageMapper;

    public Map<Integer, Language> getAvailableLanguages() {
        return languageMapper.findAll().stream()
                .collect(ImmutableMap.toImmutableMap(Language::getId, Function.identity()));
    }

    @Nonnull
    public Language getAvailableLanguage(int languageId) {
        Language language = languageMapper.findOne(languageId);
        if (language == null) {
            throw new BusinessException(BusinessCode.LANGUAGE_NOT_FOUND, languageId);
        }
        return language;
    }

    @SpecialCall({"contests/problems-status", "problems/status"})
    public String getLanguageName(int languageId) {
        return Optional.ofNullable(languageMapper.findOne(languageId))
                .map(Language::getName).orElse("unknown language " + languageId);
    }

}
