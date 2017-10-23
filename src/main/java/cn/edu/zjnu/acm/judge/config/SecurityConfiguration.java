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

import com.github.zhanhb.ckfinder.connector.autoconfigure.CKFinderProperties;
import java.io.IOException;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.util.StringUtils;

/**
 *
 * @author zhanhb
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PersistentTokenRepository persistentTokenRepository;
    @Autowired
    private CKFinderProperties ckfinder;

    @Bean(name = "authenticationManager")
    @Primary
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        SimpleUrlAuthenticationSuccessHandler simpleUrlAuthenticationSuccessHandler = new SimpleUrlAuthenticationSuccessHandler("/");
        simpleUrlAuthenticationSuccessHandler.setUseReferer(false);
        simpleUrlAuthenticationSuccessHandler.setTargetUrlParameter("url");
        DefaultRedirectStrategy defaultRedirectStrategy = new DefaultRedirectStrategy();

        simpleUrlAuthenticationSuccessHandler.setRedirectStrategy(defaultRedirectStrategy);

        SimpleUrlLogoutSuccessHandler simpleUrlLogoutSuccessHandler = new SimpleUrlLogoutSuccessHandler();
        simpleUrlLogoutSuccessHandler.setUseReferer(true);

        // @formatter:off
        http
            .authorizeRequests()
                .antMatchers(ckfinder.getServlet().getPath()).hasAnyRole("ADMIN")
                .and()
            .csrf()
                .disable()
            .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint())
                .and()
            .formLogin()
                .loginPage("/login")
                .usernameParameter("user_id1")
                .passwordParameter("password1")
                .successHandler(simpleUrlAuthenticationSuccessHandler)
                .failureHandler(failureHandler())
                .permitAll()
                .and()
            .headers()
                .cacheControl().disable()
                .httpStrictTransportSecurity().disable()
                .frameOptions().sameOrigin()
                .and()
            .logout()
                .logoutUrl("/logout.html")
                .logoutSuccessHandler(simpleUrlLogoutSuccessHandler)
                .permitAll()
                .and()
            .rememberMe()
                .rememberMeParameter("rememberMe")
                .tokenRepository(persistentTokenRepository)
                .and()
            .requestCache()
                .requestCache(new NullRequestCache())
                .and()
            .servletApi();
        // @formatter:on
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> request.getRequestDispatcher("/unauthorized").forward(request, response);
    }

    private AuthenticationFailureHandler failureHandler() {
        final String defaultFailureUrl = "/login?error";
        RedirectStrategy redirectStrategy = new FailureRedirectStrategy();
        return (request, response, exception) -> redirectStrategy.sendRedirect(request, response, defaultFailureUrl);
    }

    private static class FailureRedirectStrategy implements RedirectStrategy {

        private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

        @Override
        public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
            String url1 = request.getParameter("url");
            if (StringUtils.hasText(url1)) {
                redirectStrategy.sendRedirect(request, response, url + (url.contains("?") ? '&' : '?') + "url=" + URLEncoder.encode(url1, "UTF-8"));
            } else {
                redirectStrategy.sendRedirect(request, response, url);
            }
        }
    }

}
