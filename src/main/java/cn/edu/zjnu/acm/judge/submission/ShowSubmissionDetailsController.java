package cn.edu.zjnu.acm.judge.submission;

import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.domain.Submission;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.ContestMapper;
import cn.edu.zjnu.acm.judge.mapper.SubmissionMapper;
import cn.edu.zjnu.acm.judge.service.SubmissionService;
import cn.edu.zjnu.acm.judge.util.ResultType;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ShowSubmissionDetailsController {

    @Autowired
    private SubmissionMapper submissionMapper;
    @Autowired
    private ContestMapper contestMapper;
    @Autowired
    private SubmissionService submissionService;

    @RequestMapping(value = "/showsolutiondetails", method = {RequestMethod.GET, RequestMethod.HEAD})
    public void showsolutiondetails(HttpServletRequest request, HttpServletResponse response,
            @RequestParam("solution_id") long submissionId)
            throws IOException {
        Submission submission = submissionMapper.findOne(submissionId);
        if (submission == null) {
            throw new MessageException("No such solution");
        }
        Long contestId = submission.getContest();
        if (contestId != null) {
            Contest contest = contestMapper.findOne(contestId);
            if (contest != null) {
                request.setAttribute("contestId", contest.getId());
            }
        }
        if (!submissionService.canView(request, submission)) {
            throw new MessageException("You have no permission to view solution '" + submissionId + "'");
        }
        String submissionDetail = submissionMapper.getSubmissionDetail(submissionId);
        if (submissionDetail == null) {
            throw new MessageException("No such solution");
        }
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print("<html><head><title>Solution Details</title></head><body>"
                + "<ul><li><font color=#333399 size=5>Solution Details</font></li></ul>"
                + "<TABLE align=center cellSpacing=0 cellPadding=0 width=75% border=1 class=table-back style=\"border-collapse: collapse\" bordercolor=#FFF>"
                + "<tr bgcolor=#6589D1><td align=center width=25%><b>Case num</b></td>"
                + "<td align=center width=25%><b>Result</b></td>"
                + "<td align=center width=25%><b>Score</b></td><td align=center width=7%><b>Time</b></td>"
                + "<td align=center width=25%><b>Memory</b></td>"
                + "</tr>");

        String[] detailsArray = submissionDetail.split(",");
        for (int i = 0; i < detailsArray.length / 4; ++i) {
            out.print("<tr align=center>"
                    + "<td>" + (i + 1) + "</td><td>"
                    + ResultType.getCaseScoreDescription(Integer.parseInt(detailsArray[i << 2])) + "</td><td>"
                    + detailsArray[i << 2 | 1] + "</td><td>" + detailsArray[i << 2 | 2] + "MS</td><td>"
                    + detailsArray[i << 2 | 3] + "K</td><td></tr>");
        }
        out.print("<tr bgcolor=#6589D1 align=center>"
                + "<td>Result</td><td>"
                + ResultType.getResultDescription(submission.getScore()) + "</td><td>"
                + submission.getScore() + "</td><td>"
                + submission.getTime() + "MS</td><td>"
                + submission.getMemory() + "K</td><td></tr>"
                + "</table></body></html>");
    }
}
