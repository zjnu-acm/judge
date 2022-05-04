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

import cn.edu.zjnu.acm.judge.data.form.ProblemForm;
import cn.edu.zjnu.acm.judge.domain.Problem;
import java.time.Instant;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author zhanhb
 */
@Mapper
public interface ProblemMapper {

    long save(Problem problem);

    @Nullable
    Problem findOne(@Param("id") long id, @Nullable @Param("lang") String lang);

    @Nullable
    Problem findOneNoI18n(@Param("id") long id);

    long setInDate(@Param("id") long problemId, @Param("inDate") Instant timestamp);

    long updateSelective(@Param("id") long id, @Param("p") Problem build, @Nullable @Param("lang") String lang);

    long updateImgUrl(
            @Param("problemId") long problemId,
            @Param("imgSrc") String imgSrc,
            @Param("newImgSrc") String newImgSrc);

    List<Problem> findAll(
            @Param(value = "form") ProblemForm form,
            @Nullable @Param(value = "userId") String userId,
            @Nullable @Param(value = "lang") String lang,
            @Param(value = "pageable") Pageable pageable);

    long count(@Param("form") ProblemForm form, @Nullable @Param("lang") String lang);

    long touchI18n(
            @Param("problemId") long problemId,
            @Nonnull @Param("lang") String lang);

    long delete(@Param("id") long id);

    long deleteI18n(@Param("id") long id);

}
