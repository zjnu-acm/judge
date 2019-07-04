package cn.edu.zjnu.acm.judge.controller.contest;

import cn.edu.zjnu.acm.judge.data.form.ContestStatus;
import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.exception.BusinessCode;
import cn.edu.zjnu.acm.judge.exception.BusinessException;
import cn.edu.zjnu.acm.judge.service.ContestService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

/**
 * @author zhanhb
 */
@Controller
@RequestMapping(produces = TEXT_HTML_VALUE)
@RequiredArgsConstructor
public class ContestListController {

    private final ContestService contestService;

    @GetMapping("contests")
    public String contests(Model model, RedirectAttributes redirectAttributes) {
        return execute(model, "Contests", BusinessCode.NO_CONTESTS, redirectAttributes, ContestStatus.PENDING, ContestStatus.RUNNING, ContestStatus.ENDED);
    }

    @GetMapping("scheduledcontests")
    public String scheduledContests(Model model, RedirectAttributes redirectAttributes) {
        return execute(model, "Scheduled Contests", BusinessCode.NO_SCHEDULED_CONTESTS, redirectAttributes, ContestStatus.PENDING);
    }

    @GetMapping("pastcontests")
    public String pastContests(Model model, RedirectAttributes redirectAttributes) {
        return execute(model, "Past Contests", BusinessCode.NO_PAST_CONTESTS, redirectAttributes, ContestStatus.ENDED);
    }

    @GetMapping("currentcontests")
    public String currentContests(Model model, RedirectAttributes redirectAttributes) {
        return execute(model, "Current Contests", BusinessCode.NO_CURRENT_CONTESTS, redirectAttributes, ContestStatus.RUNNING);
    }

    private String execute(Model model, String title, BusinessCode businessCode, RedirectAttributes redirectAttributes,
            ContestStatus status, ContestStatus... rest) {
        List<Contest> contests = contestService.findAll(status, rest);
        if (contests.isEmpty()) {
            throw new BusinessException(businessCode);
        } else if (contests.size() == 1) {
            redirectAttributes.addAttribute("contest_id", contests.get(0).getId());
            return "redirect:/showcontest";
        } else {
            model.addAttribute("title", title);
            model.addAttribute("contests", contests);
            return "contests/index";
        }
    }

}
