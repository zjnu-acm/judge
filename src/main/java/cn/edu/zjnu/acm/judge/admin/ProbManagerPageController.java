package cn.edu.zjnu.acm.judge.admin;

import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.ContestMapper;
import cn.edu.zjnu.acm.judge.mapper.ProblemMapper;
import cn.edu.zjnu.acm.judge.util.JudgeUtils;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@Secured("ROLE_ADMIN")
public class ProbManagerPageController {

    @Autowired
    private ContestMapper contestMapper;
    @Autowired
    private ProblemMapper problemMapper;

    @GetMapping("/admin/problems/new")
    public String newProblem(Model model) {
        model.addAttribute("title", "New Problem");
        model.addAttribute("hint", "Add New problem");
        model.addAttribute("url", "/admin/problems.html");
        model.addAttribute("method", "POST");
        model.addAttribute("hint2", "Add a Problem");
        Problem problem = Problem
                .builder()
                .memoryLimit(65536)
                .timeLimit(3000)
                .build();
        return finalBlock(problem, model);
    }

    // TODO this page requires to be updated, for the page will modify the content in default language
    @GetMapping("/admin/problems/{problemId}/edit")
    public String probmanagerpage(Model model,
            @PathVariable("problemId") long problemId,
            @RequestParam(value = "problemLang", defaultValue = "") String lang) {
        log.debug(lang);
        Problem problem = problemMapper.findOne(problemId, lang);
        if (problem == null) {
            throw new MessageException("No such problem, ID:" + problemId, HttpStatus.NOT_FOUND);
        }
        problem = problem.toBuilder()
                .description(JudgeUtils.getHtmlFormattedString(problem.getDescription()))
                .input(JudgeUtils.getHtmlFormattedString(problem.getInput()))
                .output(JudgeUtils.getHtmlFormattedString(problem.getOutput()))
                .hint(JudgeUtils.getHtmlFormattedString(problem.getHint()))
                .source(JudgeUtils.getHtmlFormattedString(problem.getSource()))
                .build();

        model.addAttribute("title", "Modify " + problemId);
        model.addAttribute("hint", "Modify problem " + problemId);
        model.addAttribute("url", "/admin/problems/" + problemId + ".html");
        model.addAttribute("method", "PUT");
        model.addAttribute("hint2", "Modify problem");
        model.addAttribute("problemLang", lang);
        return finalBlock(problem, model);
    }

    private String finalBlock(Problem problem, Model model) {
        List<Contest> contests = problem.getContest() == null
                ? contestMapper.pending()
                : contestMapper.runningAndScheduling();
        model.addAttribute("problem", problem);
        model.addAttribute("contests", contests);
        return "admin/problems/edit";
    }

}
