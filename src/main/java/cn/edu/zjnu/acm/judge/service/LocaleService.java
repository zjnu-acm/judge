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
import cn.edu.zjnu.acm.judge.mapper.LocaleMapper;
import com.google.common.collect.ImmutableSortedSet;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author zhanhb
 */
@Service
public class LocaleService {

    private static final Comparator<Locale> DEFAULT_LOCALE_COMPARATOR = Comparator.comparing(Locale::toLanguageTag);
    private static final Comparator<DomainLocale> DEFAULT_DOMAIN_LOCALE_COMPARATOR = Comparator.comparing(DomainLocale::getName);

    @Autowired
    private LocaleMapper localeMapper;

    @Nullable
    public String resolve(Locale locale) {
        Locale toSupported = toSupported(locale);
        return toSupported.equals(Locale.ROOT) ? null : toSupported.toLanguageTag();
    }

    @Nonnull
    public Locale toSupported(Locale locale) {
        if (locale == null) {
            return Locale.ROOT;
        }
        List<Locale> candidateLocales = ControlHolder.CONTROL.getCandidateLocales("", locale);
        Set<String> collect = findAll().stream().map(DomainLocale::getId)
                .collect(ImmutableSortedSet.toImmutableSortedSet(String.CASE_INSENSITIVE_ORDER));
        for (Locale candidateLocale : candidateLocales) {
            if (collect.contains(candidateLocale.toLanguageTag())) {
                return candidateLocale;
            }
        }
        return Locale.ROOT;
    }

    public DomainLocale toDomainLocale(Locale locale, Locale inLocale) {
        DomainLocale domainLocale = localeMapper.findOne(locale.toLanguageTag());
        if (domainLocale != null) {
            return domainLocale;
        }
        String displayName = locale.getDisplayName(inLocale);
        return DomainLocale.builder().id(locale.toLanguageTag()).name(displayName).build();
    }

    public DomainLocale toDomainLocale(String localeName, boolean supportOnly) {
        Locale locale = Locale.forLanguageTag(localeName);
        return toDomainLocale(supportOnly ? toSupported(locale) : locale, locale);
    }

    public List<DomainLocale> findAll() {
        return localeMapper.findAll();
    }

    @Nullable
    public DomainLocale findOne(String id) {
        if (Locale.ROOT.toLanguageTag().equalsIgnoreCase(id)) {
            return null;
        }
        return localeMapper.findOne(id);
    }

    public List<DomainLocale> support(boolean all) {
        Locale[] locales = Locale.getAvailableLocales();
        Stream<Locale> stream = Arrays.stream(locales);
        if (!all) {
            stream = stream.map(locale -> Locale.forLanguageTag(locale.getLanguage()));
        }
        return stream
                .sorted(DEFAULT_LOCALE_COMPARATOR).distinct()
                .map(locale -> toDomainLocale(locale, locale))
                .sorted(DEFAULT_DOMAIN_LOCALE_COMPARATOR).distinct().collect(Collectors.toList());
    }

    private static interface ControlHolder {

        static final ResourceBundle.Control CONTROL = ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_PROPERTIES);

    }

}
