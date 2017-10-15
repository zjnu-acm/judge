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
package cn.edu.zjnu.acm.judge.service;

import cn.edu.zjnu.acm.judge.domain.User;
import cn.edu.zjnu.acm.judge.mapper.UserMapper;
import cn.edu.zjnu.acm.judge.mapper.UserRoleMapper;
import com.google.common.collect.ImmutableList;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author zhanhb
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final List<GrantedAuthority> ROLE_ADMIN = ImmutableList.copyOf(AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_SOURCE_BROWSER", "ROLE_USER"));
    private static final List<GrantedAuthority> ROLE_SOURCE_BROWSER = ImmutableList.copyOf(AuthorityUtils.createAuthorityList("ROLE_SOURCE_BROWSER", "ROLE_USER"));
    private static final List<GrantedAuthority> ROLE_USER = ImmutableList.copyOf(AuthorityUtils.createAuthorityList("ROLE_USER"));
    private static final List<List<GrantedAuthority>> ROLES = ImmutableList.of(ROLE_USER, ROLE_SOURCE_BROWSER, ROLE_ADMIN);

    public static boolean isAdminLoginned(HttpServletRequest request) {
        return request.isUserInRole("ADMIN");
    }

    public static boolean isSourceBrowser(HttpServletRequest request) {
        return request.isUserInRole("SOURCE_BROWSER");
    }

    public static boolean isUser(Authentication authentication, String userId) {
        return userId != null && Optional.ofNullable(authentication).map(Principal::getName)
                .map(userId::equals).orElse(false);
    }

    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private UserMapper userMapper;

    @Nonnull
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userMapper.findOne(userId);
        if (user == null) {
            throw new UsernameNotFoundException(userId);
        }
        int role = 0;
        for (String rightstr : userRoleMapper.findAllByUserId(user.getId())) {
            if (rightstr != null) {
                switch (rightstr.toLowerCase()) {
                    case "administrator":
                        role = 2;
                        break;
                    case "source_browser":
                        role = Math.max(role, 1);
                        break;
                    case "news_publisher":
                        // TODO
                        break;
                    default:
                        break;
                }
            }
        }

        return org.springframework.security.core.userdetails.User.withUsername(userId).password(user.getPassword()).authorities(ROLES.get(role)).build();
    }

}
