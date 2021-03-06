package cn.edu.zjnu.acm.judge.controller.submission;

import cn.edu.zjnu.acm.judge.data.dto.SubmissionDetailDTO;
import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.domain.Submission;
import cn.edu.zjnu.acm.judge.exception.BusinessCode;
import cn.edu.zjnu.acm.judge.exception.BusinessException;
import cn.edu.zjnu.acm.judge.mapper.ContestMapper;
import cn.edu.zjnu.acm.judge.service.SubmissionService;
import cn.edu.zjnu.acm.judge.util.ResultType;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
public class ShowSubmissionDetailsController {

    private final ContestMapper contestMapper;
    private final SubmissionService submissionService;

    @GetMapping("showsolutiondetails")
    public String showSolutionDetails(
            HttpServletRequest request,
            @RequestParam("solution_id") long submissionId) {
        Submission submission = submissionService.findById(submissionId);
        Long contestId = submission.getContest();
        if (contestId != null) {
            Contest contest = contestMapper.findOne(contestId);
            if (contest != null) {
                request.setAttribute("contestId", contest.getId());
            }
        }
        if (!submissionService.canView(request, submission)) {
            throw new BusinessException(BusinessCode.VIEW_SOURCE_PERMISSION_DENIED, submissionId);
        }
        List<SubmissionDetailDTO> details = submissionService.getSubmissionDetail(submissionId);
        request.setAttribute("details", details);
        request.setAttribute("user", submission.getUser());
        request.setAttribute("problem", submission.getProblem());
        request.setAttribute("result", ResultType.getResultDescription(submission.getScore()));
        request.setAttribute("score", submission.getScore());
        request.setAttribute("time", submission.getTime());
        request.setAttribute("memory", submission.getMemory());
        return "submissions/detail";
    }

}
