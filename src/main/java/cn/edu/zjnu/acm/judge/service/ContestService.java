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
package cn.edu.zjnu.acm.judge.service;

import cn.edu.zjnu.acm.judge.data.form.ContestForm;
import cn.edu.zjnu.acm.judge.data.form.ContestStatus;
import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.exception.BusinessException;
import cn.edu.zjnu.acm.judge.service.impl.UserStanding;
import cn.edu.zjnu.acm.judge.util.SpecialCall;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import javax.annotation.Nonnull;

/**
 *
 * @author zhanhb
 */
public interface ContestService {

    void delete(long id) throws IOException;

    List<Contest> findAll(ContestForm form);

    List<Contest> findAll(ContestStatus first, ContestStatus... rest);

    @Nonnull
    Contest findOneByIdAndNotDisabled(long contestId) throws BusinessException;

    /**
     * throws BusinessException if contest not exists.
     *
     * @param contestId
     * @param locale
     * @return
     */
    @Nonnull
    Contest getContestAndProblems(long contestId, Locale locale);

    @Nonnull
    Contest getContestAndProblemsNotDisabled(long contestId, String userId, Locale locale);

    @Nonnull
    Problem getProblem(long contestId, long problemNum, Locale locale);

    Map<Long, long[]> getProblemsMap(long id);

    @SpecialCall(value = "contests/problems")
    String getStatus(@Nonnull Contest contest);

    void addProblem(long contestId, long problemId);

    Contest save(Contest contest);

    List<UserStanding> standing(long id);

    CompletableFuture<List<UserStanding>> standingAsync(long id);

    String toProblemIndex(long num);

    void updateSelective(long id, Contest contest);

}
