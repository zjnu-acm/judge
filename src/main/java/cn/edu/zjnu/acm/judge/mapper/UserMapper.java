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
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author zhanhb
 */
@Mapper
public interface UserMapper {

    @Nullable
    User findOne(@Param("id") String id);

    long save(User user);

    long count(@Param("form") AccountForm form);

    long rank(@Param("id") String userId);

    List<User> neighbours(@Nonnull @Param("id") String userId, @Param("c") int count);

    List<User> recentrank(@Param("count") int count);

    List<User> findAll(@Param("form") AccountForm form, @Param("pageable") Pageable pageable);

    List<Account> findAllByExport(@Param("form") AccountForm form, @Param("pageable") Pageable pageable);

    int updateSelective(@Nonnull @Param("userId") String userId, @Param("user") User user);

    List<String> findAllByUserIds(@Param("userIds") Collection<String> userIds);

    long countAllByUserIds(@Param("userIds") Collection<String> userIds);

    /**
     * {@link AccountImportForm.ExistPolicy}
     */
    int batchUpdate(@Param("accounts") List<Account> accounts, @Param("mask") int mask);

    int insert(@Param("accounts") List<Account> accounts);

    int delete(String userId);

}
