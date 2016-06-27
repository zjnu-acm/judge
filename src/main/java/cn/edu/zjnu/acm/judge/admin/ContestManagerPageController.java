package cn.edu.zjnu.acm.judge.admin;

import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.ContestMapper;
import cn.edu.zjnu.acm.judge.service.UserDetailService;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ContestManagerPageController {

    @Autowired
    private ContestMapper contestMapper;

    @RequestMapping(value = "/admin/contests/{contestId}", method = {RequestMethod.GET, RequestMethod.HEAD})
    public String view(HttpServletRequest request,
            @PathVariable("contestId") long contestId,
            Locale locale) {
        UserDetailService.requireAdminLoginned(request);
        Contest contest = Optional.ofNullable(contestMapper.findOne(contestId))
                .orElseThrow(() -> new MessageException("onlinejudge.contest.nosuchcontest", HttpStatus.NOT_FOUND));
        List<Problem> problems = contestMapper.getProblems(contestId, null, locale.getLanguage());
        request.setAttribute("contest", contest);
        request.setAttribute("problems", problems);
        return "admin/contests/view";
    }

}
