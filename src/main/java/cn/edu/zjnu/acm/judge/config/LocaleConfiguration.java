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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 *
 * @author zhanhb
 */
@Configuration
public class LocaleConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        localeChangeInterceptor.setIgnoreInvalidLocale(true);
        localeChangeInterceptor.setLanguageTagCompliant(true);
        registry.addInterceptor(localeChangeInterceptor);
    }

    /* Store preferred language configuration in a cookie */
    @Bean(name = "localeResolver")
    public LocaleResolver localeResolver(ServletContext container) {
        CookieLocaleResolver localeResolver = new CookieLocaleResolver() {

            @Override
            protected Locale determineDefaultLocale(HttpServletRequest request) {
                Locale locale = super.determineDefaultLocale(request);
                HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
                addCookie(response, (locale != null ? toLocaleValue(locale) : "-"));
                return locale;
            }

        };
        localeResolver.setCookieName("locale");
        localeResolver.setCookieMaxAge(15 * 24 * 60 * 60);
        localeResolver.setCookiePath(getCookiePath(container));
        localeResolver.setLanguageTagCompliant(true);
        return localeResolver;
    }

    private String getCookiePath(ServletContext container) {
        String contextPath = container.getContextPath();
        return contextPath.endsWith("/") ? contextPath : contextPath + '/';
    }

}
