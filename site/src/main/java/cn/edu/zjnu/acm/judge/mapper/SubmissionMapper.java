/*
 * Copyright 2015 ZJNU ACM.
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
package cn.edu.zjnu.acm.judge.mapper;

import cn.edu.zjnu.acm.judge.data.dto.ScoreCount;
import cn.edu.zjnu.acm.judge.data.form.BestSubmissionForm;
import cn.edu.zjnu.acm.judge.data.form.SubmissionQueryForm;
import cn.edu.zjnu.acm.judge.domain.Submission;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author zhanhb
 */
@Mapper
public interface SubmissionMapper {

    long save(Submission submission);

    @Nullable
    Submission findOne(@Param("id") long id);

    long updateResult(
            @Param("id") long id,
            @Param("score") int score,
            @Param("time") long time,
            @Param("memory") long memory);

    List<Submission> findAllByCriteria(SubmissionQueryForm submissionQueryForm);

    List<Long> findAllByProblemIdAndResultNotAccept(@Param("problemId") long problemId);

    List<Submission> bestSubmission(@Param("form") BestSubmissionForm form, @Param("pageable") Pageable pageable);

    List<ScoreCount> groupByScore(
            @Nullable @Param("contestId") Long contestId,
            @Param("problemId") long problemId);

    long clearByContestId(@Param("contest") long contest);

    List<Long> findAllByContestId(@Param("contest") long id);

    long delete(@Param("id") long id);

}
