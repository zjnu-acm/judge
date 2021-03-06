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
package cn.edu.zjnu.acm.judge.service.impl;

import cn.edu.zjnu.acm.judge.domain.User;
import cn.edu.zjnu.acm.judge.mapper.UserMapper;
import cn.edu.zjnu.acm.judge.service.ResetPasswordService;
import cn.edu.zjnu.acm.judge.util.Utility;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Policy;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import com.google.common.collect.ImmutableMap;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author zhanhb
 */
@RequiredArgsConstructor
@Service("resetPasswordService")
public class ResetPasswordServiceImpl implements ResetPasswordService {

    private static final int EXPIRE_HOUR = 3;
    private static final Duration REPEAT_EMAIL_DURATION = Duration.ofMinutes(10);

    private final UserMapper userMapper;

    private Cache<String, String> cache;
    private Set<String> emailSet;
    private Policy.Expiration<String, Boolean> expirations;

    @PostConstruct
    public void init() {
        cache = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofHours(EXPIRE_HOUR))
                .recordStats()
                .build();
        Cache<String, Boolean> emailCache = Caffeine.newBuilder()
                .expireAfterWrite(REPEAT_EMAIL_DURATION)
                .recordStats()
                .build();
        expirations = emailCache.policy().expireAfterWrite().orElseThrow(IllegalStateException::new);
        emailSet = Collections.newSetFromMap(emailCache.asMap());
    }

    @Override
    public Optional<User> checkVcode(String userId, String vcode) {
        Optional<User> optioanl = Optional.ofNullable(userId).map(userMapper::findOne);
        return optioanl.map(User::getId).map(asMap()::get)
                .filter(code -> code.equals(vcode))
                .flatMap(__ -> optioanl);
    }

    @Override
    public String getOrCreate(String id) {
        return asMap().compute(id, (__, old) -> old != null ? old : Utility.getRandomString(16));
    }

    @Nullable
    @Override
    public Optional<String> get(String id) {
        return Optional.ofNullable(asMap().get(id));
    }

    private ConcurrentMap<String, String> asMap() {
        return cache.asMap();
    }

    @Override
    public void remove(String userId) {
        cache.invalidate(userId);
    }

    @Override
    public Map<String, ?> stats() {
        CacheStats stats = cache.stats();
        return ImmutableMap.of("content", asMap(), "stats",
                ImmutableMap.builder()
                        .put("hitCount", stats.hitCount())
                        .put("missCount", stats.missCount())
                        .put("loadSuccessCount", stats.loadSuccessCount())
                        .put("loadFailureCount", stats.loadFailureCount())
                        .put("totalLoadTime", stats.totalLoadTime())
                        .put("evictionCount", stats.evictionCount())
                        .build());
    }

    @Override
    public Integer addEmailCache(String email) {
        Optional<Duration> age = expirations.ageOf(email);
        if (!age.isPresent()) {
            boolean result = emailSet.add(email);
            assert result;
            return null;
        }
        Duration expiration = age.get();
        return (int) (Math.ceil(REPEAT_EMAIL_DURATION.minus(expiration).getSeconds() / 60.) + 0.5);
    }

    @Override
    public void removeEmailCache(String email) {
        emailSet.remove(email);
    }

    @Override
    public int getExpireHour() {
        return EXPIRE_HOUR;
    }

}
