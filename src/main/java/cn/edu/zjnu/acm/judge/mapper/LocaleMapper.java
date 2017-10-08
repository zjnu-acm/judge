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

import cn.edu.zjnu.acm.judge.config.Constants;
import cn.edu.zjnu.acm.judge.domain.DomainLocale;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

/**
 *
 * @author zhanhb
 */
@Mapper
public interface LocaleMapper {

    @Cacheable(Constants.Cache.LOCALE)
    @Select("select id,name from locale where not disabled")
    List<DomainLocale> findAll();

    @CacheEvict(value = Constants.Cache.LOCALE, allEntries = true)
    @Insert("insert into locale(id,name,created_time,modified_time)values(#{id},#{name},now(),now())")
    int save(DomainLocale locale);

    @Cacheable(Constants.Cache.LOCALE)
    @Nullable
    @Select("select id,name from locale where id=#{id} and not disabled")
    public DomainLocale findOne(@Param("id") String id);

}
