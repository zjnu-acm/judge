package cn.edu.zjnu.acm.judge.controller.submission;

import cn.edu.zjnu.acm.judge.mapper.UserPreferenceMapper;
import cn.edu.zjnu.acm.judge.service.ContestOnlyService;
import cn.edu.zjnu.acm.judge.service.LanguageService;
import cn.edu.zjnu.acm.judge.service.SubmissionService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Secured("ROLE_USER")
public class SubmitController {

    @Autowired
    private ContestOnlyService contestOnlyService;
    @Autowired
    private SubmissionService submissionService;
    @Autowired
    private UserPreferenceMapper userPerferenceMapper;
    @Autowired
    private LanguageService languageService;

    @GetMapping({"/submitpage", "/submit"})
    public String submitPage(Model model,
            @RequestParam(value = "problem_id", required = false) Long problemId,
            Authentication authentication) {
        return page(null, problemId, authentication, model);
    }

    @GetMapping({
        "/problems/{problemId}/submit",
        "/contests/{contestId}/problems/{problemId}/submit"
    })
    public String contestSubmitPage(Model model,
            @PathVariable(value = "contestId", required = false) Long contestId,
            @PathVariable("problemId") long problemId,
            Authentication authentication) {
        return page(contestId, problemId, authentication, model);
    }

    private String page(Long contestId, Long problemId, Authentication authentication, Model model) {
        model.addAttribute("contestId", contestId);
        model.addAttribute("problemId", problemId);
        model.addAttribute("languages", languageService.getAvailableLanguages().values());
        String user = authentication != null ? authentication.getName() : null;
        int languageId = userPerferenceMapper.getLanguage(user);
        model.addAttribute("languageId", languageId);
        return "submit";
    }

    @PostMapping({
        "/problems/{problemId}/submit",
        "/contests/{contestId}/problems/{problemId}/submit"
    })
    public synchronized String submit(HttpServletRequest request,
            @PathVariable(value = "contestId", required = false) Long contestId,
            @PathVariable("problemId") long problemId,
            @RequestParam("language") int languageId,
            @RequestParam("source") String source,
            RedirectAttributes redirectAttributes,
            Authentication authentication) {
        contestOnlyService.checkSubmit(request, contestId, problemId);
        String userId = authentication.getName();
        String ip = request.getRemoteAddr();
        //提交是否在竞赛中
        redirectAttributes.addAttribute("user_id", userId);
        if (contestId == null) {
            submissionService.submit(languageId, source, userId, ip, problemId);
        } else {
            redirectAttributes.addAttribute("contest_id", contestId);
            submissionService.contestSubmit(languageId, source, userId, ip, contestId, problemId);
        }
        return "redirect:/status";
    }

}
