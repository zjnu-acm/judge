package cn.edu.zjnu.acm.judge.admin;

import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.ContestMapper;
import cn.edu.zjnu.acm.judge.mapper.ProblemMapper;
import cn.edu.zjnu.acm.judge.service.UserDetailService;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ContestManagerController {

    @Autowired
    private ContestMapper contestMapper;
    @Autowired
    private ProblemMapper problemMapper;

    private final Object contestLock = new Object();

    private Contest getContest(long contestId) {
        return Optional.ofNullable(contestMapper.findOne(contestId))
                .orElseThrow(() -> new MessageException("onlinejudge.contest.nosuchcontest", HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/admin/contests/{contestId}/problems", method = RequestMethod.POST)
    public String addProblem(HttpServletRequest request,
            @PathVariable("contestId") long contestId,
            @RequestParam("problemId") long problemId,
            RedirectAttributes redirectAttributes) {
        UserDetailService.requireAdminLoginned(request);
        Contest contest = getContest(contestId);
        if (contest.isEnded()) {
            throw new MessageException("Contest is ended, can't add problem now", HttpStatus.BAD_REQUEST);
        }
        synchronized (contestLock) {
            problemMapper.setContest(problemId, contestId);
            contestMapper.addProblem(contestId, problemId, null, 9999999);
            contestMapper.updateContestOrder(contestId);
            redirectAttributes.addAttribute("contestId", contestId);
        }
        return "redirect:/admin/contests/{contestId}";
    }

    @RequestMapping(value = "/admin/contests/{contestId}/problems/{problemId}", method = RequestMethod.DELETE)
    public String deleteProblem(HttpServletRequest request,
            @PathVariable("contestId") long contestId,
            @PathVariable("problemId") long problemId,
            RedirectAttributes redirectAttributes) {
        UserDetailService.requireAdminLoginned(request);
        Contest contest = getContest(contestId);
        if (contest.isEnded()) {
            throw new MessageException("Contest is ended, can't delete problem now", HttpStatus.BAD_REQUEST);
        }
        if (contest.isStarted()) {
            throw new MessageException("Contest is started, can't delete problem now", HttpStatus.BAD_REQUEST);
        }
        synchronized (contestLock) {
            problemMapper.setContest(problemId, null);
            contestMapper.deleteContestProblem(contestId, problemId);
            contestMapper.updateContestOrder(contestId);
        }
        redirectAttributes.addAttribute("contestId", contestId);
        return "redirect:/admin/contests/{contestId}";
    }

}
