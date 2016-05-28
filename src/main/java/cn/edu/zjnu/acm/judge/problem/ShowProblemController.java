package cn.edu.zjnu.acm.judge.problem;

import cn.edu.zjnu.acm.judge.config.JudgeConfiguration;
import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.ContestMapper;
import cn.edu.zjnu.acm.judge.mapper.ProblemMapper;
import cn.edu.zjnu.acm.judge.util.JudgeUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ShowProblemController {

    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private ContestMapper contestMapper;
    @Autowired
    private JudgeConfiguration judgeConfiguration;

    @RequestMapping(value = "/showproblem", method = {RequestMethod.GET, RequestMethod.HEAD})
    public void showproblem(HttpServletRequest request, HttpServletResponse response,
            @RequestParam("problem_id") long problemId)
            throws IOException {
        Problem problem = problemMapper.findOne(problemId);
        if (problem == null) {
            throw new MessageException("Can not find problem (ID:" + problemId + ")", HttpServletResponse.SC_NOT_FOUND);
        }
        Long contestId = problem.getContest();
        long accepted = problem.getAccepted();
        long submit = problem.getSubmit();
        Path dataPath = judgeConfiguration.getDataDirectory(problemId);
        long contestNum = -1;
        boolean started = true;
        boolean ended = true;
        if (contestId != null) {
            Contest contest = contestMapper.findOne(contestId);
            if (contest != null) {
                started = contest.isStarted();
                ended = contest.isEnded();
            }
            if (ended) {
                problemMapper.setContest(problemId, null);
                contestId = null;
                contest = null;
            } else {
                contestNum = contestMapper.getProblemIdInContest(contestId, problemId);
            }
            if (problem.isDisabled() && contestId == null) {
                started = false;
            }
        }
        if (!started) {
            throw new MessageException("Can not find problem (ID:" + problemId + ")", HttpServletResponse.SC_NOT_FOUND);
        }
        String title = problem.getTitle();
        long timeLimit = problem.getTimeLimit();
        long memoryLimit = problem.getMemoryLimit();
        String description = problem.getDescription();
        String input = problem.getInput();
        String output = problem.getOutput();
        String sampleInput = problem.getSampleInput();
        String sampleOutput = problem.getSampleOutput();
        String hint = problem.getHint();
        String source = problem.getSource();

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        if (contestId == null) {
            out.print("<html><head><title>" + problemId + " -- " + StringEscapeUtils.escapeHtml4(title) + "</title></head><body>"
                    + "<table border=0 width=100% class=table-back><tr><td><div class=\"ptt\" lang=\"en-US\">" + title + "</div>");
        } else {
            request.setAttribute("contestId", contestId);
            out.print("<html><head><title>" + (char) (contestNum + 'A') + ":"
                    + problemId + " -- " + title + "</title></head><body>"
                    + "<table border=0 width=100% class=table-back><tr><td><table border=0 width=100%><tr>");
            List<Problem> problems = contestMapper.getProblems(contestId, null);
            for (Problem p : problems) {
                out.print("<td><a href=showproblem?problem_id="
                        + p.getOrign() + "><b>"
                        + (char) (p.getId() + 'A') + "</b></a></td>");
            }
            out.print("</tr></table>"
                    + "<div class=\"ptt\" lang=\"en-US\">Problem "
                    + (char) (contestNum + 'A') + ":" + title + "</div>");
        }
        out.print("<div class=\"plm\"><table align=\"center\"><tr><td><b>Time Limit:</b> " + timeLimit + "MS</td>"
                + "<td width=\"10px\"></td><td colspan=\"3\"><b>Memory Limit:</b> " + memoryLimit + "K</td></tr>"
                + "<tr><td><b>Total Submissions:</b> " + submit + "</td><td width=\"10px\"></td>"
                + "<td><b>Accepted:</b> " + accepted + "</td>");
        boolean isSpecial = Files.exists(dataPath.resolve(JudgeConfiguration.VALIDATE_FILE_NAME));

        if (isSpecial) {
            out.print("<td width=\"10px\"></td><td style=\"font-weight:bold; color:red;\">Special Judge</td>");
        }
        out.print("</table></div>");
        if (StringUtils.hasText(description)) {
            out.print("<p class=\"pst\">Description</p><div class=\"ptx\" lang=\"en-US\">");
            out.print(JudgeUtils.getHtmlFormattedString(description));
            out.print("</div>");
        }
        if (StringUtils.hasText(input)) {
            out.print("<p class=\"pst\">Input</p><div class=\"ptx\" lang=\"en-US\">");
            out.print(JudgeUtils.getHtmlFormattedString(input));
            out.print("</div>");
        }
        if (StringUtils.hasText(output)) {
            out.print("<p class=\"pst\">Output</p><div class=\"ptx\" lang=\"en-US\">");
            out.print(JudgeUtils.getHtmlFormattedString(output));
            out.print("</div>");
        }
        if (StringUtils.hasText(sampleInput)) {
            out.print("<p class=\"pst\">Sample Input</p><div class=\"ptx\" style=\"white-space:pre\" lang=\"en-US\">");
            out.print(sampleInput);
            out.print("</div>");
        }
        if (StringUtils.hasText(sampleOutput)) {
            out.print("<p class=\"pst\">Sample Output</p><div class=\"ptx\" style=\"white-space:pre\" lang=\"en-US\">");
            out.print(sampleOutput);
            out.print("</div>");
        }
        if (StringUtils.hasText(hint)) {
            out.print("<p class=\"pst\">Hint</p><div class=\"ptx\" lang=\"en-US\">");
            out.print(JudgeUtils.getHtmlFormattedString(hint));
            out.print("</div>");
        }
        if (ended && StringUtils.hasText(source)) {
            out.print("<p class=\"pst\">Source</p><div class=\"ptx\" lang=\"en-US\">");
            out.print(JudgeUtils.getHtmlFormattedString(source));
            out.print("</div>");
        }
        out.print("</td></tr></table><font color=\"#333399\" size=\"3\"><p align=\"center\">[<a href=\"submitpage?problem_id=");
        out.print(problemId);
        if (contestId != null) {
            out.print("&contest_id=");
            out.print(contestId);
        }
        out.print("\">Submit</a>]&nbsp;&nbsp;[<a href=problemstatus?problem_id=");
        out.print(problemId);
        out.print(">Status</a>]&nbsp;&nbsp; [<a href=\"bbs?problem_id=");
        out.print(problemId);
        out.print("\">Discuss</a>]</font></p></body></html>");
    }

}
