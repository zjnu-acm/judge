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

import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.domain.Standing;
import cn.edu.zjnu.acm.judge.domain.User;
import java.util.List;
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
            + " cp.problem_id orign," // TODO remove if Problem.orign
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
            + "left join problem_i18n pi on p.problem_id=pi.id and pi.locale=#{lang} "
            + "where cp.contest_id=#{contest} "
            + "order by cp.num</script>")
    List<Problem> getProblems(
            @Param("contest") long contestId,
            @Nullable @Param("userId") String userId,
            @Param("lang") String lang);

    @Update("update contest_problem cp left join ( "
            + "select cp1.contest_id,cp1.problem_id,count(cp2.problem_id) num "
            + "from contest_problem cp1 left join contest_problem cp2 "
            + "on cp1.contest_id=cp2.contest_id and cp1.num > cp2.num "
            + "group by contest_id,problem_id "
            + ") tmp on cp.problem_id=tmp.problem_id and cp.contest_id=tmp.contest_id "
            + "set cp.num=tmp.num+#{base} "
            + "where cp.contest_id=#{contest}")
    long updateContestOrder(
            @Param("contest") long contestId,
            @Param("base") long base);

    @Delete("delete from contest_problem where contest_id=#{contest} and problem_id=#{problem}")
    long deleteContestProblem(
            @Param("contest") long contestId,
            @Param("problem") long problemId);

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

    // TODO not necessary support for i18n,
    // for return value only the id is used
    @Select("select cp.num id,if(cp.title is null or trim(cp.title)='',p.title,cp.title) title,p.problem_id orign "
            + "from contest_problem cp "
            + "join problem p on cp.problem_id=p.problem_id "
            + "where cp.contest_id=#{contest} and cp.num=#{problem}")
    Problem getProblem(
            @Param("contest") long contestId,
            @Param("problem") long problemNum);

    @Select("select" + COLUMNS + "from contest where contest_id=#{id} and not disabled")
    Contest findOneByIdAndDisabledFalse(@Param("id") long contestId);

    @Select("select "
            + " s.user_id id,u.nick "
            + "from "
            + " solution s "
            + "left join "
            + " users u "
            + "on s.user_id=u.user_id "
            + "where s.contest_id=#{id} "
            + "group by s.user_id")
    List<User> attenders(@Param("id") long contestId);

    @Insert("insert ignore into contest_problem (contest_id,problem_id,title,num) "
            + "values(#{id},#{problem},nullif(#{title},''),#{num})")
    long addProblem(@Param("id") long contestId, @Param("problem") long problem,
            @Param("title") String title,
            @Param("num") int num);

    @Update("update contest set disabled=0 where contest_id=#{id}")
    long enable(@Param("id") long contestId);

    @Update("update contest set disabled=1 where contest_id=#{id}")
    long disable(@Param("id") long contestId);

    @Deprecated
    @Nullable
    @Select("select num-1000 from contest_problem where contest_id=#{cid} and problem_id=#{pid}")
    Long getProblemIdInContest(@Param("cid") long contestId, @Param("pid") long problemId);

    @Select("select" + COLUMNS + "from contest order by contest_id desc")
    List<Contest> findAll();

    @Select("select" + COLUMNS + "from contest where now()<start_time and start_time<end_time and not disabled order by contest_id")
    List<Contest> pending();

    @Select("select" + COLUMNS + "from contest where start_time<end_time and end_time<now() and not disabled order by contest_id desc")
    List<Contest> past();

    @Select("select" + COLUMNS + "from contest where start_time<now() and end_time>now() and not disabled order by contest_id desc")
    List<Contest> current();

    @Select("select" + COLUMNS + "from contest where start_time<end_time and not disabled order by contest_id desc")
    List<Contest> contests();

    @Select("select" + COLUMNS + "from contest where start_time<end_time and not disabled and end_time>now() order by contest_id desc")
    List<Contest> runningAndScheduling();

    @Update("update contest_problem set title=null where problem_id=#{problem} and contest_id=#{contest}")
    long updateProblemTitle(@Param("contest") long contestId, @Param("problem") long problemId);

}
