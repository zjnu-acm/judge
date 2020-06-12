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
package cn.edu.zjnu.acm.judge.config.security;

import cn.edu.zjnu.acm.judge.config.security.password.CombinePasswordEncoder;
import cn.edu.zjnu.acm.judge.config.security.password.LengthLimitedPasswordEncoder;
import cn.edu.zjnu.acm.judge.config.security.password.MessageDigestPasswordEncoder;
import cn.edu.zjnu.acm.judge.config.security.password.MultiPasswordSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author zhanhb
 */
@Configuration
public class PasswordConfiguration {

    public static final int MAX_PASSWORD_LENGTH = 30;

    @Bean
    @SuppressWarnings("deprecation")
    public PasswordEncoder passwordEncoder() {
        return new LengthLimitedPasswordEncoder(
                new CombinePasswordEncoder(
                        new MultiPasswordSupport(
                                new CombinePasswordEncoder(
                                        new BCryptPasswordEncoder(8),
                                        MessageDigestPasswordEncoder.MD5,
                                        MessageDigestPasswordEncoder.SHA1,
                                        MessageDigestPasswordEncoder.SHA256,
                                        MessageDigestPasswordEncoder.SHA384,
                                        MessageDigestPasswordEncoder.SHA512
                                )
                        ),
                        org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance()
                ), MAX_PASSWORD_LENGTH);
    }

}
