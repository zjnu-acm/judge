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

import java.util.Locale;
import java.util.Objects;
import java.util.function.Predicate;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.LocaleResolver;

/**
 *
 * @author Administrator
 */
public class FilterLocaleResolver implements LocaleResolver {

    private final Predicate<Locale> predicate;
    private final Locale defaultLocale;
    private final LocaleResolver parent;

    public FilterLocaleResolver(LocaleResolver parent, Locale defaultLocale, Predicate<Locale> predicate) {
        this.parent = Objects.requireNonNull(parent, "parent");
        this.defaultLocale = Objects.requireNonNull(defaultLocale, "defaultLocale");
        this.predicate = Objects.requireNonNull(predicate, "predicate");
    }

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        Locale locale = parent.resolveLocale(request);
        return locale == null || !predicate.test(defaultLocale) ? defaultLocale : locale;
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        parent.setLocale(request, response, locale);
    }

}
