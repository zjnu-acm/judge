package cn.edu.zjnu.acm.judge.controller.user;

import javax.annotation.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

/**
 * @author zhanhb
 */
@Controller
@RequestMapping(produces = TEXT_HTML_VALUE)
public class LoginController {

    @GetMapping({"loginpage", "login"})
    public String login(Model model,
            @RequestParam(value = "url", required = false) @Nullable String back,
            @RequestHeader(value = "Referer", required = false) @Nullable String referrer,
            @RequestParam(value = "contest_id", required = false) @Nullable String contestId) {
        model.addAttribute("backURL", StringUtils.hasText(back) ? back : referrer);
        model.addAttribute("contestId", contestId);
        return "login";
    }

}
