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

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 *
 * @author zhanhb
 */
@Mapper
public interface UserRoleMapper {

    @Select("select rightstr from privilege where user_id=#{userId} and not disabled")
    List<String> findAllByUserId(@Param("userId") String userId);

    @Select("<script>select count(*) from privilege where user_id in"
            + "<foreach item='item' collection='array' open='(' separator=',' close=')'>"
            + "#{item}"
            + "</foreach>"
            + " and rightstr='administrator'</script>")
    int countAdmin(@Param("array") String... toArray);

}
