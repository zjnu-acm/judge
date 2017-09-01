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

import cn.edu.zjnu.acm.judge.data.dto.ContestDto;
import cn.edu.zjnu.acm.judge.data.form.ContestAddProblemForm;
import cn.edu.zjnu.acm.judge.data.form.ContestForm;
import cn.edu.zjnu.acm.judge.data.form.ContestStatus;
import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.exception.BusinessCode;
import cn.edu.zjnu.acm.judge.exception.BusinessException;
import cn.edu.zjnu.acm.judge.mapper.ContestMapper;
import cn.edu.zjnu.acm.judge.mapper.ProblemMapper;
import cn.edu.zjnu.acm.judge.util.SpecialCall;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
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
    private LocaleService localeService;

    @SpecialCall("contests/problems.html")
    public String getStatus(Contest contest) {
        if (contest.isDisabled()) {
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

    private void updateContestOrder(long contest, long base) {
        contestMapper.updateContestOrder(contest, base + 99999999L);
        contestMapper.updateContestOrder(contest, base);
    }

    @Transactional
    public void addProblem(long contestId, long problemId) {
        problemMapper.setContest(problemId, contestId);
        contestMapper.addProblem(contestId, problemId, null, 9999999);
        updateContestOrder(contestId, 1000);
    }

    @Transactional
    public void removeProblem(long contestId, long problemId) {
        Contest contest = getContest(contestId);
        if (contest.isEnded()) {
            throw new BusinessException(BusinessCode.CONTEST_ENDED);
        }
        if (contest.isStarted()) {
            throw new BusinessException(BusinessCode.CONTEST_STARTED);
        }
        problemMapper.setContest(problemId, null);
        contestMapper.deleteContestProblem(contestId, problemId);
        updateContestOrder(contestId, 1000);
    }

    public void addProblems(long contestId, ContestAddProblemForm[] form) {
        Contest contest = getContest(contestId);
        if (contest.isEnded()) {
            throw new BusinessException(BusinessCode.CONTEST_ENDED);
        }
        int num = 9999999;
        for (ContestAddProblemForm contestAddProblemForm : form) {
            problemMapper.setContest(contestAddProblemForm.getId(), contestId);
            contestMapper.addProblem(contestId, contestAddProblemForm.getId(), null, num);
            ++num;
        }
        updateContestOrder(contestId, 1000);
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
        int mask = 0;
        for (ContestStatus contestStatus : result) {
            mask |= 1 << contestStatus.ordinal();
        }
        log.info("mask: {}", mask);
        return contestMapper.findAllByQuery(form.isIncludeDisabled(), mask);
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

    public void disable(long id) {
        contestMapper.disable(id);
    }

    public void enable(long id) {
        contestMapper.enable(id);
    }

    public Contest save(Contest contest) {
        contestMapper.save(contest);
        return contest;
    }

    public ContestDto getContestAndProblems(long contestId, String userId, Locale locale) {
        Contest contest = getContest(contestId);
        List<Problem> problems = contestMapper.getProblems(contestId, userId, localeService.resolve(locale));
        return ContestDto.builder().contest(contest).problems(problems).build();
    }

    private Contest getContest(long contestId) {
        Contest contest = contestMapper.findOne(contestId);
        if (contest == null) {
            throw new BusinessException(BusinessCode.NOT_FOUND);
        }
        return contest;
    }

}
