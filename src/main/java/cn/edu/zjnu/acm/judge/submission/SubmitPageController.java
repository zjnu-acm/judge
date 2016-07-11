package cn.edu.zjnu.acm.judge.submission;

import cn.edu.zjnu.acm.judge.mapper.UserPerferenceMapper;
import cn.edu.zjnu.acm.judge.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SubmitPageController {

    @Autowired
    private UserPerferenceMapper userPerferenceMapper;
    @Autowired
    private LanguageService languageService;

    @Secured("ROLE_USER")
    @RequestMapping(value = {"/submitpage", "/submit"}, method = {RequestMethod.GET, RequestMethod.HEAD})
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
