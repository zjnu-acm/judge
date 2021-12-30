/*
 * Copyright 2016-2019 ZJNU ACM.
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

import cn.edu.zjnu.acm.judge.data.form.ContestForm;
import cn.edu.zjnu.acm.judge.data.form.ContestStatus;
import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.exception.BusinessCode;
import cn.edu.zjnu.acm.judge.exception.BusinessException;
import cn.edu.zjnu.acm.judge.mapper.ContestMapper;
import cn.edu.zjnu.acm.judge.mapper.ContestProblemMapper;
import cn.edu.zjnu.acm.judge.mapper.SubmissionMapper;
import cn.edu.zjnu.acm.judge.service.ContestService;
import cn.edu.zjnu.acm.judge.service.LocaleService;
import cn.edu.zjnu.acm.judge.util.EnumUtils;
import cn.edu.zjnu.acm.judge.util.SpecialCall;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.ObjIntConsumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author zhanhb
 */
@RequiredArgsConstructor
@Service("contestService")
@Slf4j
@SpecialCall("contests/problems")
public class ContestServiceImpl implements ContestService {

    private static final ConcurrentMap<Long, CompletableFuture<List<UserStanding>>> STANDINGS = new ConcurrentHashMap<>(20);

    private static <T> Function<T, T> removeStanding(long id, CompletableFuture<List<UserStanding>> future) {
        return it -> {
            STANDINGS.remove(id, future);
            return it;
        };
    }

    private final ContestMapper contestMapper;
    private final ContestProblemMapper contestProblemMapper;
    private final SubmissionMapper submissionMapper;
    private final LocaleService localeService;
    private final ObjectMapper objectMapper;

    @Override
    @SpecialCall("contests/problems")
    public String getStatus(@Nonnull Contest contest) {
        Boolean disabled = contest.getDisabled();
        if (disabled != null && disabled) {
            return "Disabled";
        } else if (contest.isError()) {
            return "Error";
        } else if (!contest.isStarted()) {
            return "Pending";
        } else if (!contest.isEnded()) {
            return "Running";
        } else {
            return "Ended";
        }
    }

    @Override
    public void addProblem(long contestId, long problemId) {
        contestProblemMapper.addProblem(contestId, problemId, null);
    }

    @Override
    public List<Contest> findAll(ContestForm form) {
        EnumSet<ContestStatus> exclude = ContestStatus.parse(form.getExclude());
        EnumSet<ContestStatus> include = ContestStatus.parse(form.getInclude());
        EnumSet<ContestStatus> result;
        if (!exclude.isEmpty()) {
            result = EnumSet.allOf(ContestStatus.class);
            result.removeAll(exclude);
        } else if (!include.isEmpty()) {
            result = include;
        } else {
            result = EnumSet.allOf(ContestStatus.class);
        }
        return contestMapper.findAllByQuery(form.isIncludeDisabled(), EnumUtils.toMask(result));
    }

    @Override
    public List<Contest> findAll(ContestStatus first, ContestStatus... rest) {
        return contestMapper.findAllByQuery(false, EnumUtils.toMask(EnumSet.of(first, rest)));
    }

    @Transactional
    @Override
    public Contest save(Contest contest) {
        contestMapper.save(contest);
        Long id = contest.getId();
        List<Problem> problems = contest.getProblems();
        if (problems != null) {
            add(id, problems);
        }
        return contest;
    }

    @Nonnull
    @Override
    public Contest getContestAndProblems(long contestId, Locale locale) {
        Contest contest = checkedGet(contestId);
        List<Problem> problems = contestProblemMapper.getProblems(contestId, null, localeService.resolve(locale));
        contest.setProblems(problems);
        return contest;
    }

    @Nonnull
    @Override
    public Contest getContestAndProblemsNotDisabled(long contestId, @Nullable String userId, Locale locale) {
        Contest contest = getEnabledContest(contestId);
        List<Problem> problems = contestProblemMapper.getProblems(contestId, userId, localeService.resolve(locale));
        contest.setProblems(problems);
        return contest;
    }

    @Nonnull
    private Contest getEnabledContest(long contestId) {
        Contest contest = contestMapper.findOneByIdAndNotDisabled(contestId);
        if (contest == null) {
            throw new BusinessException(BusinessCode.CONTEST_NOT_FOUND, contestId);
        }
        return contest;
    }

    @Nonnull
    private Contest checkedGet(long contestId) {
        Contest contest = contestMapper.findOne(contestId);
        if (contest == null) {
            throw new BusinessException(BusinessCode.CONTEST_NOT_FOUND, contestId);
        }
        return contest;
    }

    @Transactional
    @Override
    public void delete(long id) throws IOException {
        if (log.isWarnEnabled()) {
            List<Long> submissions = submissionMapper.findAllByContestId(id);
            List<Problem> problems = contestProblemMapper.getProblems(id, null, null);
            // TODO add trash
            log.warn("delete contest id: {}, submissions: {}, problems: {}", id,
                    objectMapper.writeValueAsString(submissions), objectMapper.writeValueAsString(problems));
        }
        long result = submissionMapper.clearByContestId(id)
                + contestProblemMapper.deleteByContest(id)
                + contestMapper.deleteByPrimaryKey(id);
        if (result == 0) {
            throw new BusinessException(BusinessCode.CONTEST_NOT_FOUND, id);
        }
    }

    @Transactional
    @Override
    public void updateSelective(long id, Contest contest) {
        contest.setCreatedTime(null);
        contest.setModifiedTime(Instant.now());
        long result = contestMapper.updateSelective(id, contest);
        if (result == 0) {
            throw new BusinessException(BusinessCode.CONTEST_NOT_FOUND, id);
        }
        List<Problem> problems = contest.getProblems();
        if (problems != null) {
            contestProblemMapper.deleteByContest(id);
            add(id, problems);
        }
    }

    @Override
    public Map<Long, long[]> getProblemsMap(long id) {
        List<Problem> problems = contestProblemMapper.getProblems(id, null, null);
        AtomicInteger atomic = new AtomicInteger();
        return problems.stream().collect(ImmutableMap.toImmutableMap(Problem::getOrigin,
                problem -> new long[]{atomic.getAndIncrement(), problem.getId()}
        ));
    }

    private void add(long contestId, List<Problem> problems) {
        assert problems != null;
        if (!problems.isEmpty()) {
            long[] array = problems.stream().mapToLong(Problem::getOrigin).toArray();
            contestProblemMapper.addProblems(contestId, 1000, array);
        }
    }

    @Override
    @SpecialCall({"contests/problem", "contests/problems", "contests/standing"})
    public String toProblemIndex(long num) {
        char t = (char) ('A' + num);
        return String.valueOf(t);
    }

    @Override
    public List<UserStanding> standing(long id) {
        Map<String, UserStanding> hashMap = Maps.newHashMapWithExpectedSize(80);
        contestProblemMapper.standing(id).forEach(standing
                -> hashMap.computeIfAbsent(standing.getUser(), UserStanding::new)
                        .add(standing.getProblem(), standing.getTime(), standing.getPenalty())
        );
        contestMapper.attenders(id).forEach(attender
                -> Optional.ofNullable(hashMap.get(attender.getId()))
                        .ifPresent(us -> us.setNick(attender.getNick()))
        );
        UserStanding[] standings = hashMap.values().stream().sorted(UserStanding.COMPARATOR).toArray(UserStanding[]::new);
        setIndexes(standings, Comparator.nullsFirst(UserStanding.COMPARATOR), UserStanding::setIndex);
        return Arrays.asList(standings);
    }

    @Override
    public CompletableFuture<List<UserStanding>> standingAsync(long id) {
        CompletableFuture<List<UserStanding>> future = STANDINGS
                .computeIfAbsent(id, contestId
                        -> CompletableFuture.supplyAsync(() -> standing(contestId)));
        return future.thenApply(removeStanding(id, future));
    }

    @SuppressWarnings("ValueOfIncrementOrDecrementUsed")
    private <T> void setIndexes(T[] standings, Comparator<T> c, ObjIntConsumer<T> consumer) {
        Objects.requireNonNull(standings, "standings");
        Objects.requireNonNull(c, "c");
        Objects.requireNonNull(consumer, "consumer");
        int i = 0, len = standings.length, lastIndex = 0;

        for (T last = null, standing; i < len; last = standing) {
            standing = standings[i++];
            if (c.compare(standing, last) != 0) {
                lastIndex = i;
            }
            consumer.accept(standing, lastIndex);
        }
    }

    @Nonnull
    @Override
    public Problem getProblem(long contestId, long problemNum, @Nullable Locale locale) {
        Problem problem = contestProblemMapper.getProblem(contestId, problemNum, localeService.resolve(locale));
        if (problem == null) {
            throw new BusinessException(BusinessCode.CONTEST_PROBLEM_NOT_FOUND, contestId, problemNum);
        }
        return problem;
    }

    @Nonnull
    @Override
    public Contest findOneByIdAndNotDisabled(long contestId) {
        return getEnabledContest(contestId);
    }

}
