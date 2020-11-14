package cn.edu.zjnu.acm.judge.controller.submission;

import cn.edu.zjnu.acm.judge.domain.Submission;
import cn.edu.zjnu.acm.judge.exception.BusinessCode;
import cn.edu.zjnu.acm.judge.exception.BusinessException;
import cn.edu.zjnu.acm.judge.mapper.SubmissionDetailMapper;
import cn.edu.zjnu.acm.judge.mapper.UserPreferenceMapper;
import cn.edu.zjnu.acm.judge.service.ContestOnlyService;
import cn.edu.zjnu.acm.judge.service.LanguageService;
import cn.edu.zjnu.acm.judge.service.SubmissionService;
import cn.edu.zjnu.acm.judge.util.ResultType;
import cn.edu.zjnu.acm.judge.util.SecurityUtils;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

/**
 * @author zhanhb
 */
@Controller
@RequestMapping(produces = TEXT_HTML_VALUE)
@RequiredArgsConstructor
public class ShowSourceController {

    private final SubmissionDetailMapper submissionDetailMapper;
    private final UserPreferenceMapper userPreferenceMapper;
    private final SubmissionService submissionService;
    private final LanguageService languageService;
    private final ContestOnlyService contestOnlyService;

    @Secured("ROLE_USER")
    @GetMapping("showsource")
    @SuppressWarnings("AssignmentToMethodParameter")
    public String showSource(HttpServletRequest request,
            @RequestParam("solution_id") long submissionId,
            @RequestParam(value = "style", required = false) Integer style) {
        String userId = SecurityUtils.getUserId();
        Submission submission = submissionService.findById(submissionId);
        contestOnlyService.checkViewSource(request, submission);
        if (!submissionService.canView(request, submission)) {
            throw new BusinessException(BusinessCode.VIEW_SOURCE_PERMISSION_DENIED, submissionId);
        }
        String language = languageService.getLanguageName(submission.getLanguage());

        if (style == null) {
            style = userPreferenceMapper.getStyle(userId);
        } else {
            userPreferenceMapper.setStyle(userId, style);
        }
        String source = submissionDetailMapper.findSourceById(submissionId);

        request.setAttribute("submission", submission);
        if (submission.getContest() != null) {
            request.setAttribute("contestId", submission.getContest());
        }
        request.setAttribute("language", language);
        request.setAttribute("result", ResultType.getShowSourceString(submission.getScore()));
        request.setAttribute("style", style);
        request.setAttribute("source", source);
        return "submissions/source";
    }

}
