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
import cn.edu.zjnu.acm.judge.util.ResultType;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author zhanhb
 */
@Mapper
public interface SubmissionMapper {

    String COLUMNS = " s.solution_id id,s.problem_id problem,s.user_id user,s.contest_id contest,s.time,s.memory,s.score,s.language,s.ip,s.code_length sourceLength,s.in_date inDate ";
    String LIST_COLUMNS = " s.solution_id id,s.problem_id problem,s.user_id user,s.contest_id contest,s.time,s.memory,s.score,s.language,s.code_length sourceLength,s.in_date inDate ";

    @Nullable
    @Select("select source from submission_source where solution_id=#{id}")
    String findSourceById(@Param("id") long id);

    @Nullable
    @Select("SELECT error FROM compileinfo WHERE solution_id=#{id}")
    String findCompileInfoById(@Param("id") long id);

    @Insert("INSERT INTO solution (solution_id,problem_id,user_id,in_date,code_length,score,language,ip,contest_id) VALUES"
            + " (#{id},#{problem},#{user},#{inDate},#{sourceLength},#{score},#{language},#{ip},#{contest})")
    @SelectKey(statement = "select COALESCE(max(solution_id)+1,1000) maxp from solution",
            before = true, keyProperty = "id", resultType = long.class)
    long save(Submission submission);

    @Nullable
    @Select("SELECT" + COLUMNS + "FROM solution s WHERE solution_id=#{id}")
    Submission findOne(@Param("id") long id);

    @Insert("insert into submission_source(solution_id,source) values(#{id},#{source})")
    long saveSource(@Param("id") long id, @Param("source") String source);

    @Update("update solution set score=#{score},time=#{time},memory=#{memory} where solution_id=#{id}")
    long updateResult(
            @Param("id") long id,
            @Param("score") int score,
            @Param("time") long time,
            @Param("memory") long memory);

    @Insert("replace into solution_details (solution_id,details) values(#{id},#{detail})")
    long saveDetail(@Param("id") long id, @Param("detail") String detail);

    @Insert("replace into compileinfo (solution_id,error) values(#{id},#{errorInfo})")
    long saveCompileInfo(@Param("id") long id, @Param("errorInfo") String errorInfo);

    @Nullable
    @Select("SELECT details FROM solution_details WHERE solution_id=#{id}")
    String getSubmissionDetail(@Param("id") long id);

    @Select({
        "<script>"
        + "<if test='bottom!=null'>select * from (</if>"
        + "select" + LIST_COLUMNS + "from solution s "
        + "<where>"
        + "<if test='contest!=null'> and contest_id=#{contest}</if>"
        + "<if test='problem!=null'> and problem_id=#{problem}</if>"
        + "<if test='user!=null'> and user_id=#{user}</if>"
        + "<if test='language!=null'> and language=#{language}</if>"
        + "<if test='score!=null'> and score=#{score}</if>"
        + "<if test='bottom!=null'> and solution_id&gt;#{bottom}</if>"
        + "<if test='top!=null'> and solution_id&lt;#{top}</if>"
        + "</where>"
        + "order by solution_id"
        + "<if test='bottom==null'>desc</if>"
        + "limit #{size}"
        + "<if test='bottom!=null'>) tmp order by id desc</if>"
        + "</script>"
    })
    List<Submission> findAllByCriteria(SubmissionQueryForm submissionQueryForm);

    @Select("select solution_id from solution s where problem_id=#{problemId} and score<> "
            + ResultType.SCORE_ACCEPT
            + " order by solution_id desc")
    List<Long> findAllByProblemIdAndResultNotAccept(@Param("problemId") long problemId);

    @SelectProvider(type = BestSubmissionsBuilder.class, method = "bestSubmissions")
    List<Submission> bestSubmission(@Param("form") BestSubmissionForm form, @Param("pageable") Pageable pageable);

    @Select("<script>"
            + "select score,count(*) count from solution where problem_id=#{problemId}"
            + "<if test='contestId!=null'> and contest_id = #{contestId}</if> group by score"
            + "</script>")
    List<ScoreCount> groupByScore(
            @Nullable @Param("contestId") Long contestId,
            @Param("problemId") long problemId);

    @Update("update solution set contest_id=null where contest_id=#{contest}")
    long clearByContestId(@Param("contest") long contest);

    @Select("select solution_id from solution where contest_id=#{contest}")
    List<Long> findAllByContestId(@Param("contest") long id);

    @Delete("delete from submission_source where solution_id=#{id}")
    int deleteSource(@Param("id") long id);

    @Delete("delete from compileinfo WHERE solution_id=#{id}")
    int deleteCompileinfo(@Param("id") long id);

    @Delete("delete from solution WHERE solution_id=#{id}")
    int deleteSolution(@Param("id") long id);

    @Delete("delete from solution_details WHERE solution_id=#{id}")
    int deleteDetail(long id);

}
