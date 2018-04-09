package cn.edu.zjnu.acm.judge.controller.user;

import cn.edu.zjnu.acm.judge.data.form.AccountForm;
import cn.edu.zjnu.acm.judge.domain.User;
import cn.edu.zjnu.acm.judge.exception.BusinessCode;
import cn.edu.zjnu.acm.judge.exception.BusinessException;
import cn.edu.zjnu.acm.judge.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchUserController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/searchuser")
    public String searchuser(Model model,
            @RequestParam(value = "user_id", defaultValue = "") String keyword,
            @RequestParam(value = "position", required = false) String position,
            @PageableDefault(1000) Pageable pageable) {
        if (keyword.replace("%", "").length() < 2) {
            throw new BusinessException(BusinessCode.USER_SEARCH_KEYWORD_SHORT);
        }
        String like = keyword;
        if (position == null) {
            like = "%" + like + "%";
        } else if (position.equalsIgnoreCase("begin")) {
            like += "%";
        } else {
            like = "%" + like;
        }
        Pageable t = pageable;
        if (t.getSort() == null) {
            t = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(
                    Sort.Order.desc("solved"),
                    Sort.Order.asc("submit")
            ));
        }
        Page<User> users = accountService.findAll(AccountForm.builder().disabled(Boolean.FALSE).query(like).build(), t);
        model.addAttribute("query", keyword);
        model.addAttribute("users", users);
        return "users/search";
    }

}
