package cn.edu.zjnu.acm.judge.controller.api;

import cn.edu.zjnu.acm.judge.data.excel.Account;
import cn.edu.zjnu.acm.judge.data.form.AccountForm;
import cn.edu.zjnu.acm.judge.data.form.AccountImportForm;
import cn.edu.zjnu.acm.judge.service.AccountService;
import cn.edu.zjnu.acm.judge.util.excel.ExcelType;
import cn.edu.zjnu.acm.judge.util.excel.ExcelUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@Secured("ROLE_ADMIN")
@RequiredArgsConstructor
public class AccountExcelController {

    private final AccountService accountService;

    @GetMapping("api/accounts.xlsx")
    public ResponseEntity<?> findAllXlsx(
            AccountForm form, Pageable pageable, @Nullable Locale requestLocale) throws IOException {
        Locale locale = Optional.ofNullable(requestLocale).orElse(Locale.ROOT);
        List<Account> content = accountService.findAllForExport(form, pageable);
        return ExcelUtil.toResponse(Account.class, content, locale, ExcelType.XLSX,
                accountService.getExcelName(locale));
    }

    @GetMapping("api/accounts.xls")
    public ResponseEntity<?> findAllXls(
            AccountForm form, Pageable pageable, @Nullable Locale requestLocale) throws IOException {
        Locale locale = Optional.ofNullable(requestLocale).orElse(Locale.ROOT);
        List<Account> content = accountService.findAllForExport(form, pageable);
        return ExcelUtil.toResponse(Account.class, content, locale, ExcelType.XLS,
                accountService.getExcelName(locale));
    }

    @PostMapping(value = "api/accounts", consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    public List<Account> parseExcel(
            @RequestParam("file") MultipartFile multipartFile,
            @Nullable Locale locale) throws IOException {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            return accountService.parseExcel(inputStream, Optional.ofNullable(locale).orElse(Locale.ROOT));
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(value = "api/accounts/import", consumes = APPLICATION_JSON_VALUE)
    public void importUsers(@RequestBody AccountImportForm form) {
        accountService.importUsers(form);
    }

}
