package cn.edu.zjnu.acm.judge.bbs;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PostPageController {

    @Secured("ROLE_USER")
    @RequestMapping(value = {"/postpage", "/post"}, method = {RequestMethod.GET, RequestMethod.HEAD})
    public String postpage(Model model,
            @RequestParam(value = "problem_id", required = false) Long problemId) {
        model.addAttribute("problemId", problemId);
        return "bbs/postpage";
    }

}
