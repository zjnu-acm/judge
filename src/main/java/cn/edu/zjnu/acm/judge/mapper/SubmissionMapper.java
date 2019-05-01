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

    String LIST_COLUMNS = " s.solution_id id,s.problem_id problem,s.user_id user,s.contest_id contest,s.time,s.memory,s.score,s.language,s.code_length sourceLength,s.in_date inDate ";

    @Nullable
    String findSourceById(@Param("id") long id);

    @Nullable
    String findCompileInfoById(@Param("id") long id);

    long save(Submission submission);

    @Nullable
    Submission findOne(@Param("id") long id);

    long saveSource(@Param("id") long id, @Param("source") String source);

    long updateResult(
            @Param("id") long id,
            @Param("score") int score,
            @Param("time") long time,
            @Param("memory") long memory);

    long saveDetail(@Param("id") long id, @Param("detail") String detail);

    long saveCompileInfo(@Param("id") long id, @Param("errorInfo") String errorInfo);

    @Nullable
    String getSubmissionDetail(@Param("id") long id);

    List<Submission> findAllByCriteria(SubmissionQueryForm submissionQueryForm);

    List<Long> findAllByProblemIdAndResultNotAccept(@Param("problemId") long problemId);

    List<Submission> bestSubmission(@Param("form") BestSubmissionForm form, @Param("pageable") Pageable pageable);

    List<ScoreCount> groupByScore(
            @Nullable @Param("contestId") Long contestId,
            @Param("problemId") long problemId);

    long clearByContestId(@Param("contest") long contest);

    List<Long> findAllByContestId(@Param("contest") long id);

    int deleteSource(@Param("id") long id);

    int deleteCompileinfo(@Param("id") long id);

    int deleteSolution(@Param("id") long id);

    int deleteDetail(@Param("id") long id);

}
