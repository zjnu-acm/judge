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
import javax.servlet.ServletContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 *
 * @author zhanhb
 */
@Configuration
public class LocaleConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        localeChangeInterceptor.setIgnoreInvalidLocale(true);
        registry.addInterceptor(localeChangeInterceptor);
    }

    /* Store preferred language configuration in a cookie */
    @Bean(name = "localeResolver")
    public LocaleResolver localeResolver(ServletContext container, LocaleFactory localeFactory) {
        CookieLocaleResolver localeResolver = new CookieLocaleResolver();
        localeResolver.setCookieName("locale");
        localeResolver.setCookieMaxAge(15 * 24 * 60 * 60);
        localeResolver.setCookiePath(getCookiePath(container));
        return new FilterLocaleResolver(localeResolver, Locale.SIMPLIFIED_CHINESE, locale -> localeFactory.getAllLanguages().contains(locale.getLanguage()));
    }

    private String getCookiePath(ServletContext container) {
        String contextPath = container.getContextPath();
        return contextPath.endsWith("/") ? contextPath : contextPath + '/';
    }

}
