package cn.edu.zjnu.acm.judge.admin;

import cn.edu.zjnu.acm.judge.mapper.ContestMapper;
import cn.edu.zjnu.acm.judge.service.UserDetailService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AdminContestListController {

    @Autowired
    private ContestMapper contestMapper;

    @RequestMapping(value = "/admin/contests", method = {RequestMethod.GET, RequestMethod.HEAD})
    public String contests(HttpServletRequest request) {
        UserDetailService.requireAdminLoginned(request);
        request.setAttribute("list", contestMapper.findAll());
        return "admin/contests/list";
    }

    @RequestMapping(value = "/contests/{contestId}/disable", method = {RequestMethod.GET, RequestMethod.HEAD})
    public String disable(HttpServletRequest request,
            @PathVariable("contestId") long contestId,
            @RequestHeader("Referer") String referrer) {
        UserDetailService.requireAdminLoginned(request);
        contestMapper.disable(contestId);
        return "redirect:" + referrer;
    }

    @RequestMapping(value = "/contests/{contestId}/enable", method = {RequestMethod.GET, RequestMethod.HEAD})
    public String enable(HttpServletRequest request,
            @PathVariable("contestId") long contestId,
            @RequestHeader("Referer") String referrer) {
        UserDetailService.requireAdminLoginned(request);
        contestMapper.enable(contestId);
        return "redirect:" + referrer;
    }

}
