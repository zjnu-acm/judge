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
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @author zhanhb
 */
@Mapper
public interface ContestMapper {

    long save(Contest contest);

    List<Problem> getProblems(
            @Param("contest") long contestId,
            @Nullable @Param("userId") String userId,
            @Nullable @Param("lang") String lang);

    List<Standing> standing(@Param("id") long contestId);

    @Nullable
    Contest findOne(@Param("id") long contestId);

    @Nullable
    Problem getProblem(
            @Param("contest") long contestId,
            @Param("problem") long problemNum,
            @Nullable @Param("lang") String lang);

    @Nullable
    Contest findOneByIdAndNotDisabled(@Param("id") long contestId);

    List<User> attenders(@Param("id") long contestId);

    long addProblem(@Param("id") long contestId, @Param("problem") long problem,
            @Nullable @Param("title") String title);

    long addProblems(@Param("contestId") long contestId, @Param("base") int base, @Nonnull @Param("problems") long[] problems);

    List<Contest> findAllByQuery(@Param("includeDisabled") boolean includeDisabled, @Param("mask") int mask);

    long deleteContestProblems(@Param("id") long id);

    long deleteByPrimaryKey(@Param("id") long id);

    long updateSelective(@Param("id") long id, @Nonnull @Param("c") Contest contest);

    List<Long> submittedProblems(@Param("id") long id);

}
