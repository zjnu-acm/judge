package cn.edu.zjnu.acm.judge.bbs;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PostPageController {

    @Secured("ROLE_USER")
    @RequestMapping(value = {"/postpage", "/post"}, method = {RequestMethod.GET, RequestMethod.HEAD})
    public String postpage(HttpServletRequest request,
            @RequestParam(value = "problem_id", required = false) Long problemId) {
        request.setAttribute("problemId", problemId);
        return "bbs/postpage";
    }

}
