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
package cn.edu.zjnu.acm.judge.mapper;

import cn.edu.zjnu.acm.judge.domain.UserProblem;
import cn.edu.zjnu.acm.judge.util.ResultType;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 *
 * @author zhanhb
 */
@Mapper
public interface UserProblemMapper {

    String COLUMNS = " user_id user,problem_id problem,accepted,submit ";
    String SET_USER = "set u.solved=COALESCE(t.solved,0),u.submit=COALESCE(t.submit,0)";
    String SET_PROBLEM = "set p.submit=COALESCE(t.submit,0),p.accepted=COALESCE(t.accepted,0),"
            + "p.submit_user=COALESCE(t.submit_user,0),p.solved=COALESCE(t.solved,0)";

    @Insert("replace into user_problem(user_id,problem_id,accepted,submit) "
            + "select user_id,problem_id,sum(if(score=100,1,0)),count(solution_id) "
            + "from solution where score!=" + ResultType.QUEUING + " group by user_id,problem_id")
    long init();

    @Nullable
    @Select("select" + COLUMNS + "from user_problem where user_id=#{userId} and problem_id=#{problemId}")
    UserProblem findOne(@Param("userId") String userId, @Param("problemId") long problemId);

    @Insert("replace into user_problem(user_id,problem_id,accepted,submit) "
            + "select user_id,problem_id,sum(if(score=100,1,0)) accepted,count(solution_id) submit "
            + "from solution where user_id=#{userId} and problem_id=#{problem} and score!=" + ResultType.QUEUING + " group by user_id,problem_id")
    long update(@Param("userId") String userId, @Param("problem") long problemId);

    @Update("update users u left join ("
            + "select sum(up.submit) submit,sum(if(up.accepted!=0,1,0)) solved "
            + "from user_problem up where user_id=#{userId}"
            + ") t on 1=1 " + SET_USER
            + " where u.user_id=#{userId}")
    long updateUser(@Param("userId") String userId);

    @Update("update problem p left join ("
            + "select sum(up.submit) submit,sum(up.accepted) accepted,"
            + "sum(if(up.submit!=0,1,0)) submit_user,sum(if(up.accepted!=0,1,0)) solved "
            + "from user_problem up where problem_id=#{problemId}"
            + ") t on 1=1 " + SET_PROBLEM + " where p.problem_id=#{problemId}")
    long updateProblem(@Param("problemId") long problemId);

    @Update("update users u left join ("
            + "select user_id,sum(submit) submit,sum(if(accepted!=0,1,0)) solved from user_problem group by user_id"
            + ") t on u.user_id=t.user_id "
            + SET_USER)
    long updateUsers();

    @Update("update problem p left join ("
            + "select problem_id,sum(submit) submit,sum(accepted) accepted,"
            + "sum(if(submit!=0,1,0)) submit_user,sum(if(accepted!=0,1,0)) solved "
            + "from user_problem group by problem_id"
            + ") t on p.problem_id=t.problem_id "
            + SET_PROBLEM)
    long updateProblems();

    @Select("select problem_id from user_problem where user_id=#{userId} and accepted!=0 order by problem_id")
    List<Long> findAllSolvedByUserId(@Param("userId") String userId);

    @Select("select" + COLUMNS + "from user_problem where user_id=#{userId} order by problem_id")
    List<UserProblem> findAllByUserId(@Param("userId") String userId);

    @Delete("delete from user_problem where problem_id=#{param1}")
    long deleteByProblem(long problemId);

    @Delete("delete from user_problem where user_id=#{param1}")
    long deleteByUser(String userId);

}
