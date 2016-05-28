package cn.edu.zjnu.acm.judge.contest;

import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.ContestMapper;
import java.util.List;
import java.util.function.Supplier;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ContestListController {

    @Autowired
    private ContestMapper contestMapper;

    @RequestMapping(value = "/contests", method = {RequestMethod.GET, RequestMethod.HEAD})
    protected String contests(Model model, RedirectAttributes redirectAttributes) {
        return execute(model, contestMapper::contests, "Contests", "没有考试", redirectAttributes);
    }

    @RequestMapping(value = "/scheduledcontests", method = {RequestMethod.GET, RequestMethod.HEAD})
    protected String scheduledcontests(Model model, RedirectAttributes redirectAttributes) {
        return execute(model, contestMapper::pending, "Scheduled Contests", "没有计划中的考试", redirectAttributes);
    }

    @RequestMapping(value = "/pastcontests", method = {RequestMethod.GET, RequestMethod.HEAD})
    protected String pastcontests(Model model, RedirectAttributes redirectAttributes) {
        return execute(model, contestMapper::past, "Contests", "没有考过的考试", redirectAttributes);
    }

    @RequestMapping(value = "/currentcontests", method = {RequestMethod.GET, RequestMethod.HEAD})
    protected String currentcontests(Model model, RedirectAttributes redirectAttributes) {
        return execute(model, contestMapper::current, "Current Contests", "没有正在进行的比赛", redirectAttributes);
    }

    private String execute(Model model, Supplier<List<Contest>> supplier,
            String title, String errorMessage, RedirectAttributes redirectAttributes) {
        List<Contest> contests = supplier.get();
        if (contests.isEmpty()) {
            throw new MessageException(errorMessage, HttpServletResponse.SC_NOT_FOUND);
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
