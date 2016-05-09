package cn.edu.zjnu.acm.judge.bbs;

import cn.edu.zjnu.acm.judge.service.UserDetailService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PostPageController {

    @RequestMapping(value = "/postpage", method = {RequestMethod.GET, RequestMethod.HEAD})
    public String postpage(HttpServletRequest request,
            @RequestParam(value = "problem_id", required = false) Long problemId) {
        UserDetailService.requireLoginned(request);
        request.setAttribute("problemId", problemId);
        return "bbs/postpage";
    }

}
