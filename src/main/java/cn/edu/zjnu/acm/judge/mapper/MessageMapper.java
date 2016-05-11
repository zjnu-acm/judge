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
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 *
 * @author zhanhb
 */
@Mapper
public interface MessageMapper {

    String COLUMNS = " message_id id,in_date inDate,parent_id parent,user_id user,"
            + "content,title,problem_id problem,depth,thread_id thread,orderNum `order` ";

    @Deprecated
    @Select("select COALESCE(max(message_id)+1,1000) maxp from message")
    long nextId();

    @Select("select" + COLUMNS + "from message where message_id=#{id}")
    Message findOne(@Param("id") long id);

    @Select("select" + COLUMNS + "from message where thread_id=#{thread} and orderNum>#{orderNum} order by orderNum")
    List<Message> findAllByThreadIdAndOrderNumGreaterThanOrderByOrderNum(
            @Param("thread") long thread, @Param("orderNum") long orderNum);

}
