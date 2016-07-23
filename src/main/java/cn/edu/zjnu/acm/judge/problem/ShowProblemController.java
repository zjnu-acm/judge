package cn.edu.zjnu.acm.judge.problem;

import cn.edu.zjnu.acm.judge.config.JudgeConfiguration;
import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.ContestMapper;
import cn.edu.zjnu.acm.judge.mapper.ProblemMapper;
import cn.edu.zjnu.acm.judge.util.JudgeUtils;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

@Controller
public class ShowProblemController {

    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private ContestMapper contestMapper;
    @Autowired
    private JudgeConfiguration judgeConfiguration;

    @GetMapping(value = "/showproblem", produces = TEXT_HTML_VALUE)
    public ResponseEntity<String> showproblem(Model model,
            @RequestParam("problem_id") long problemId,
            Locale locale) {
        Problem problem = problemMapper.findOne(problemId, locale.getLanguage());
        if (problem == null) {
            throw new MessageException("Can not find problem (ID:" + problemId + ")", HttpStatus.NOT_FOUND);
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
                Long problemIdInContest = contestMapper.getProblemIdInContest(contestId, problemId);
                contestNum = problemIdInContest == null ? -1 : problemIdInContest;
            }
            if (problem.isDisabled() && contestId == null) {
                started = false;
            }
        }
        if (!started) {
            throw new MessageException("Can not find problem (ID:" + problemId + ")", HttpStatus.NOT_FOUND);
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

        StringBuilder sb = new StringBuilder(800);

        if (contestId == null) {
            sb.append("<html><head><title>")
                    .append(problemId)
                    .append(" -- ").
                    append(StringUtils.escapeXml(title))
                    .append("</title></head><body>" + "<table border=0 width=100% class=table-back><tr><td><div class=\"ptt\" lang=\"en-US\">")
                    .append(title)
                    .append("</div>");
        } else {
            model.addAttribute("contestId", contestId);
            sb.append("<html><head><title>").append((char) (contestNum + 'A')).append(":").append(problemId).append(" -- ").append(title).append("</title></head><body>"
                    + "<table border=0 width=100% class=table-back><tr><td><table border=0 width=100%><tr>");
            List<Problem> problems = contestMapper.getProblems(contestId, null, locale.getLanguage());
            for (Problem p : problems) {
                sb.append("<td style='text-align:center'><a href=showproblem?problem_id=").append(p.getOrign()).append("><b>").append((char) (p.getId() + 'A')).append("</b></a></td>");
            }
            sb.append("</tr></table>" + "<div class=\"ptt\" lang=\"en-US\">Problem ").append((char) (contestNum + 'A')).append(":").append(title).append("</div>");
        }
        sb.append("<div class=\"plm\"><table align=\"center\"><tr><td><b>Time Limit:</b> ").append(timeLimit).append("MS</td>" + "<td width=\"10px\"></td><td colspan=\"3\"><b>Memory Limit:</b> ").append(memoryLimit).append("K</td></tr>" + "<tr><td><b>Total Submissions:</b> ").append(submit).append("</td><td width=\"10px\"></td>" + "<td><b>Accepted:</b> ").append(accepted).append("</td>");
        boolean isSpecial = Files.exists(dataPath.resolve(JudgeConfiguration.VALIDATE_FILE_NAME));

        if (isSpecial) {
            sb.append("<td width=\"10px\"></td><td style=\"font-weight:bold; color:red;\">Special Judge</td>");
        }
        sb.append("</table></div>");
        if (!StringUtils.isEmptyOrWhitespace(description)) {
            sb.append("<p class=\"pst\">Description</p><div class=\"ptx\" lang=\"en-US\">");
            sb.append(JudgeUtils.getHtmlFormattedString(description));
            sb.append("</div>");
        }
        if (!StringUtils.isEmptyOrWhitespace(input)) {
            sb.append("<p class=\"pst\">Input</p><div class=\"ptx\" lang=\"en-US\">");
            sb.append(JudgeUtils.getHtmlFormattedString(input));
            sb.append("</div>");
        }
        if (!StringUtils.isEmptyOrWhitespace(output)) {
            sb.append("<p class=\"pst\">Output</p><div class=\"ptx\" lang=\"en-US\">");
            sb.append(JudgeUtils.getHtmlFormattedString(output));
            sb.append("</div>");
        }
        if (!StringUtils.isEmptyOrWhitespace(sampleInput)) {
            sb.append("<p class=\"pst\">Sample Input</p><div class=\"ptx\" style=\"white-space:pre\" lang=\"en-US\">");
            sb.append(sampleInput);
            sb.append("</div>");
        }
        if (!StringUtils.isEmptyOrWhitespace(sampleOutput)) {
            sb.append("<p class=\"pst\">Sample Output</p><div class=\"ptx\" style=\"white-space:pre\" lang=\"en-US\">");
            sb.append(sampleOutput);
            sb.append("</div>");
        }
        if (!StringUtils.isEmptyOrWhitespace(hint)) {
            sb.append("<p class=\"pst\">Hint</p><div class=\"ptx\" lang=\"en-US\">");
            sb.append(JudgeUtils.getHtmlFormattedString(hint));
            sb.append("</div>");
        }
        if (ended && !StringUtils.isEmptyOrWhitespace(source)) {
            sb.append("<p class=\"pst\">Source</p><div class=\"ptx\" lang=\"en-US\">");
            sb.append(JudgeUtils.getHtmlFormattedString(source));
            sb.append("</div>");
        }
        sb.append("</td></tr></table><font color=\"#333399\" size=\"3\"><p align=\"center\">[<a href=\"submitpage?problem_id=");
        sb.append(problemId);
        if (contestId != null) {
            sb.append("&contest_id=");
            sb.append(contestId);
        }
        sb.append("\">Submit</a>]&nbsp;&nbsp;[<a href=problemstatus?problem_id=");
        sb.append(problemId);
        sb.append(">Status</a>]&nbsp;&nbsp; [<a href=\"bbs?problem_id=");
        sb.append(problemId);
        sb.append("\">Discuss</a>]</font></p></body></html>");
        return ResponseEntity.ok().contentType(MediaType.valueOf("text/html;charset=UTF-8")).body(sb.toString());
    }

}
