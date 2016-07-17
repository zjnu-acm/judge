package cn.edu.zjnu.acm.judge.admin;

import cn.edu.zjnu.acm.judge.mapper.ContestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Secured("ROLE_ADMIN")
public class AdminContestListController {

    @Autowired
    private ContestMapper contestMapper;

    @RequestMapping(value = "/admin/contests", method = {RequestMethod.GET, RequestMethod.HEAD})
    public String contests(Model model) {
        model.addAttribute("list", contestMapper.findAll());
        return "admin/contests/list";
    }

    @RequestMapping(value = "/contests/{contestId}/disable", method = {RequestMethod.GET, RequestMethod.HEAD})
    public String disable(
            @PathVariable("contestId") long contestId,
            @RequestHeader("Referer") String referrer) {
        contestMapper.disable(contestId);
        return "redirect:" + referrer;
    }

    @RequestMapping(value = "/contests/{contestId}/enable", method = {RequestMethod.GET, RequestMethod.HEAD})
    public String enable(
            @PathVariable("contestId") long contestId,
            @RequestHeader("Referer") String referrer) {
        contestMapper.enable(contestId);
        return "redirect:" + referrer;
    }

}
