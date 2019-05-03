/*
 * Copyright 2016-2019 ZJNU ACM.
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

import cn.edu.zjnu.acm.judge.data.dto.MailInfo;
import cn.edu.zjnu.acm.judge.domain.Mail;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @author zhanhb
 */
@Mapper
public interface MailMapper {

    @Nullable
    Mail findOne(@Param("id") long id);

    List<Mail> findAllByTo(
            @Param("user") String user,
            @Param("start") long start,
            @Param("size") int size);

    long readed(@Param("id") long id);

    long delete(long id);

    long save(Mail mail);

    @Nullable
    MailInfo getMailInfo(@Param("user") String user);

    long setReply(@Param("id") long id);

}
