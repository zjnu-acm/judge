package cn.edu.zjnu.acm.judge.controller.api;

import cn.edu.zjnu.acm.judge.Application;
import cn.edu.zjnu.acm.judge.data.excel.Account;
import cn.edu.zjnu.acm.judge.data.form.AccountForm;

import java.util.Arrays;
import java.util.Locale;

import cn.edu.zjnu.acm.judge.data.form.AccountImportForm;
import cn.edu.zjnu.acm.judge.domain.User;
import cn.edu.zjnu.acm.judge.service.MockDataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(JUnitPlatform.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
@WithMockUser(roles = "ADMIN")
public class AccountExcelControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private MockDataService mockDataService;
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test of findAllXlsx method, of class AccountExcelController.
     *
     * {@link AccountExcelController#findAllXlsx(AccountForm, Pageable, Locale)}
     * {@link AccountExcelController#parseExcel(MultipartFile, Locale)}
     */
    @Test
    public void testFindAllXlsx() throws Exception {
        log.info("findAllXlsx");
        test("/api/accounts.xlsx");
    }

    /**
     * Test of findAllXls method, of class AccountExcelController.
     *
     * {@link AccountExcelController#findAllXls(AccountForm, Pageable, Locale)}
     * {@link AccountExcelController#parseExcel(MultipartFile, Locale)}
     */
    @Test
    public void testFindAllXls() throws Exception {
        log.info("findAllXls");
        test("/api/accounts.xls");
    }

    /**
     * Test of findAllXls method, of class AccountController.
     *
     * {@link AccountExcelController#findAllXlsx(AccountForm, Pageable, Locale)}
     * {@link AccountExcelController#findAllXls(AccountForm, Pageable, Locale)}
     * {@link AccountExcelController#parseExcel(MultipartFile, Locale)}
     */
    private void test0(String url) throws Exception {
        byte[] content = mvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();
        MockMultipartFile file = new MockMultipartFile("file", content);
        mvc.perform(multipart("/api/accounts?_format=json").file(file))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    private void test(String url) throws Exception {
        test0(url);
        mockDataService.user();
        test0(url);
    }

    /**
     * Test of importUsers method, of class AccountController.
     *
     * {@link AccountExcelController#importUsers(AccountImportForm)}
     */
    @Test
    public void testImportUsers() throws Exception {
        log.info("importUsers");
        AccountImportForm form = new AccountImportForm();
        Account account = toAccount(mockDataService.user(false));
        Account account2 = toAccount(mockDataService.user(false));
        form.setContent(Arrays.asList(account, account2));
        expect(form, HttpStatus.NO_CONTENT);
        account.setExists(true);
        account2.setExists(true);
        expect(form, HttpStatus.BAD_REQUEST);
        form.getExistsPolicy().add(AccountImportForm.ExistPolicy.ENABLE);
        expect(form, HttpStatus.NO_CONTENT);
    }

    private MvcResult expect(AccountImportForm form, HttpStatus status) throws Exception {
        return mvc.perform(post("/api/accounts/import?_format=json")
                        .content(objectMapper.writeValueAsString(form)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(status.value()))
                .andReturn();
    }

    private Account toAccount(User user) {
        Account account = new Account();
        account.setId(user.getId());
        account.setPassword(user.getPassword());
        account.setEmail(user.getEmail());
        account.setSchool(user.getSchool());
        return account;
    }

}
