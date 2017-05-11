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

import cn.edu.zjnu.acm.judge.domain.User;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author zhanhb
 */
@Mapper
public interface UserMapper {

    String LIST_COLUMNS = " user_id id,solved,submit,nick ";

    @Select("select user_id id,nick,email,vcode,expire_time expireTime,password,school,ip,solved,submit,accesstime,created_time createdTime,modified_time modifiedTime from users where user_id=#{id}")
    User findOne(@Param("id") String id);

    @Insert("insert into users(USER_ID,PASSWORD,EMAIL,REG_TIME,NICK,SCHOOL,ip) values"
            + "(#{id},#{password},#{email},now(),#{nick},#{school},ip=#{ip})")
    long save(User user);

    @Update("UPDATE users SET password=#{password},email=#{email},nick=#{nick},"
            + "school=#{school},ip=#{ip},accesstime=#{accesstime},vcode=#{vcode},expire_time=#{expireTime},"
            + "created_time=#{createdTime},modified_time=#{modifiedTime} "
            + "WHERE user_id=#{id}")
    long update(User user);

    @Select("SELECT COUNT(*) total FROM users WHERE not disabled")
    long countByDisabledFalse();

    String ON = " (u1.solved>u2.solved or u1.solved=u2.solved and u1.submit<u2.submit or u1.solved=u2.solved and u1.submit=u2.submit and u1.user_id<u2.user_id) ";

    @Select("select count(u1.user_id) "
            + "from users u1 join users u2 "
            + "on" + ON + "and not u1.disabled where u2.user_id=#{id}")
    long rank(@Param("id") String userId);

    @Select("select user_id id,nick,solved,submit from (( "
            + "select u1.* from users u1 join users u2 "
            + "on " + ON + "and not u1.disabled "
            + "where u2.user_id=#{id} order by u1.solved,u1.submit desc,u1.user_id desc limit #{c} "
            + ") union ("
            + "select * from users where user_id=#{id} "
            + ") union ( "
            + "select u2.* from users u1 join users u2 "
            + "on " + ON + " and not u2.disabled "
            + "where u1.user_id=#{id} order by u2.solved desc,u2.submit,u2.user_id limit #{c} "
            + ") order by solved desc,submit,user_id) z")
    List<User> neighbours(@Param("id") String userId, @Param("c") int count);

    @Select("select temp.user_id id,sol solved,sub submit,nick from ( select user_id,sum(if(score=100,1,0)) sol,count(*) sub from ( select * from solution order by solution_id desc limit #{count} ) s group by user_id order by sol desc,sub asc limit 50 ) temp,users where temp.user_id=users.user_id")
    List<User> recentrank(@Param("count") int count);

    @Select("<script>select" + LIST_COLUMNS
            + "from users where not disabled "
            + "<if test='sort!=null'><foreach item='item' index='index' collection='sort' open='order by' separator=','>"
            + "<choose>"
            + "<when test='item.property==&quot;user_id&quot;'>user_id</when>"
            + "<when test='item.property==&quot;nick&quot;'>nick</when>"
            + "<when test='item.property==&quot;submit&quot;'>submit</when>"
            + "<when test='item.property==&quot;ratio&quot;'>solved/submit</when>"
            + "<otherwise>solved</otherwise>"
            + "</choose>"
            + "<if test='not item.ascending'>desc</if>"
            + "</foreach></if>"
            + "limit #{offset},#{size}"
            + "</script>")
    List<User> findAll(Pageable pageable);

    @Select("select " + LIST_COLUMNS + " from users WHERE (user_id like #{query} or nick like #{query}) and not disabled order by solved desc,submit asc")
    List<User> findAllBySearch(@Param("query") String query);

}
