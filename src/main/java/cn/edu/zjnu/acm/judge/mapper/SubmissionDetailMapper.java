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
package cn.edu.zjnu.acm.judge.mapper;

import cn.edu.zjnu.acm.judge.domain.SubmissionDetail;
import javax.annotation.Nullable;
import org.apache.ibatis.annotations.Mapper;

/**
 *
 * @author zhanhb
 */
@Mapper
public interface SubmissionDetailMapper {

    int saveSource(long id, String source);

    long delete(long id);

    @Nullable
    String findSourceById(long id);

    @Nullable
    String findCompileInfoById(long id);

    @Nullable
    String getSubmissionDetail(long id);

    long update(SubmissionDetail build);

}
