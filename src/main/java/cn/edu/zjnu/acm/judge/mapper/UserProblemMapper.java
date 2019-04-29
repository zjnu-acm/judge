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
import java.util.List;
import javax.annotation.Nullable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @author zhanhb
 */
@Mapper
public interface UserProblemMapper {

    long init();

    @Nullable
    UserProblem findOne(@Param("userId") String userId, @Param("problemId") long problemId);

    long update(@Param("userId") String userId, @Param("problem") long problemId);

    long updateUser(@Param("userId") String userId);

    long updateProblem(@Param("problemId") long problemId);

    long updateUsers();

    long updateProblems();

    List<Long> findAllSolvedByUserId(@Param("userId") String userId);

    List<UserProblem> findAllByUserId(@Param("userId") String userId);

    long deleteByProblem(long problemId);

    long deleteByUser(String userId);

}
