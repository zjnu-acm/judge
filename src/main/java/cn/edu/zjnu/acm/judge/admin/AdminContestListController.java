package cn.edu.zjnu.acm.judge.admin;

import cn.edu.zjnu.acm.judge.mapper.ContestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
@Secured("ROLE_ADMIN")
public class AdminContestListController {

    @Autowired
    private ContestMapper contestMapper;

    @GetMapping("/admin/contests")
    public String contests(Model model) {
        model.addAttribute("list", contestMapper.findAll());
        return "admin/contests/list";
    }

    @GetMapping("/contests/{contestId}/disable")
    public String disable(
            @PathVariable("contestId") long contestId,
            @RequestHeader("Referer") String referrer) {
        contestMapper.disable(contestId);
        return "redirect:" + referrer;
    }

    @GetMapping("/contests/{contestId}/enable")
    public String enable(
            @PathVariable("contestId") long contestId,
            @RequestHeader("Referer") String referrer) {
        contestMapper.enable(contestId);
        return "redirect:" + referrer;
    }

}
