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

import cn.edu.zjnu.acm.judge.config.Constants;
import cn.edu.zjnu.acm.judge.domain.Submission;
import cn.edu.zjnu.acm.judge.exception.BusinessCode;
import cn.edu.zjnu.acm.judge.exception.BusinessException;
import java.util.Objects;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 *
 * @author zhanhb
 */
@Service
public class ContestOnlyService {

    private static final String KEY = "value";
    private Cache cache;

    @Autowired
    public void setCacheManager(CacheManager cacheManager) {
        cache = cacheManager.getCache(Constants.Cache.CONTEST_ONLY);
    }

    @Nullable
    public Long getContestOnly() {
        return cache.get(KEY, Long.class);
    }

    public void setContestOnly(@Nullable Long contestOnly) {
        if (contestOnly == null) {
            cache.evict(KEY);
        } else {
            cache.put(KEY, contestOnly);
        }
    }

    public void checkSubmit(HttpServletRequest request, Long contest, long problemId) {
        Long contestOnly = getContestOnly();
        if (contestOnly == null) {
            return;
        }
        if (UserDetailsServiceImpl.isAdminLoginned(request)) {
            return;
        }
        if (!Objects.equals(contest, contestOnly)) {
            throw new BusinessException(BusinessCode.CONTEST_ONLY_SUBMIT);
        }
    }

    public void checkRegister() {
        Long contestOnly = getContestOnly();
        if (contestOnly == null) {
            return;
        }
        throw new BusinessException(BusinessCode.CONTEST_ONLY_REGISTER);
    }

    public void checkViewSource(HttpServletRequest request, @NonNull Submission submission) {
        if (!canViewSource(request, submission)) {
            throw new BusinessException(BusinessCode.CONTEST_ONLY_VIEW_SOURCE, submission.getId());
        }
    }

    public boolean canViewSource(HttpServletRequest request, @NonNull Submission submission) {
        Long contestOnly = getContestOnly();
        if (contestOnly == null) {
            return true;
        }
        if (UserDetailsServiceImpl.isAdminLoginned(request)) {
            return true;
        }
        Long contestId = submission.getContest();
        if (contestId == null) {
            return false;
        }
        return Objects.equals(contestId, contestOnly);
    }

}
