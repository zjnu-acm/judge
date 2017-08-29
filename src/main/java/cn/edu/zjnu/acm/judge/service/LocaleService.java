/*
 * Copyright 2017 ZJNU ACM.
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

import cn.edu.zjnu.acm.judge.domain.DomainLocale;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 *
 * @author zhanhb
 */
@Service
public class LocaleService {

    private final List<String> allLanguages;

    public LocaleService() {
        this.allLanguages = Arrays.asList("zh", "en");
    }

    public String resolve(Locale locale) {
        return toSupported(locale).toLanguageTag();
    }

    public Locale toSupported(Locale locale) {
        List<Locale> candidateLocales = ControlHolder.CONTROL.getCandidateLocales("", locale);
        Set<String> collect = allLanguages.stream()
                .collect(Collectors.toCollection(() -> new TreeSet<>(String.CASE_INSENSITIVE_ORDER)));
        for (Locale candidateLocale : candidateLocales) {
            if (collect.contains(candidateLocale.toLanguageTag())) {
                return candidateLocale;
            }
        }
        return Locale.ROOT;
    }

    public DomainLocale toDomainLocale(Locale locale, Locale inLocale) {
        String displayName = locale.getDisplayName(inLocale);
        return DomainLocale.builder().id(locale).name(displayName).build();
    }

    public DomainLocale toDomainLocale(String localeName) {
        Locale locale = Locale.forLanguageTag(localeName);
        return toDomainLocale(locale, locale);
    }

    public List<DomainLocale> findAll() {
        return allLanguages.stream().map(Locale::forLanguageTag).map(locale -> toDomainLocale(locale, locale)).collect(Collectors.toList());
    }

    @SuppressWarnings("UtilityClassWithoutPrivateConstructor")
    private static class ControlHolder {

        static final ResourceBundle.Control CONTROL = ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_PROPERTIES);

    }

}
