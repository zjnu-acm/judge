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

import cn.edu.zjnu.acm.judge.data.dto.Standing;
import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.domain.User;
import cn.edu.zjnu.acm.judge.util.ResultType;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

/**
 *
 * @author zhanhb
 */
@Mapper
public interface ContestMapper {

    String COLUMNS = " contest_id id,title,start_time startTime,end_time endTime,disabled,description ";

    @Insert("INSERT INTO contest (contest_id,title,description,start_time,end_time)"
            + " VALUES (#{id},#{title},#{description},#{startTime},#{endTime})")
    @SelectKey(statement = "select COALESCE(max(contest_id)+1,1000) maxp from contest",
            keyProperty = "id", before = true, resultType = long.class)
    long save(Contest contest);

    @Select("<script>select"
            + " cp.problem_id origin," // TODO remove if Problem.origin
            + " cp.num id,"
            + " COALESCE(nullif(cp.title,''),nullif(pi.title,''),p.title) title"
            + "<if test='userId!=null'>,temp.status</if>"
            + "from "
            + " contest_problem cp "
            + "<if test='userId!=null'>left join ( "
            + " select "
            + "  problem_id,"
            + "  if(count(solution_id)=0,0,if(sum(if(score=100,1,0)) !=0,1,2)) status "
            + " from solution "
            + " where user_id=#{userId} and contest_id=#{contest} "
            + " group by problem_id "
            + ") temp "
            + "on cp.problem_id=temp.problem_id</if>"
            + "left join problem p "
            + "on cp.problem_id=p.problem_id "
            + "left join problem_i18n pi on p.problem_id=pi.id and pi.locale<choose><when test='lang==null'> is null</when><otherwise>=#{lang}</otherwise></choose> "
            + "where cp.contest_id=#{contest} "
            + "order by cp.num</script>")
    List<Problem> getProblems(
            @Param("contest") long contestId,
            @Nullable @Param("userId") String userId,
            @Nullable @Param("lang") String lang);

    @Select("select "
            + " ac.user_id `user`,"
            + " cp.num `problem`,"
            + " UNIX_TIMESTAMP(ac.ac_time) - UNIX_TIMESTAMP(( "
            + "  select start_time from contest where contest_id=#{id} "
            + " )) `time`,"
            + " sum(if(wa.in_date < ac.ac_time,1,0)) penalty "
            + "from ( "
            + " select "
            + "  user_id,"
            + "  problem_id,"
            + "  min(in_date) ac_time "
            + " from solution "
            + " where contest_id=#{id} "
            + " and score=100 "
            + " group by user_id,problem_id "
            + ") ac "
            + "left join "
            + " solution wa "
            + "on "
            + " ac.user_id=wa.user_id and "
            + " ac.problem_id=wa.problem_id "
            + "join contest_problem cp "
            + "on cp.contest_id=#{id} and cp.problem_id=ac.problem_id "
            + "where wa.contest_id=#{id} "
            + "group by ac.user_id,ac.problem_id "
            + "union "
            + "select "
            + " s.user_id user,"
            + " cp.num `problem`,"
            + " null time,"
            + " count(s.solution_id) penalty "
            + "from solution s join contest_problem cp "
            + "on s.contest_id=cp.contest_id and s.problem_id=cp.problem_id "
            + "where s.contest_id=#{id} "
            + "group by s.user_id,s.problem_id "
            + "having sum(if(score=100,1,0))=0")
    List<Standing> standing(@Param("id") long contestId);

    @Nullable
    @Select("select" + COLUMNS + "from contest where contest_id=#{id}")
    Contest findOne(@Param("id") long contestId);

    @Nullable
    @Select("<script>select cp.num id,if(cp.title is null or trim(cp.title)='',p.title,cp.title) title,p.problem_id origin,"
            + ProblemMapper.EXTERN_COLUMNS + ",count(distinct sa.solution_id)accepted,count(distinct s.solution_id) submit,count(distinct sa.user_id)solved,count(distinct s.user_id)submitUser "
            + "from contest_problem cp "
            + "join problem p on cp.problem_id=p.problem_id "
            + "left join problem_i18n pi on p.problem_id=pi.id and pi.locale<choose><when test='lang==null'> is null</when><otherwise>=#{lang}</otherwise></choose>"
            + " left join solution s on s.problem_id=p.problem_id and s.contest_id=#{contest}"
            + " left join solution sa on sa.problem_id=p.problem_id and sa.contest_id=#{contest} and sa.score=" + ResultType.SCORE_ACCEPT
            + " where cp.contest_id=#{contest} and cp.num=#{problem} group by p.problem_id</script>")
    Problem getProblem(
            @Param("contest") long contestId,
            @Param("problem") long problemNum,
            @Nullable @Param("lang") String lang);

    @Nullable
    @Select("select" + COLUMNS + "from contest where contest_id=#{id} and not disabled")
    Contest findOneByIdAndNotDisabled(@Param("id") long contestId);

    @Select("select s.user_id id,u.nick from solution s left join users u "
            + "on s.user_id=u.user_id where s.contest_id=#{id} group by s.user_id")
    List<User> attenders(@Param("id") long contestId);

    @Insert("insert ignore into contest_problem (contest_id,problem_id,title,num) "
            + "select #{id},#{problem},nullif(#{title},''),COALESCE(max(num)+1,1000) num "
            + "from contest_problem cp where contest_id=#{id}")
    long addProblem(@Param("id") long contestId, @Param("problem") long problem,
            @Nullable @Param("title") String title);

    @Insert("<script>insert into contest_problem (contest_id,problem_id,title,num)"
            + "select #{contest_id} AS contest_id,origin,null title,num from "
            + "<foreach item='item' index='index' collection='problems' open='(' separator=' union ' close=')'>"
            + "select problem_id origin,${base}+#{index} as num from problem where problem_id=#{item}"
            + "</foreach> as t"
            + "</script>")
    long addProblems(@Param("contest_id") long contest_id, @Param("base") int base, @Nonnull @Param("problems") long[] problems);

    @Select("<script>select" + COLUMNS + "from contest"
            + "<where>"
            + "<choose>"
            + "<when test='mask==0'>1=0</when>" // none
            + "<when test='mask==1'>now()&lt;start_time and start_time&lt;end_time</when>" // pending
            + "<when test='mask==2'>start_time&lt;=now() and now()&lt;end_time</when>" // runnging
            + "<when test='mask==3'>now()&lt;end_time and start_time&lt;end_time</when>" // pending,runnging
            + "<when test='mask==4'>start_time&lt;end_time and end_time&lt;=now()</when>" // ended
            + "<when test='mask==5'>(now()&lt;start_time or end_time&lt;=now()) and start_time&lt;end_time</when>" // pending,ended
            + "<when test='mask==6'>start_time&lt;now() and start_time&lt;end_time</when>" // runnging,ended
            + "<when test='mask==7'>start_time&lt;end_time</when>" // pending,runnging,ended
            + "<when test='mask==8'>start_time&gt;=end_time</when>" // error
            + "<when test='mask==9'>(now()&lt;start_time or start_time&gt;=end_time)</when>" // pending,error
            + "<when test='mask==10'>(start_time&lt;=now() and now()&lt;end_time or start_time&gt;=end_time)</when>" // runnging,error
            + "<when test='mask==11'>(now()&lt;end_time or start_time&gt;=end_time)</when>" // pending,runnging,error
            + "<when test='mask==12'>(start_time&gt;=end_time or end_time&lt;=now())</when>" // ended,error
            + "<when test='mask==13'>(now()&lt;start_time or end_time&lt;=now() or start_time&gt;=end_time)</when>" // pending,ended,error
            + "<when test='mask==14'>(start_time&lt;now() or start_time&gt;=end_time)</when>" // runnging,ended,error
            + "<when test='mask==15'>1=1</when>" // pending,runnging,ended,error
            + "</choose>"
            + "<if test='!includeDisabled'> and not disabled</if>"
            + "</where>"
            + " order by contest_id desc</script>")
    List<Contest> findAllByQuery(@Param("includeDisabled") boolean includeDisabled, @Param("mask") int mask);

    @Delete("delete from contest_problem where contest_id=#{id}")
    long deleteContestProblems(long id);

    @Delete("delete from contest where contest_id=#{id}")
    long deleteByPrimaryKey(@Param("id") long id);

    @Update("<script>update contest"
            + "<set>"
            + "<if test='c.title!=null'>title=#{c.title},</if>"
            + "<if test='c.description!=null'>description=#{c.description},</if>"
            + "<if test='c.startTime!=null'>start_time=#{c.startTime},</if>"
            + "<if test='c.endTime!=null'>end_time=#{c.endTime},</if>"
            + "<if test='c.disabled!=null'>disabled=#{c.disabled},</if>"
            + "<if test='c.description!=null'>description=#{c.description},</if>"
            + "<if test='c.createdTime!=null'>created_time=#{c.createdTime},</if>"
            + "<if test='c.modifiedTime!=null'>modified_time=#{c.modifiedTime},</if>"
            + "</set>"
            + "<where>contest_id=#{id}</where>"
            + "</script>")
    long updateSelective(@Param("id") long id, @Nonnull @Param("c") Contest contest);

    @Select("select distinct(problem_id) from solution where contest_id=#{id}")
    List<Long> submittedProblems(@Param("id") long id);

}
