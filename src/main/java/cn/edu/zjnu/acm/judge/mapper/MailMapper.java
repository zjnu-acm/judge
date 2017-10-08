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

import cn.edu.zjnu.acm.judge.config.Constants;
import cn.edu.zjnu.acm.judge.data.dto.MailInfo;
import cn.edu.zjnu.acm.judge.domain.Mail;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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
public interface MailMapper {

    @Nullable
    @Select("select mail_id id,from_user `from`,to_user `to`,in_date inDate,title,content from mail where mail_id=#{id} and not disabled")
    Mail findOne(@Param("id") long id);

    @Select("select mail_id id,title,new_mail newMail,from_user `from`,in_date inDate from mail where to_user=#{user} and not disabled order by in_date desc limit #{start},#{size}")
    List<Mail> findAllByTo(
            @Param("user") String user,
            @Param("start") long start,
            @Param("size") int size);

    @CacheEvict(value = Constants.Cache.MAIL, allEntries = true)
    @Update("update mail set new_mail=0 where mail_id=#{id}")
    long readed(@Param("id") long id);

    @CacheEvict(value = Constants.Cache.MAIL, allEntries = true)
    @Update("update mail set disabled=1 where mail_id=#{id}")
    long delete(long id);

    @CacheEvict(value = Constants.Cache.MAIL, allEntries = true)
    @Insert("insert into mail (mail_id,from_user,to_user,title,content,in_date) "
            + "values(#{id},#{from},#{to},#{title},#{content},now())")
    @SelectKey(statement = "select COALESCE(max(mail_id)+1,1000) maxp from mail",
            keyProperty = "id", before = true, resultType = long.class)
    long save(Mail mail);

    @Cacheable(Constants.Cache.MAIL)
    @Select("select count(*) total,sum(if(new_mail!=0,1,0)) newMail from mail where to_user=#{user} and not disabled")
    MailInfo getMailInfo(@Param("user") String user);

    @CacheEvict(value = Constants.Cache.MAIL, allEntries = true)
    @Update("update mail set reply=1 where mail_id=#{id}")
    long setReply(@Param("id") long id);

}
