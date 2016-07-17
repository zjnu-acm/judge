package cn.edu.zjnu.acm.judge.bbs;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PostPageController {

    @Secured("ROLE_USER")
    @GetMapping({"/postpage", "/post"})
    public String postpage(Model model,
            @RequestParam(value = "problem_id", required = false) Long problemId) {
        model.addAttribute("problemId", problemId);
        return "bbs/postpage";
    }

}
