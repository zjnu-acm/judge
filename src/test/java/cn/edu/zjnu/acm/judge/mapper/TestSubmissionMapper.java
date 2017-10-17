/*
 * Copyright 2017 ZJNU ACM.
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

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @author zhanhb
 */
@Mapper
public interface TestSubmissionMapper {

    @Delete("delete from submission_source where solution_id=#{id}")
    int deleteSource(@Param("id") long id);

    @Delete("delete from compileinfo WHERE solution_id=#{id}")
    int deleteCompileinfo(@Param("id") long id);

    @Delete("delete from solution WHERE solution_id=#{id}")
    int deleteSolution(@Param("id") long id);

    @Delete("delete from solution_details WHERE solution_id=#{id}")
    int deleteDetail(long id);

}
