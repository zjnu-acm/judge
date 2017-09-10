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

import cn.edu.zjnu.acm.judge.data.form.ContestForm;
import cn.edu.zjnu.acm.judge.data.form.ContestStatus;
import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.exception.BusinessCode;
import cn.edu.zjnu.acm.judge.exception.BusinessException;
import cn.edu.zjnu.acm.judge.mapper.ContestMapper;
import cn.edu.zjnu.acm.judge.mapper.ProblemMapper;
import cn.edu.zjnu.acm.judge.mapper.SubmissionMapper;
import cn.edu.zjnu.acm.judge.util.SpecialCall;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author zhanhb
 */
@Service
@Slf4j
@SpecialCall("contests/problems.html")
public class ContestService {

    @Autowired
    private ContestMapper contestMapper;
    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private SubmissionMapper submissionMapper;
    @Autowired
    private LocaleService localeService;
    @Autowired
    private ObjectMapper objectMapper;

    @SpecialCall("contests/problems.html")
    public String getStatus(Contest contest) {
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

    void addProblem(long contestId, long problemId) {
        problemMapper.setContest(problemId, contestId);
        contestMapper.addProblem(contestId, problemId, null);
    }

    public List<Contest> findAll(ContestForm form) {
        EnumSet<ContestStatus> exclude = parse(form.getExclude());
        EnumSet<ContestStatus> include = parse(form.getInclude());
        Set<ContestStatus> result;
        if (!exclude.isEmpty()) {
            result = EnumSet.allOf(ContestStatus.class);
            result.removeAll(exclude);
        } else if (!include.isEmpty()) {
            result = include;
        } else {
            result = EnumSet.allOf(ContestStatus.class);
        }
        return contestMapper.findAllByQuery(form.isIncludeDisabled(), toMask(result));
    }

    public List<Contest> findAll(ContestStatus first, ContestStatus... rest) {
        return contestMapper.findAllByQuery(false, toMask(EnumSet.of(first, rest)));
    }

    private int toMask(Set<ContestStatus> result) {
        int mask = 0;
        for (ContestStatus contestStatus : result) {
            mask |= 1 << contestStatus.ordinal();
        }
        log.info("mask: {}", mask);
        return mask;
    }

    private EnumSet<ContestStatus> parse(String[] filter) {
        EnumSet<ContestStatus> set = EnumSet.noneOf(ContestStatus.class);
        if (filter != null) {
            for (String exclude : filter) {
                if (exclude != null) {
                    for (String name : exclude.split("\\W+")) {
                        ContestStatus contestStatus;
                        try {
                            contestStatus = ContestStatus.valueOf(name.trim().toUpperCase(Locale.US));
                        } catch (IllegalArgumentException ex) {
                            continue;
                        }
                        switch (contestStatus) {
                            case PENDING:
                            case RUNNING:
                            case ENDED:
                            case ERROR:
                                set.add(contestStatus);
                                break;
                            default:
                                throw new AssertionError();
                        }
                    }
                }
            }
        }
        return set;
    }

    public Contest findOne(long id) {
        return contestMapper.findOne(id);
    }

    @Transactional
    public Contest save(Contest contest) {
        contestMapper.save(contest);
        Long id = contest.getId();
        List<Problem> problems = contest.getProblems();
        if (problems != null) {
            add(id, problems);
        }
        return contest;
    }

    public Contest getContestAndProblems(long contestId, String userId, Locale locale) {
        Contest contest = getContest(contestId);
        List<Problem> problems = contestMapper.getProblems(contestId, userId, localeService.resolve(locale));
        contest.setProblems(problems);
        return contest;
    }

    private Contest getContest(long contestId) {
        Contest contest = contestMapper.findOne(contestId);
        if (contest == null) {
            throw new BusinessException(BusinessCode.NOT_FOUND);
        }
        return contest;
    }

    @Transactional
    public void delete(long id) throws IOException {
        if (log.isWarnEnabled()) {
            List<Long> submissions = submissionMapper.findAllByContestId(id);
            List<Problem> problems = contestMapper.getProblems(id, null, null);
            log.warn("delete contest id: {}, submissions: {}, problems: {}", id, objectMapper.writeValueAsString(submissions), objectMapper.writeValueAsString(problems));
        }
        long result = submissionMapper.clearByContestId(id)
                + problemMapper.clearByContestId(id)
                + contestMapper.deleteContestProblems(id)
                + contestMapper.deleteByPrimaryKey(id);
        if (result == 0) {
            throw new BusinessException(BusinessCode.NOT_FOUND);
        }
    }

    @Transactional
    public void updateSelective(long id, Contest contest) {
        contest.setModifiedTime(Instant.now());
        long result = contestMapper.updateSelective(id, contest);
        if (result == 0) {
            throw new BusinessException(BusinessCode.NOT_FOUND);
        }
        List<Problem> problems = contest.getProblems();
        if (problems != null) {
            contestMapper.deleteContestProblems(id);
            add(id, problems);
        }
    }

    public Map<Long, Integer> getNumMap(long id) {
        List<Problem> problems = contestMapper.getProblems(id, null, null);
        AtomicInteger atomic = new AtomicInteger();
        return problems.stream().collect(Collectors.toMap(Problem::getOrigin, __ -> atomic.getAndIncrement()));
    }

    public List<Long> submittedProblems(long id) {
        if (contestMapper.findOne(id) == null) {
            throw new BusinessException(BusinessCode.NOT_FOUND);
        }
        return contestMapper.submittedProblems(id);
    }

    private void add(long contestId, List<Problem> problems) {
        assert problems != null;
        if (!problems.isEmpty()) {
            long[] array = problems.stream().mapToLong(Problem::getOrigin).toArray();
            contestMapper.addProblems(contestId, 1000, array);
            problemMapper.setContestBatch(array, contestId);
        }
    }

    public String toProblemIndex(int num) {
        char t = (char) ('A' + num);
        return String.valueOf(t);
    }

}
