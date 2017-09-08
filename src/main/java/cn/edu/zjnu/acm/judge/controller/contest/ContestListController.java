package cn.edu.zjnu.acm.judge.controller.contest;

import cn.edu.zjnu.acm.judge.data.form.ContestStatus;
import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.service.ContestService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ContestListController {

    @Autowired
    private ContestService contestService;

    @GetMapping("/contests")
    public String contests(Model model, RedirectAttributes redirectAttributes) {
        return execute(model, "Contests", "onlinejudge.contests.nocontest", redirectAttributes, ContestStatus.PENDING, ContestStatus.RUNNING, ContestStatus.ENDED);
    }

    @GetMapping("/scheduledcontests")
    public String scheduledContests(Model model, RedirectAttributes redirectAttributes) {
        return execute(model, "Scheduled Contests", "onlinejudge.contests.noschedule", redirectAttributes, ContestStatus.PENDING);
    }

    @GetMapping("/pastcontests")
    public String pastContests(Model model, RedirectAttributes redirectAttributes) {
        return execute(model, "Contests", "onlinejudge.contests.nopast", redirectAttributes, ContestStatus.ENDED);
    }

    @GetMapping("/currentcontests")
    protected String currentContests(Model model, RedirectAttributes redirectAttributes) {
        return execute(model, "Current Contests", "onlinejudge.contest.nocurrent", redirectAttributes, ContestStatus.RUNNING);
    }

    private String execute(Model model, String title, String errorMessage, RedirectAttributes redirectAttributes,
            ContestStatus status, ContestStatus... rest) {
        List<Contest> contests = contestService.findAll(status, rest);
        if (contests.isEmpty()) {
            throw new MessageException(errorMessage, HttpStatus.OK);
        } else if (contests.size() == 1) {
            redirectAttributes.addAttribute("contest_id", contests.get(0).getId());
            return "redirect:/showcontest";
        } else {
            model.addAttribute("title", title);
            model.addAttribute("contests", contests);
            return "contests";
        }
    }

}
