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
package cn.edu.zjnu.acm.judge.controller.user;

import cn.edu.zjnu.acm.judge.service.AccountService;
import cn.edu.zjnu.acm.judge.util.URLBuilder;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author zhanhb
 */
@Controller
public class UserListController {

    private static final Sort DEFAULT_SORT = Sort.by(
            Sort.Order.desc("solved"),
            Sort.Order.asc("submit"),
            Sort.Order.asc("user_id")
    );

    @Autowired
    private AccountService accountService;

    @GetMapping({"/userlist", "/users"})
    @SuppressWarnings("AssignmentToMethodParameter")
    public String userList(HttpServletRequest request, @PageableDefault(50) Pageable pageable) {
        Sort sort = pageable.getSort();
        int pageSize = Math.min(pageable.getPageSize(), 500);

        if (sort == null || !sort.iterator().hasNext()) {
            sort = DEFAULT_SORT;
        }

        pageable = PageRequest.of(pageable.getPageNumber(), pageSize, sort);

        String query = URLBuilder.fromRequest(request)
                .replaceQueryParam("page")
                .toString();

        request.setAttribute("url", query);
        request.setAttribute("page", accountService.findAll(pageable));
        return "users/list";
    }

}
