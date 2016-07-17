package cn.edu.zjnu.acm.judge.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

@Controller
public class LoginController {

    @GetMapping({"/loginpage", "/login"})
    protected String login(Model model,
            @RequestParam(value = "url", required = false) String back,
            @RequestHeader(value = "Referer", required = false) String referrer,
            @RequestParam(value = "contest_id", required = false) String contestId) {
        model.addAttribute("backURL", !StringUtils.isEmptyOrWhitespace(back) ? back : referrer);
        model.addAttribute("contestId", contestId);
        return "login";
    }

}
