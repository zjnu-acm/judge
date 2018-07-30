/*
 * Copyright 2017 ZJNU ACM.
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

import cn.edu.zjnu.acm.judge.config.Constants;
import cn.edu.zjnu.acm.judge.data.excel.Account;
import cn.edu.zjnu.acm.judge.data.form.AccountForm;
import cn.edu.zjnu.acm.judge.data.form.AccountImportForm;
import cn.edu.zjnu.acm.judge.data.form.UserPasswordForm;
import cn.edu.zjnu.acm.judge.domain.User;
import cn.edu.zjnu.acm.judge.service.AccountService;
import cn.edu.zjnu.acm.judge.service.ResetPasswordService;
import cn.edu.zjnu.acm.judge.util.excel.ExcelUtil;
import cn.edu.zjnu.acm.judge.util.excel.Type;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

/**
 *
 * @author zhanhb
 */
@RequestMapping(value = "/api/accounts", produces = APPLICATION_JSON_VALUE)
@RestController
@Secured("ROLE_ADMIN")
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private ResetPasswordService resetPasswordService;

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

    @GetMapping(produces = Constants.MineTypes.XLSX)
    public ResponseEntity<?> findAllXlsx(AccountForm form, Pageable pageable, Locale locale) {
        List<Account> content = accountService.findAllByExport(form, pageable);
        return ExcelUtil.toResponse(Account.class, content, locale, Type.XLSX);
    }

    @GetMapping(produces = Constants.MineTypes.XLS)
    public ResponseEntity<?> findAllXls(AccountForm form, Pageable pageable, Locale locale) {
        List<Account> content = accountService.findAllByExport(form, pageable);
        return ExcelUtil.toResponse(Account.class, content, locale, Type.XLS);
    }

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public List<Account> parseExcel(@RequestParam("file") MultipartFile multipartFile, Locale locale) throws IOException {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            return accountService.parseExcel(inputStream, locale);
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(value = "import", consumes = APPLICATION_JSON_VALUE)
    public void importUsers(@RequestBody AccountImportForm form) {
        accountService.importUsers(form);
    }

    @GetMapping("password/status")
    public Map<String, ?> passwordStatus() {
        return resetPasswordService.stats();
    }

}
