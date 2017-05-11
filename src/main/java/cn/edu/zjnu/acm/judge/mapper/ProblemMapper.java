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

import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.domain.ScoreCount;
import java.time.Instant;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author zhanhb
 */
@Mapper
public interface ProblemMapper {

    String COLUMNS = " p.problem_id id,"
            + "COALESCE(pi.title,p.title) title,"
            + "COALESCE(pi.description,p.description) description,"
            + "COALESCE(pi.input,p.input) input,"
            + "COALESCE(pi.output,p.output) output,"
            + "p.sample_input sampleInput,"
            + "p.sample_output sampleOutput,"
            + "COALESCE(pi.hint,p.hint) hint,"
            + "COALESCE(pi.source,p.source) source,"
            + "p.in_date inDate,p.time_limit timeLimit,"
            + "p.memory_limit memoryLimit,"
            + "p.contest_id contest,p.disabled,"
            + "p.accepted,p.submit,p.solved,p.submit_user submitUser,"
            + "p.created_time createdTime,p.modified_time modifiedTime ";

    String COLUMNS_NO_I18N = " p.problem_id id,"
            + "p.in_date inDate,p.time_limit timeLimit,"
            + "p.memory_limit memoryLimit,"
            + "p.contest_id contest,p.disabled,"
            + "p.accepted,p.submit,p.solved,p.submit_user submitUser ";

    String LIST_COLUMNS = " p.problem_id id,"
            + "COALESCE(pi.title,p.title) title,"
            + "p.in_date inDate,"
            + "p.time_limit timeLimit,"
            + "p.memory_limit memoryLimit,"
            + "p.contest_id contest,"
            + "p.disabled,"
            + "p.accepted,"
            + "p.submit,"
            + "p.solved,"
            + "p.submit_user submitUser ";

    String LIST_COLUMNS_SOURCE = LIST_COLUMNS + ","
            + "COALESCE(pi.source,p.source) source ";

    String STATUS = "<if test='userId!=null'>,if(up.submit is null or up.submit=0,0,if(up.accepted!=0,1,2)) status </if>";

    String FROM = " from problem p left join problem_i18n pi on p.problem_id=pi.id and pi.locale=#{lang} ";

    @Update("update problem set disabled=#{disabled} where problem_id=#{id}")
    long setDisabled(@Param("id") long id, @Param("disabled") boolean disabled);

    @Deprecated
    @Select("select COALESCE(max(problem_id)+1,1000) maxp from problem")
    long nextId();

    @Select("SELECT COUNT(*) total from problem")
    long count();

    @Insert("INSERT INTO problem (problem_id,title,description,input,output,sample_input,sample_output,"
            + "hint,source,in_date,time_limit,memory_limit,contest_id) values ("
            + "#{id},#{title},#{description},#{input},#{output},#{sampleInput},#{sampleOutput},"
            + "#{hint},#{source},now(),#{timeLimit},#{memoryLimit},#{contest})")
    @SelectKey(statement = "select COALESCE(max(problem_id)+1,1000) maxp from problem",
            before = true, keyProperty = "id", resultType = long.class)
    long save(Problem problem);

    @Deprecated
    @Update("update problem set contest_id=#{cid} where problem_id=#{id}")
    long setContest(@Param("id") long problem, @Param("cid") Long contest);

    // TODO not used
    @Nullable
    @Select("select" + COLUMNS + FROM + " where problem_id=#{id} AND not disabled")
    Problem findOneByIdAndDisabledFalse(@Param("id") long pid, @Param("lang") String lang);

    @Select("SELECT" + LIST_COLUMNS + FROM + " order by problem_id limit #{pageable.offset},#{pageable.pageSize}")
    List<Problem> findAll(@Param("pageable") Pageable pageable, @Param("lang") String lang);

    // TODO not used
    @Select("<script>"
            + "select" + LIST_COLUMNS
            + STATUS
            + FROM
            + "<if test='userId!=null'>left join user_problem up "
            + "on up.user_id=#{userId} and up.problem_id=p.problem_id </if>"
            + "where not p.disabled and p.problem_id&gt;=#{start} and p.problem_id&lt;=#{end}"
            + "</script>"
    )
    List<Problem> findAllByDisabledFalse(
            @Nullable @Param("userId") String userId,
            @Param("start") long start,
            @Param("end") long end,
            @Param("lang") String lang);

    @Nullable
    @Select("select" + COLUMNS + FROM + " where problem_id=#{id}")
    Problem findOne(@Param("id") long id, @Param("lang") String lang);

    @Nullable
    @Select("select" + COLUMNS_NO_I18N + "from problem p where problem_id=#{id}")
    Problem findOneNoI18n(@Param("id") long id);

    @Update("UPDATE problem SET in_date=#{inDate} where problem_id=#{id}")
    long setInDate(@Param("id") long problemId, @Param("inDate") Instant timestamp);

    @Update("<script>UPDATE problem p "
            + "<if test='lang!=null and lang!=&quot;&quot;'>"
            + "left join problem_i18n pi on p.problem_id = pi.id and pi.locale=#{lang}"
            + "</if>"
            + "SET "
            + "<if test='lang==null or lang==&quot;&quot;'>"
            + "p.title=#{p.title},"
            + "p.description=#{p.description},"
            + "p.input=#{p.input},"
            + "p.output=#{p.output},"
            + "p.hint=#{p.hint},"
            + "p.source=#{p.source},"
            + "</if>"
            + "<if test='lang!=null and lang!=&quot;&quot;'>"
            + "pi.title=nullif(nullif(#{p.title},''),p.title),"
            + "pi.description=nullif(nullif(#{p.description},''),p.description),"
            + "pi.input=nullif(nullif(#{p.input},''),p.input),"
            + "pi.output=nullif(nullif(#{p.output},''),p.output),"
            + "pi.hint=nullif(nullif(#{p.hint},''),p.hint),"
            + "pi.source=nullif(nullif(#{p.source},''),p.source),"
            + "</if>"
            + "p.sample_input=#{p.sampleInput},"
            + "p.sample_output=#{p.sampleOutput},"
            + "p.time_limit=#{p.timeLimit},"
            + "p.memory_limit=#{p.memoryLimit},"
            + "p.modified_time=#{p.modifiedTime},"
            + "p.contest_id=#{p.contest} "
            + "WHERE p.problem_id=#{p.id}</script>")
    long update(@Param("p") Problem build, @Param("lang") String lang);

    @Select({"<script>"
        + "select" + LIST_COLUMNS_SOURCE + STATUS
        + FROM
        + "<if test='userId!=null'>left join user_problem up on up.problem_id=p.problem_id and up.user_id=#{userId}</if>"
        + "WHERE (instr(p.title,#{query})&gt;0 or instr(p.source,#{query})&gt;0) "
        + "and not p.disabled"
        + "<if test='userId!=null'>group by p.problem_id</if>"
        + "ORDER BY p.problem_id"
        + "</script>"
    })
    List<Problem> findAllBySearchTitleOrSourceAndDisabledFalse(
            @Param("query") String query,
            @Param("userId") String userId,
            @Param("lang") String lang);

    @Select("select score,count(*) count from solution where problem_id=#{problemId} group by score")
    List<ScoreCount> groupByScore(@Param("problemId") long problemId);

    @Insert("insert ignore into problem_i18n(id,locale) values(#{problemId},#{lang})")
    long touchI18n(
            @Param("problemId") long problemId,
            @Param("lang") String lang);

    @Update("<script>"
            + "update problem_i18n"
            + "<set>"
            + "<if test='problem.title!=null'>title=nullif(#{problem.title},''),</if>"
            + "<if test='problem.description!=null'>description=nullif(#{problem.description},''),</if>"
            + "<if test='problem.input!=null'>input=nullif(#{problem.input},''),</if>"
            + "<if test='problem.output!=null'>output=nullif(#{problem.output},''),</if>"
            + "<if test='problem.hint!=null'>hint=nullif(#{problem.hint},''),</if>"
            + "<if test='problem.source!=null'>source=nullif(#{problem.source},''),</if>"
            + "</set>"
            + "where id=#{problemId} and locale=#{lang}"
            + "</script>")
    long updateI18n(
            @Param("problemId") long problemId,
            @Param("lang") String lang,
            @Param("problem") Problem problem);

}
