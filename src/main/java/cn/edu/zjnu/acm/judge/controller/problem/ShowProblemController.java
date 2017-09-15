package cn.edu.zjnu.acm.judge.controller.problem;

import cn.edu.zjnu.acm.judge.config.JudgeConfiguration;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.exception.BusinessCode;
import cn.edu.zjnu.acm.judge.exception.BusinessException;
import cn.edu.zjnu.acm.judge.mapper.ProblemMapper;
import cn.edu.zjnu.acm.judge.service.LocaleService;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

@Controller
public class ShowProblemController {

    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private JudgeConfiguration judgeConfiguration;
    @Autowired
    private LocaleService localeService;

    private Problem getProblem(long problemId, Locale locale) {
        Problem problem = problemMapper.findOne(problemId, localeService.resolve(locale));
        if (problem != null) {
            Boolean disabled = problem.getDisabled();
            if (disabled == null || !disabled) {
                return problem;
            }
        }
        throw new BusinessException(BusinessCode.PROBLEM_NOT_FOUND, problemId);
    }

    @GetMapping(value = "/showproblem", produces = TEXT_HTML_VALUE)
    public String showProblem(Model model, @RequestParam("problem_id") long problemId, Locale locale) {
        Problem problem = getProblem(problemId, locale);
        Path dataPath = judgeConfiguration.getDataDirectory(problemId);
        model.addAttribute("problem", problem);
        model.addAttribute("isSpecial", Files.exists(dataPath.resolve(JudgeConfiguration.VALIDATE_FILE_NAME)));
        String title1 = problemId + " -- " + problem.getTitle();
        String title2 = problem.getTitle();
        model.addAttribute("title1", title1);
        model.addAttribute("title2", title2);
        return "problems/view";
    }

}
