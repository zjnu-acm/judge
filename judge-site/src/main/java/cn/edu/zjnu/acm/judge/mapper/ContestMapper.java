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

    @Nullable
    Contest findOne(@Param("id") long contestId);

    @Nullable
    Contest findOneByIdAndNotDisabled(@Param("id") long contestId);

    List<User> attenders(@Param("id") long contestId);

    List<Contest> findAllByQuery(@Param("includeDisabled") boolean includeDisabled, @Param("mask") int mask);

    long deleteByPrimaryKey(@Param("id") long id);

    long updateSelective(@Param("id") long id, @Nonnull @Param("c") Contest contest);

}
