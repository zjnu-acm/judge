/*
 * Copyright 2014 zhanhb.
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
package cn.edu.zjnu.acm.judge.data.dto;

import cn.edu.zjnu.acm.judge.domain.User;
import java.util.Collection;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author zhanhb
 */
public class UserModel extends org.springframework.security.core.userdetails.User {

    private static final long serialVersionUID = 1L;

    @Getter
    private final User user;
    @Getter
    @Setter
    private volatile long lastSubmitTime;

    public UserModel(User user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getId(), user.getPassword(), authorities);
        this.user = user;
    }

    public String getUserId() {
        return getUsername();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + user.getId() + "]";
    }

}
