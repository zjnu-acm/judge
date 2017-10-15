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
package cn.edu.zjnu.acm.judge.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Comparator;
import java.util.Map;
import javax.annotation.Nullable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 *
 * @author zhanhb
 */
@RequiredArgsConstructor
public class UserStanding {

    public static final Comparator<UserStanding> COMPARATOR
            = Comparator.comparingInt(UserStanding::getSolved).reversed().thenComparingLong(UserStanding::getTime);

    @Getter
    private final String user;
    @Getter
    private int solved;
    @Getter
    private long time;
    @Getter
    @Setter(AccessLevel.PACKAGE)
    private int index;
    @Getter
    @Setter(AccessLevel.PACKAGE)
    private String nick;

    private final Map<Long, ProblemTimePenalty> map = Maps.newHashMapWithExpectedSize(50);

    /**
     *
     * @param problem the id of the problem
     * @param time the time escaped after the contest starts, {@code null} if
     * the user hasn't got an accepted.
     * @param penalty the number of wrong answer before user get accepted.
     */
    void add(long problem, Long time, long penalty) {
        Preconditions.checkArgument(penalty >= 0, "penalty < 0");
        if (map.put(problem, new ProblemTimePenalty(time, penalty)) != null) {
            throw new IllegalStateException();
        }
        if (time != null) {
            ++solved;
            this.time += time + 20L * 60 * penalty;
        }
    }

    /**
     * get accepted time of specified problem.
     *
     * @param problem the id of the problem
     * @return the time, {@code null} if user hasn't got an accepted.
     */
    @Nullable
    public Long getTime(long problem) {
        ProblemTimePenalty timePenalty = map.get(problem);
        return timePenalty == null ? null : timePenalty.time;
    }

    /**
     * get accepted time of specified problem.
     *
     * @param problem the id of the problem
     * @return the penalty
     */
    public long getPenalty(long problem) {
        ProblemTimePenalty timePenalty = map.get(problem);
        return timePenalty == null ? 0 : timePenalty.penalty;
    }

    @RequiredArgsConstructor
    private static class ProblemTimePenalty {

        final Long time;
        final long penalty;

    }
}
