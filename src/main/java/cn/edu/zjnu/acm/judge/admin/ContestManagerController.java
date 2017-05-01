package cn.edu.zjnu.acm.judge.admin;

import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.ContestMapper;
import cn.edu.zjnu.acm.judge.service.ContestService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Secured("ROLE_ADMIN")
public class ContestManagerController {

    @Autowired
    private ContestMapper contestMapper;
    @Autowired
    private ContestService contestService;

    private Contest getContest(long contestId) {
        return Optional.ofNullable(contestMapper.findOne(contestId))
                .orElseThrow(() -> new MessageException("onlinejudge.contest.nosuchcontest", HttpStatus.NOT_FOUND));
    }

    @PostMapping("/admin/contests/{contestId}/problems")
    public String addProblem(
            @PathVariable("contestId") long contestId,
            @RequestParam("problemId") long problemId,
            RedirectAttributes redirectAttributes) {
        Contest contest = getContest(contestId);
        if (contest.isEnded()) {
            throw new MessageException("Contest is ended, can't add problem now", HttpStatus.BAD_REQUEST);
        }
        contestService.addProblem(contestId, problemId);
        redirectAttributes.addAttribute("contestId", contestId);
        return "redirect:/admin/contests/{contestId}.html";
    }

    @DeleteMapping("/admin/contests/{contestId}/problems/{problemId}")
    public String deleteProblem(
            @PathVariable("contestId") long contestId,
            @PathVariable("problemId") long problemId,
            RedirectAttributes redirectAttributes) {
        Contest contest = getContest(contestId);
        if (contest.isEnded()) {
            throw new MessageException("Contest is ended, can't delete problem now", HttpStatus.BAD_REQUEST);
        }
        if (contest.isStarted()) {
            throw new MessageException("Contest is started, can't delete problem now", HttpStatus.BAD_REQUEST);
        }
        contestService.removeProblem(contestId, problemId);
        redirectAttributes.addAttribute("contestId", contestId);
        return "redirect:/admin/contests/{contestId}.html";
    }

}
