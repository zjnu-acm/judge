/*
 * Copyright 2019 ZJNU ACM.
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
package cn.edu.zjnu.acm.judge.service;

import cn.edu.zjnu.acm.judge.data.form.ProblemForm;
import cn.edu.zjnu.acm.judge.domain.Problem;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author zhanhb
 */
public interface ProblemService {

    void delete(long id);

    Page<Problem> findAll(ProblemForm problemForm, @Nullable String userId, Pageable pageable, @Nullable Locale locale);

    @Nonnull
    Problem findOne(long id, @Nullable String lang);

    @Nonnull
    Problem findOne(long id);

    @Nonnull
    Problem findOneNoI18n(long id);

    Path getDataDirectory(long id);

    @Nonnull
    Problem save(@Nonnull Problem problem);

    void updateSelective(long problemId, Problem p, @Nullable String requestLocale);

    List<String> attachment(long problemId, String requestLocale);

}
