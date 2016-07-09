package cn.edu.zjnu.acm.judge.contest;

import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.ContestMapper;
import cn.edu.zjnu.acm.judge.service.UserDetailService;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ContestProblemListController {

    @Autowired
    private ContestMapper contestMapper;

    @RequestMapping(value = "/showcontest", method = {RequestMethod.GET, RequestMethod.HEAD})
    public String showcontest(HttpServletRequest request, @RequestParam("contest_id") long contestId, Locale locale) {
        Contest contest = contestMapper.findOneByIdAndDisabledFalse(contestId);
        if (contest == null) {
            throw new MessageException("onlinejudge.contest.nosuchcontest", HttpStatus.NOT_FOUND);
        }
        request.setAttribute("contestId", contestId);
        request.setAttribute("contest", contest);
        if (contest.isStarted()) {
            List<Problem> problems = contestMapper.getProblems(contestId, UserDetailService.getCurrentUserId(request).orElse(null), locale.getLanguage());
            request.setAttribute("problems", problems);
        }

        return "contests/problems";
    }

}
