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

import cn.edu.zjnu.acm.judge.data.excel.Account;
import cn.edu.zjnu.acm.judge.data.form.AccountForm;
import cn.edu.zjnu.acm.judge.data.form.AccountImportForm;
import cn.edu.zjnu.acm.judge.domain.User;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.ibatis.annotations.Delete;
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
    String FORM_CONDITION = "<where>"
            + "<if test='form.userId!=null and form.userId!=\"\"'>user_id like concat('%',#{form.userId},'%')</if>"
            + "<if test='form.nick!=null and form.nick!=\"\"'> and nick like concat('%',#{form.nick},'%')</if>"
            + "<if test='form.query!=null and form.query!=\"\"'> and (user_id like #{form.query} or nick like #{form.query})</if>"
            + "<if test='form.disabled!=null'> and <if test='!form.disabled'> not </if> disabled</if>"
            + "</where>";

    @Nullable
    @Select("select user_id id,nick,email,password,school,ip,solved,submit,accesstime,created_time createdTime,modified_time modifiedTime from users where user_id=#{id}")
    User findOne(@Param("id") String id);

    @Insert("insert into users(USER_ID,PASSWORD,EMAIL,REG_TIME,NICK,SCHOOL,ip) values"
            + "(#{id},#{password},#{email},now(),#{nick},#{school},ip=#{ip})")
    long save(User user);

    @Select("<script>"
            + "SELECT COUNT(*) total FROM users"
            + FORM_CONDITION
            + "</script>")
    long count(@Param("form") AccountForm form);

    String ON = " on (u1.solved>u2.solved or u1.solved=u2.solved and u1.submit<u2.submit or u1.solved=u2.solved and u1.submit=u2.submit and u1.user_id<u2.user_id) ";

    @Select("select count(u1.user_id) "
            + "from users u1 join users u2 "
            + ON + "and not u1.disabled where u2.user_id=#{id}")
    long rank(@Param("id") String userId);

    @Select("select user_id id,nick,solved,submit from (( "
            + "select u1.* from users u1 join users u2 "
            + ON + "and not u1.disabled "
            + "where u2.user_id=#{id} order by u1.solved,u1.submit desc,u1.user_id desc limit #{c} "
            + ") union ("
            + "select * from users where user_id=#{id} "
            + ") union ( "
            + "select u2.* from users u1 join users u2 "
            + ON + " and not u2.disabled "
            + "where u1.user_id=#{id} order by u2.solved desc,u2.submit,u2.user_id limit #{c} "
            + ") order by solved desc,submit,user_id) z")
    List<User> neighbours(@Nonnull @Param("id") String userId, @Param("c") int count);

    @Select("select temp.user_id id,sol solved,sub submit,nick from ( select user_id,sum(if(score=100,1,0)) sol,count(*) sub from ( select * from solution order by solution_id desc limit #{count} ) s group by user_id order by sol desc,sub asc limit 50 ) temp,users where temp.user_id=users.user_id")
    List<User> recentrank(@Param("count") int count);

    String FIND_ALL_CONDITION
            = "<if test='form.disabled==null or form.disabled==true'>,disabled</if>"
            + "from users"
            + FORM_CONDITION
            + "<if test='pageable.sort!=null'>"
            + "<foreach item='item' collection='pageable.sort' open='order by' separator=','>"
            + "<choose>"
            + "<when test='item.property==&quot;user_id&quot;'>user_id</when>"
            + "<when test='item.property==&quot;nick&quot;'>nick</when>"
            + "<when test='item.property==&quot;submit&quot;'>submit</when>"
            + "<when test='item.property==&quot;ratio&quot;'>solved/submit</when>"
            + "<otherwise>solved</otherwise>"
            + "</choose>"
            + "<if test='not item.ascending'>desc</if>"
            + "</foreach></if>"
            + "limit #{pageable.offset},#{pageable.size}";

    @Select("<script>select" + LIST_COLUMNS + FIND_ALL_CONDITION + "</script>")
    List<User> findAll(@Param("form") AccountForm form, @Param("pageable") Pageable pageable);

    @Select("<script>select" + LIST_COLUMNS + ",password,email,school" + FIND_ALL_CONDITION + "</script>")
    List<Account> findAllByExport(@Param("form") AccountForm form, @Param("pageable") Pageable pageable);

    @Update("<script>"
            + "update users"
            + "<set>"
            + "<if test='user.email!=null'>email=nullif(#{user.email},''),</if>"
            + "<if test='user.nick!=null'>nick=#{user.nick},</if>"
            + "<if test='user.school!=null'>school=#{user.school},</if>"
            + "<if test='user.password!=null'>password=#{user.password},</if>"
            + "<if test='user.modifiedTime!=null'>modified_time=#{user.modifiedTime},</if>"
            + "<if test='user.accesstime!=null'>accesstime=#{user.accesstime},</if>"
            + "<if test='user.ip!=null'>ip=#{user.ip},</if>"
            + "<if test='user.disabled!=null'>disabled=#{user.disabled},</if>"
            + "</set>"
            + "<where>user_id=#{userId}</where>"
            + "</script>")
    int updateSelective(@Nonnull @Param("userId") String userId, @Param("user") User user);

    @Select("<script>select user_id id from users where user_id in"
            + "<foreach item='item' collection='userIds' open='(' separator=',' close=')'>"
            + "#{item}"
            + "</foreach>"
            + "</script>")
    List<String> findAllByUserIds(@Param("userIds") Collection<String> userIds);

    @Select("<script>select count(user_id) cnt from users where user_id in"
            + "<foreach item='item' collection='userIds' open='(' separator=',' close=')'>"
            + "#{item}"
            + "</foreach>"
            + "</script>")
    long countAllByUserIds(@Param("userIds") Collection<String> userIds);

    /**
     * {@link AccountImportForm.ExistPolicy}
     */
    @Update("<script>"
            + "update users u join"
            + "<foreach item='item' index='index' collection='accounts' open='(' separator='union' close=')'>"
            + "<if test='index==0'>(select #{item.id} id,#{item.password} password,#{item.nick} nick,#{item.school} school,#{item.email} email)</if>"
            + "<if test='index!=0'>(select #{item.id},#{item.password},#{item.nick},#{item.school},#{item.email})</if>"
            + "</foreach>"
            + "t on u.user_id=t.id"
            + "<set>"
            + "<if test='(mask&amp;1)!=0'>u.disabled=false,</if>"
            + "<if test='(mask&amp;2)!=0'>u.password=t.password,</if>"
            + "<if test='(mask&amp;4)!=0'>"
            + "u.nick=t.nick,u.school=t.school,u.email=nullif(t.email,''),"
            + "</if>"
            + "modified_time=now()"
            + "</set>"
            + "</script>")
    int batchUpdate(@Param("accounts") List<Account> accounts, @Param("mask") int mask);

    @Insert("<script>"
            + "insert into users(user_id,nick,password,school,email)values"
            + "<foreach item='item' collection='accounts' separator=','>"
            + "(#{item.id},#{item.nick},#{item.password},#{item.school},nullif(#{item.email},''))"
            + "</foreach>"
            + "</script>")
    void insert(@Param("accounts") List<Account> accounts);

    @Delete("delete from users where user_id=#{param1}")
    int delete(String userId);

}
