package cn.edu.zjnu.acm.judge.controller.problem;

import cn.edu.zjnu.acm.judge.data.form.ProblemForm;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.service.ProblemService;
import cn.edu.zjnu.acm.judge.util.URLBuilder;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.util.StringUtils;

@Controller
public class SearchProblemController {

    @Autowired
    private ProblemService problemService;

    @GetMapping("/searchproblem")
    public String searchProblem(ProblemForm form, Model model, Locale locale, Authentication authentication,
            @PageableDefault(1000) Pageable pageable, HttpServletRequest request) {
        if (StringUtils.isEmpty(form.getSstr())) {
            throw new MessageException("Please input the keyword to the problem.", HttpStatus.BAD_REQUEST);
        }
        String currentUserId = authentication != null ? authentication.getName() : null;
        String url = URLBuilder.fromRequest(request).replaceQueryParam("page").toString();
        model.addAttribute("url", url);
        form.setDisabled(Boolean.FALSE);
        Page<Problem> page = problemService.findAll(form, currentUserId, pageable, locale);
        model.addAttribute("page", page);
        return "problems/search";
    }

}
