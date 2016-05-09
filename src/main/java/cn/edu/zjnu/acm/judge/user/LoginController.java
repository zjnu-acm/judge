package cn.edu.zjnu.acm.judge.user;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @RequestMapping(value = {"/loginpage", "/login"}, method = {RequestMethod.GET, RequestMethod.HEAD})
    protected String login(HttpServletRequest request,
            @RequestParam(value = "url", required = false) String back,
            @RequestHeader(value = "Referer", required = false) String referrer,
            @RequestParam(value = "contest_id", required = false) String contestId) {
        request.setAttribute("backURL", StringUtils.isBlank(back) ? referrer : back);
        request.setAttribute("contestId", contestId);
        return "login";
    }

}
