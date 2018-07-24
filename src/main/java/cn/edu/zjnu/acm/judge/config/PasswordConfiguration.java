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

import cn.edu.zjnu.acm.judge.config.password.AcceptNullPasswordEncoder;
import cn.edu.zjnu.acm.judge.config.password.CombinePasswordEncoder;
import cn.edu.zjnu.acm.judge.config.password.LengthLimitedPasswordEncoder;
import cn.edu.zjnu.acm.judge.config.password.MessageDigestPasswordEncoder;
import cn.edu.zjnu.acm.judge.config.password.MultiPasswordSupport;
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
    public PasswordEncoder passwordEncoder() {
        @SuppressWarnings("deprecation")
        PasswordEncoder passwordEncoder = new LengthLimitedPasswordEncoder(
                new CombinePasswordEncoder(1,
                        org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance(),
                        new MultiPasswordSupport(
                                new CombinePasswordEncoder(
                                        new BCryptPasswordEncoder(6),
                                        MessageDigestPasswordEncoder.md5(),
                                        MessageDigestPasswordEncoder.sha1(),
                                        MessageDigestPasswordEncoder.sha256(),
                                        MessageDigestPasswordEncoder.sha384(),
                                        MessageDigestPasswordEncoder.sha512()
                                )
                        )
                ), MAX_PASSWORD_LENGTH);
        return new AcceptNullPasswordEncoder(passwordEncoder);
    }

}
