package cn.edu.zjnu.acm.judge.controller.contest;

import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.service.ContestService;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

@Controller
public class ContestProblemListController {

    @Autowired
    private ContestService contestService;

    @GetMapping(value = "/showcontest", produces = TEXT_HTML_VALUE)
    public String showContest(Model model, @RequestParam("contest_id") long contestId,
            Locale locale, Authentication authentication) {
        Contest contest = contestService.getContestAndProblemsNotDisabled(contestId, authentication != null ? authentication.getName() : null, locale);
        model.addAttribute("contestId", contestId);
        model.addAttribute("contest", contest);
        if (contest.isStarted()) {
            model.addAttribute("problems", contest.getProblems());
        } else {
            contest.setProblems(null);
        }

        return "contests/problems";
    }

}
