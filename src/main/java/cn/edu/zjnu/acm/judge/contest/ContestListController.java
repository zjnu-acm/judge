package cn.edu.zjnu.acm.judge.contest;

import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.ContestMapper;
import java.util.List;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ContestListController {

    @Autowired
    private ContestMapper contestMapper;

    @GetMapping("/contests")
    public String contests(Model model, RedirectAttributes redirectAttributes) {
        return execute(model, contestMapper::contests, "Contests", "onlinejudge.contests.nocontest", redirectAttributes);
    }

    @GetMapping("/scheduledcontests")
    public String scheduledContests(Model model, RedirectAttributes redirectAttributes) {
        return execute(model, contestMapper::pending, "Scheduled Contests", "onlinejudge.contests.noschedule", redirectAttributes);
    }

    @GetMapping("/pastcontests")
    public String pastContests(Model model, RedirectAttributes redirectAttributes) {
        return execute(model, contestMapper::past, "Contests", "onlinejudge.contests.nopast", redirectAttributes);
    }

    @GetMapping("/currentcontests")
    protected String currentContests(Model model, RedirectAttributes redirectAttributes) {
        return execute(model, contestMapper::current, "Current Contests", "onlinejudge.contest.nocurrent", redirectAttributes);
    }

    private String execute(Model model, Supplier<List<Contest>> supplier,
            String title, String errorMessage, RedirectAttributes redirectAttributes) {
        List<Contest> contests = supplier.get();
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
