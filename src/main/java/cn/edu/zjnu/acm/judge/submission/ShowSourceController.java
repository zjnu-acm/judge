package cn.edu.zjnu.acm.judge.submission;

import cn.edu.zjnu.acm.judge.domain.Submission;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.SubmissionMapper;
import cn.edu.zjnu.acm.judge.mapper.UserPreferenceMapper;
import cn.edu.zjnu.acm.judge.service.LanguageService;
import cn.edu.zjnu.acm.judge.service.SubmissionService;
import cn.edu.zjnu.acm.judge.util.ResultType;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ShowSourceController {

    @Autowired
    private SubmissionMapper submissionMapper;
    @Autowired
    private UserPreferenceMapper userPerferenceMapper;
    @Autowired
    private SubmissionService submissionService;
    @Autowired
    private LanguageService languageService;

    @Secured("ROLE_USER")
    @GetMapping("/showsource")
    @SuppressWarnings("AssignmentToMethodParameter")
    public String showsource(HttpServletRequest request,
            @RequestParam("solution_id") long submissionId,
            @RequestParam(value = "style", required = false) Integer style,
            Authentication authentication) {
        Submission submission = submissionMapper.findOne(submissionId);

        if (submission == null) {
            throw new MessageException("No such solution", HttpStatus.NOT_FOUND);
        }
        String userId = authentication != null ? authentication.getName() : null;
        if (!submissionService.canView(request, submission)) {
            throw new MessageException("You have no permission to view the source.", HttpStatus.FORBIDDEN);
        }
        String language = languageService.getAvailableLanguage(submission.getLanguage()).getName();

        if (style == null) {
            style = userPerferenceMapper.getStyle(userId);
        } else {
            userPerferenceMapper.setStyle(userId, style);
        }
        String source = submissionMapper.findSourceById(submissionId);

        request.setAttribute("submission", submission);
        if (submission.getContest() != null) {
            request.setAttribute("contestId", submission.getContest());
        }
        request.setAttribute("language", language);
        request.setAttribute("result", ResultType.getShowsourceString(submission.getScore()));
        request.setAttribute("style", style);
        request.setAttribute("source", source);
        return "submissions/source";
    }

}
