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
 * See the License for the specific language governing permissions  and
 * limitations under the License.
 */
package cn.edu.zjnu.acm.judge.mapper;

import cn.edu.zjnu.acm.judge.domain.Language;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

/**
 *
 * @author zhanhb
 */
@Mapper
public interface LanguageMapper {

    @CacheEvict(value = "languages", allEntries = true)
    @Insert("insert into language(id,name,source_extension,compile_command,execute_command,executable_extension,time_factor,ext_memory,description) values(#{id},#{name},#{sourceExtension},#{compileCommand},#{executeCommand},#{executableExtension},#{timeFactor},#{extMemory},#{description})")
    long save(Language language);

    @CacheEvict(value = "languages", allEntries = true)
    @Delete("<script>delete from language"
            + "<where>"
            + "<if test='id!=null'> and id=#{id}</if><if test='id==null'> and id is null</if>"
            + "<if test='name!=null'> and name=#{name}</if><if test='name==null'> and name is null</if>"
            + "<if test='sourceExtension!=null'> and source_extension=#{sourceExtension}</if><if test='sourceExtension==null'> and source_extension is null</if>"
            + "<if test='compileCommand!=null'> and compile_command=#{compileCommand}</if><if test='compileCommand==null'> and compile_command is null</if>"
            + "<if test='executeCommand!=null'> and execute_command=#{executeCommand}</if><if test='executeCommand==null'> and execute_command is null</if>"
            + "<if test='executableExtension!=null'> and executable_extension=#{executableExtension}</if><if test='executableExtension==null'> and executable_extension is null</if>"
            + "<if test='timeFactor!=null'> and time_factor=#{timeFactor}</if><if test='timeFactor==null'> and time_factor is null</if>"
            + "<if test='extMemory!=null'> and ext_memory=#{extMemory}</if><if test='extMemory==null'> and ext_memory is null</if>"
            + "<if test='description!=null'> and description=#{description}</if><if test='description==null'> and description is null</if>"
            + "</where>"
            + "</script>")
    long delete(Language language);

    @Cacheable("languages")
    @Select("select id id,name name,source_extension sourceExtension,compile_command compileCommand,execute_command executeCommand,executable_extension executableExtension,time_factor timeFactor,ext_memory extMemory,description description from language where not disabled")
    List<Language> findAll();

    @Cacheable("languages")
    @Select("select id id,name name,source_extension sourceExtension,compile_command compileCommand,execute_command executeCommand,executable_extension executableExtension,time_factor timeFactor,ext_memory extMemory,description description from language where id=#{param1}")
    Language findOne(long id);

}
