package cn.edu.zjnu.acm.judge.controller.submission;

import cn.edu.zjnu.acm.judge.mapper.UserPreferenceMapper;
import cn.edu.zjnu.acm.judge.service.ContestOnlyService;
import cn.edu.zjnu.acm.judge.service.LanguageService;
import cn.edu.zjnu.acm.judge.service.SubmissionService;
import cn.edu.zjnu.acm.judge.util.SecurityUtils;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

/**
 * @author zhanhb
 */
@Controller
@RequestMapping(produces = TEXT_HTML_VALUE)
@RequiredArgsConstructor
@Secured("ROLE_USER")
public class SubmitController {

    private final ContestOnlyService contestOnlyService;
    private final SubmissionService submissionService;
    private final UserPreferenceMapper userPreferenceMapper;
    private final LanguageService languageService;

    @GetMapping({"submitpage", "submit"})
    public String submitPage(Model model,
            @RequestParam(value = "problem_id", required = false) Long problemId) {
        String userId = SecurityUtils.getUserId();
        return page(null, problemId, userId, model);
    }

    @GetMapping({
        "problems/{problemId}/submit",
        "contests/{contestId}/problems/{problemId}/submit"
    })
    public String contestSubmitPage(Model model,
            @PathVariable(value = "contestId", required = false) Long contestId,
            @PathVariable("problemId") long problemId) {
        String userId = SecurityUtils.getUserId();
        return page(contestId, problemId, userId, model);
    }

    private String page(Long contestId, Long problemId, String userId, Model model) {
        model.addAttribute("contestId", contestId);
        model.addAttribute("problemId", problemId);
        model.addAttribute("languages", languageService.getAvailableLanguages().values());
        int languageId = userPreferenceMapper.getLanguage(userId);
        model.addAttribute("languageId", languageId);
        return "submissions/index";
    }

    @PostMapping({
        "problems/{problemId}/submit",
        "contests/{contestId}/problems/{problemId}/submit"
    })
    public synchronized String submit(HttpServletRequest request,
            @PathVariable(value = "contestId", required = false) Long contestId,
            @PathVariable("problemId") long problemId,
            @RequestParam("language") int languageId,
            @RequestParam("source") String source,
            RedirectAttributes redirectAttributes) {
        contestOnlyService.checkSubmit(request, contestId, problemId);
        String userId = SecurityUtils.getUserId();
        String ip = request.getRemoteAddr();
        redirectAttributes.addAttribute("user_id", userId);
        // If the submit is not in a contest
        if (contestId == null) {
            submissionService.submit(languageId, source, userId, ip, problemId, true);
        } else {
            // TODO if contest is ended, should redirect to stats rather than contest's status
            redirectAttributes.addAttribute("contest_id", contestId);
            submissionService.contestSubmit(languageId, source, userId, ip, contestId, problemId);
        }
        return "redirect:/status";
    }

}
