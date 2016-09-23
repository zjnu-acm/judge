package cn.edu.zjnu.acm.judge.submission;

import cn.edu.zjnu.acm.judge.mapper.UserPreferenceMapper;
import cn.edu.zjnu.acm.judge.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SubmitPageController {

    @Autowired
    private UserPreferenceMapper userPerferenceMapper;
    @Autowired
    private LanguageService languageService;

    @Secured("ROLE_USER")
    @GetMapping({"/submitpage", "/submit"})
    public String submitpage(Model model,
            @RequestParam(value = "problem_id", required = false) Long problemId,
            @RequestParam(value = "contest_id", required = false) Long contestId,
            Authentication authentication) {
        model.addAttribute("contestId", contestId);
        model.addAttribute("problemId", problemId);
        model.addAttribute("languages", languageService.getLanguages().values());
        String user = authentication != null ? authentication.getName() : null;
        int languageId = userPerferenceMapper.getLanguage(user);
        model.addAttribute("languageId", languageId);
        return "submit";
    }

}
