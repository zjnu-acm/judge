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

import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.mapper.ContestMapper;
import cn.edu.zjnu.acm.judge.mapper.ProblemMapper;
import cn.edu.zjnu.acm.judge.util.SpecialCall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author zhanhb
 */
@Service
@SpecialCall("contests/problems.html")
public class ContestService {

    @Autowired
    private ContestMapper contestMapper;
    @Autowired
    private ProblemMapper problemMapper;

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
        problemMapper.setContest(problemId, null);
        contestMapper.deleteContestProblem(contestId, problemId);
        updateContestOrder(contestId, 1000);
    }

}
