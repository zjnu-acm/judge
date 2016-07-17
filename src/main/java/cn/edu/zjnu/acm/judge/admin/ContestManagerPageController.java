package cn.edu.zjnu.acm.judge.admin;

import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.ContestMapper;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@Secured("ROLE_ADMIN")
public class ContestManagerPageController {

    @Autowired
    private ContestMapper contestMapper;

    @GetMapping("/admin/contests/{contestId}")
    public String view(Model model,
            @PathVariable("contestId") long contestId,
            Locale locale) {
        Contest contest = Optional.ofNullable(contestMapper.findOne(contestId))
                .orElseThrow(() -> new MessageException("onlinejudge.contest.nosuchcontest", HttpStatus.NOT_FOUND));
        List<Problem> problems = contestMapper.getProblems(contestId, null, locale.getLanguage());
        model.addAttribute("contest", contest);
        model.addAttribute("problems", problems);
        return "admin/contests/view";
    }

}
