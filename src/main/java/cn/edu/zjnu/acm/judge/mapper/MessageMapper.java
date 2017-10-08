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

import cn.edu.zjnu.acm.judge.domain.Message;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 *
 * @author zhanhb
 */
@Mapper
public interface MessageMapper {

    String COLUMNS = " message_id id,in_date inDate,parent_id parent,user_id user,"
            + "content,title,problem_id problem,depth,thread_id thread,orderNum `order` ";
    String LIST_COLUMNS = " message_id id,title,depth,user_id user,in_date inDate,thread_id thread,problem_id problem ";

    @Deprecated
    @Select("select COALESCE(max(message_id)+1,1000) maxp from message")
    long nextId();

    @Nullable
    @Select("select" + COLUMNS + "from message where message_id=#{id}")
    Message findOne(@Param("id") long id);

    @Select("select" + COLUMNS + "from message where thread_id=#{thread} and orderNum>#{orderNum} order by orderNum")
    List<Message> findAllByThreadIdAndOrderNumGreaterThanOrderByOrderNum(
            @Param("thread") long thread, @Param("orderNum") long orderNum);

    @Update("update message set orderNum=orderNum+1 where thread_id=#{threadId} and orderNum>#{orderNum}")
    long updateOrderNumByThreadIdAndOrderNumGreaterThan(@Param("threadId") long thread, @Param("orderNum") long orderNum);

    @Update("update message set thread_id=#{threadId} where thread_id=#{original}")
    long updateThreadIdByThreadId(@Param("threadId") long nextId, @Param("original") long original);

    @Insert("insert into message (thread_id,message_id,parent_id,orderNum,problem_id,depth,user_id,title,content,in_date) values(#{id},#{id},#{parentId},#{orderNum},#{problemId},#{depth},#{userId},#{title},#{content},now())")
    long save(@Param("id") long id,
            @Param("parentId") Long parentId,
            @Param("orderNum") long orderNum,
            @Param("problemId") Long problemId,
            @Param("depth") long depth,
            @Param("userId") String userId,
            @Param("title") String title,
            @Param("content") String content);

    @Select("<script>"
            + "select" + LIST_COLUMNS + "from message"
            + "<where>"
            + "<if test='min!=null'>and thread_id&gt;=#{min} </if>"
            + "<if test='max!=null'>and thread_id&lt;#{max} </if>"
            + "<if test='problemId!=null'>and problem_id=#{problemId} </if>"
            + "</where>"
            + "order by thread_id desc,orderNum"
            + "<if test='limit!=null'> limit #{limit}</if>"
            + "</script>")
    List<Message> findAllByThreadIdBetween(
            @Nullable @Param("min") Long min, // inclusive
            @Nullable @Param("max") Long max, // exclude
            @Nullable @Param("problemId") Long problemId,
            @Nullable @Param("limit") Integer limit);

    @Select("<script>"
            + "select COALESCE(min(thread_id),#{coalesce}) as mint from ("
            + "select thread_id from message"
            + "<where>"
            + "thread_id&lt;#{top}"
            + "<if test='problemId!=null'> and problem_id=#{problemId} </if>"
            + "</where>"
            + " order by thread_id desc limit #{limit}"
            + ") as temp"
            + "</script>")
    long mint(
            @Param("top") long top,
            @Nullable @Param("problemId") Long problemId,
            @Param("limit") int limit,
            @Param("coalesce") long coalesce);

    @Select("<script>"
            + "select COALESCE(max(thread_id),#{coalesce}) as maxt from ("
            + "select thread_id from message"
            + "<where>"
            + "thread_id&gt;=#{top} "
            + "<if test='problemId!=null'>and problem_id=#{problemId}</if>"
            + "</where>"
            + "order by thread_id limit #{limit}"
            + ") temp"
            + "</script>")
    long maxt(@Param("top") long top, @Nullable @Param("problemId") Long problemId, @Param("limit") int limit, @Param("coalesce") long coalesce);

}
