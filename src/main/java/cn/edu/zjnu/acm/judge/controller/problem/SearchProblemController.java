package cn.edu.zjnu.acm.judge.controller.problem;

import cn.edu.zjnu.acm.judge.data.form.ProblemForm;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.service.ProblemService;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

@Controller
public class SearchProblemController {

    @Autowired
    private ProblemService problemService;

    @GetMapping("/searchproblem")
    public String searchProblem(Model model,
            @RequestParam(value = "sstr", required = false) String query,
            Locale locale, @PageableDefault(100) Pageable pageable,
            Authentication authentication) {
        if (StringUtils.isEmpty(query)) {
            throw new MessageException("Please input the keyword to the problem.", HttpStatus.BAD_REQUEST);
        }
        String userId = authentication != null ? authentication.getName() : null;
        ProblemForm problemForm = new ProblemForm();
        problemForm.setDisabled(Boolean.FALSE);
        problemForm.setQuery(query);
        Page<Problem> problems = problemService.findAll(problemForm, userId, pageable, locale);

        model.addAttribute("query", query);
        model.addAttribute("problems", problems);
        return "problems/search";
    }

}
