/*
 * Copyright 2017-2019 ZJNU ACM.
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

import cn.edu.zjnu.acm.judge.domain.LoginLog;
import cn.edu.zjnu.acm.judge.domain.User;
import cn.edu.zjnu.acm.judge.mapper.UserMapper;
import cn.edu.zjnu.acm.judge.service.LoginlogService;
import java.time.Instant;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

/**
 *
 * @author zhanhb
 */
@Configuration
@SuppressWarnings({"PublicInnerClass", "UtilityClassWithoutPrivateConstructor"})
public class LoginListener {

    @Nullable
    private static String saveEvent(LoginlogService loginlogService, Authentication authentication) {
        Object details = authentication.getDetails();
        String remoteAddress = "";
        boolean success = authentication.isAuthenticated();
        String type = authentication.getClass().getSimpleName();
        final String tokenSuffix = "AuthenticationToken";
        if (type.endsWith(tokenSuffix)) {
            type = type.substring(0, type.length() - tokenSuffix.length());
        }
        if (details instanceof WebAuthenticationDetails) {
            WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails) details;
            remoteAddress = webAuthenticationDetails.getRemoteAddress();
        }
        loginlogService.save(LoginLog.builder().ip(remoteAddress).success(success).type(type).user(authentication.getName()).build());
        return remoteAddress;
    }

    @Configuration
    @RequiredArgsConstructor
    public static class LoginSuccessListener implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {

        private final LoginlogService loginlogService;
        private final UserMapper userMapper;

        @Override
        public void onApplicationEvent(@Nonnull InteractiveAuthenticationSuccessEvent event) {
            Authentication authentication = event.getAuthentication();
            String ip = saveEvent(loginlogService, authentication);
            Optional.ofNullable(userMapper.findOne(authentication.getName())).ifPresent(user -> {
                userMapper.updateSelective(user.getId(), User.builder()
                        .accesstime(Instant.now())
                        .ip(ip)
                        .build());
            });
        }

    }

    @Configuration
    @RequiredArgsConstructor
    public static class LoginFailureListener implements
            ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

        private final LoginlogService loginlogService;

        @Override
        public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
            Authentication authentication = event.getAuthentication();
            saveEvent(loginlogService, authentication);
        }

    }

}
