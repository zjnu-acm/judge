/*
 * Copyright 2019 ZJNU ACM.
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

import cn.edu.zjnu.acm.judge.mapper.PersistentLoginMapper;
import java.time.Duration;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author zhanhb
 */
@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class RememberMeCleaner {

    private static final Duration DEFAULT_EXPIRE_TIMEOUT = Duration.ofDays(14);

    private final PersistentLoginMapper persistentLoginMapper;

    // at 2:54 everyday
    @Scheduled(cron = "0 54 2 ? * *")
    public void cleanUpExpiredCookies() {
        Instant boundary = Instant.now().minus(DEFAULT_EXPIRE_TIMEOUT);
        persistentLoginMapper.cleanUpExpiredCookies(boundary);
    }

}
