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

import cn.edu.zjnu.acm.judge.domain.User;
import cn.edu.zjnu.acm.judge.mapper.UserMapper;
import cn.edu.zjnu.acm.judge.util.Utility;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheStats;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author zhanhb
 */
@Service
public class ResetPasswordService {

    @Autowired
    private UserMapper userMapper;

    private Cache<String, String> cache;

    @PostConstruct
    public void init() {
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .recordStats()
                .build();
    }

    public Optional<User> checkVcode(String userId, String vcode) {
        Optional<User> optioanl = Optional.ofNullable(userId).map(userMapper::findOne);
        return optioanl.map(User::getId).map(asMap()::get)
                .filter(code -> code.equals(vcode))
                .flatMap(__ -> optioanl);
    }

    public String getOrCreate(String id) {
        return asMap().compute(id, (__, old) -> old != null ? old : Utility.getRandomString(16));
    }

    @Nullable
    public String getIfPresent(String id) {
        return asMap().get(id);
    }

    public ConcurrentMap<String, String> asMap() {
        return cache.asMap();
    }

    public void remove(String userId) {
        cache.invalidate(userId);
    }

    public Map<String, ?> stats() {
        CacheStats stats = cache.stats();
        return ImmutableMap.of("content", asMap(), "stats",
                ImmutableMap.builder()
                        .put("hitCount", stats.hitCount())
                        .put("missCount", stats.missCount())
                        .put("loadSuccessCount", stats.loadSuccessCount())
                        .put("loadExceptionCount", stats.loadExceptionCount())
                        .put("totalLoadTime", stats.totalLoadTime())
                        .put("evictionCount", stats.evictionCount())
                        .build());
    }

}
