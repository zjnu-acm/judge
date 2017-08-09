package cn.edu.zjnu.acm.judge.contest;

import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.ContestMapper;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

@Controller
public class ContestProblemListController {

    @Autowired
    private ContestMapper contestMapper;

    @GetMapping(value = "/showcontest", produces = TEXT_HTML_VALUE)
    public String showContest(Model model, @RequestParam("contest_id") long contestId,
            Locale locale, Authentication authentication) {
        Contest contest = contestMapper.findOneByIdAndDisabledFalse(contestId);
        if (contest == null) {
            throw new MessageException("onlinejudge.contest.nosuchcontest", HttpStatus.NOT_FOUND);
        }
        model.addAttribute("contestId", contestId);
        model.addAttribute("contest", contest);
        if (contest.isStarted()) {
            List<Problem> problems = contestMapper.getProblems(contestId, authentication != null ? authentication.getName() : null, locale.getLanguage());
            model.addAttribute("problems", problems);
        }

        return "contests/problems";
    }

}
