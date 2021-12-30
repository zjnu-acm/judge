/*
 * Copyright 2017-2019 ZJNU ACM.
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
package cn.edu.zjnu.acm.judge.controller.api;

import cn.edu.zjnu.acm.judge.data.form.AccountForm;
import cn.edu.zjnu.acm.judge.data.form.UserPasswordForm;
import cn.edu.zjnu.acm.judge.domain.User;
import cn.edu.zjnu.acm.judge.service.AccountService;
import cn.edu.zjnu.acm.judge.service.ResetPasswordService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author zhanhb
 */
@RequestMapping(value = "/api/accounts", produces = APPLICATION_JSON_VALUE)
@RestController
@Secured("ROLE_ADMIN")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final ResetPasswordService resetPasswordService;

    @GetMapping
    public Page<User> findAll(AccountForm form, @PageableDefault(50) Pageable pageable) {
        return accountService.findAll(form, pageable);
    }

    @PatchMapping("{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("userId") String userId, @RequestBody User user) {
        accountService.updateSelective(userId, user.toBuilder().password(null).build());
    }

    @PatchMapping("{userId}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePassword(@PathVariable("userId") String userId, @RequestBody UserPasswordForm user) {
        accountService.updatePassword(userId, user.getPassword());
    }

    @GetMapping("password/status")
    public Map<String, ?> passwordStatus() {
        return resetPasswordService.stats();
    }

}
