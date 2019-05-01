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
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @author zhanhb
 */
@Mapper
public interface MessageMapper {

    @Deprecated
    long nextId();

    @Nullable
    Message findOne(@Param("id") long id);

    List<Message> findAllByThreadIdAndOrderNumGreaterThanOrderByOrderNum(
            @Param("thread") long thread, @Param("order") long order);

    long updateOrderNumByThreadIdAndOrderNumGreaterThan(@Param("threadId") long thread, @Param("order") long order);

    long updateThreadIdByThreadId(@Param("threadId") long nextId, @Param("original") long original);

    long save(@Param("id") long id,
            @Param("parentId") Long parentId,
            @Param("order") long order,
            @Param("problemId") Long problemId,
            @Param("depth") long depth,
            @Param("userId") String userId,
            @Param("title") String title,
            @Param("content") String content);

    List<Message> findAllByThreadIdBetween(
            @Nullable @Param("min") Long min, // inclusive
            @Nullable @Param("max") Long max, // exclude
            @Nullable @Param("problemId") Long problemId,
            @Nullable @Param("limit") Integer limit);

    long mint(
            @Param("top") long top,
            @Nullable @Param("problemId") Long problemId,
            @Param("limit") int limit,
            @Param("coalesce") long coalesce);

    long maxt(@Param("top") long top, @Nullable @Param("problemId") Long problemId, @Param("limit") int limit, @Param("coalesce") long coalesce);

}
