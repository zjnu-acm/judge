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

import cn.edu.zjnu.acm.judge.config.Constants;
import cn.edu.zjnu.acm.judge.domain.Language;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

/**
 *
 * @author zhanhb
 */
@Mapper
public interface LanguageMapper {

    @CacheEvict(value = Constants.Cache.LANGUAGE, allEntries = true)
    @Insert("insert into language(id,name,source_extension,compile_command,execute_command,executable_extension,time_factor,ext_memory,description) values(#{id},#{name},#{sourceExtension},#{compileCommand},#{executeCommand},#{executableExtension},#{timeFactor},#{extMemory},#{description})")
    @SelectKey(statement = "select COALESCE(max(id)+1,1) maxp from language",
            keyProperty = "id", before = true, resultType = int.class)
    long save(Language language);

    @CacheEvict(value = Constants.Cache.LANGUAGE, allEntries = true)
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

    @CacheEvict(value = Constants.Cache.LANGUAGE, allEntries = true)
    @Delete("delete from language where id=#{param1}")
    long deleteById(long id);

    @CacheEvict(value = Constants.Cache.LANGUAGE, allEntries = true)
    @Update("update language set disabled=1 where id=#{param1}")
    long disabled(long id);

    @Cacheable(Constants.Cache.LANGUAGE)
    @Select("select id id,name name,source_extension sourceExtension,compile_command compileCommand,execute_command executeCommand,executable_extension executableExtension,time_factor timeFactor,ext_memory extMemory,description description from language where not disabled")
    List<Language> findAll();

    @Cacheable(Constants.Cache.LANGUAGE)
    @Nullable
    @Select("select id id,name name,source_extension sourceExtension,compile_command compileCommand,execute_command executeCommand,executable_extension executableExtension,time_factor timeFactor,ext_memory extMemory,description description from language where id=#{param1}")
    Language findOne(long id);

    @CacheEvict(value = Constants.Cache.LANGUAGE, allEntries = true)
    @Update("<script>update language "
            + "<set>"
            + "<if test='param2.id!=null'>id=#{param2.id},</if>"
            + "<if test='param2.name!=null'>name=#{param2.name},</if>"
            + "<if test='param2.sourceExtension!=null'>source_extension=#{param2.sourceExtension},</if>"
            + "<if test='param2.compileCommand!=null'>compile_command=#{param2.compileCommand},</if>"
            + "<if test='param2.executeCommand!=null'>execute_command=#{param2.executeCommand},</if>"
            + "<if test='param2.executableExtension!=null'>executable_extension=#{param2.executableExtension},</if>"
            + "<if test='param2.timeFactor!=null'>time_factor=#{param2.timeFactor},</if>"
            + "<if test='param2.extMemory!=null'>ext_memory=#{param2.extMemory},</if>"
            + "<if test='param2.description!=null'>description=#{param2.description},</if>"
            + "<if test='param2.disabled!=null'>disabled=#{param2.disabled},</if>"
            + "</set> where id=#{param1}"
            + "</script>")
    long update(long id, Language language);

}
