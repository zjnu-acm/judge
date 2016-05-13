package cn.edu.zjnu.acm.judge.submission;

import cn.edu.zjnu.acm.judge.config.LanguageFactory;
import cn.edu.zjnu.acm.judge.domain.Language;
import cn.edu.zjnu.acm.judge.domain.Submission;
import cn.edu.zjnu.acm.judge.domain.SubmissionCriteria;
import cn.edu.zjnu.acm.judge.exception.BadRequestException;
import cn.edu.zjnu.acm.judge.mapper.ContestMapper;
import cn.edu.zjnu.acm.judge.mapper.SubmissionMapper;
import cn.edu.zjnu.acm.judge.service.SubmissionService;
import cn.edu.zjnu.acm.judge.service.UserDetailService;
import cn.edu.zjnu.acm.judge.util.ResultType;
import cn.edu.zjnu.acm.judge.util.URLBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class StatusController {

    @Autowired
    private SubmissionMapper submissionMapper;
    @Autowired
    private ContestMapper contestMapper;
    @Autowired
    private SubmissionService submissionService;

    @RequestMapping(value = {"/status", "/submissions"}, method = {RequestMethod.GET, RequestMethod.HEAD})
    public void status(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(value = "problem_id", defaultValue = "") String pid,
            @RequestParam(value = "contest_id", required = false) Long contestId,
            @RequestParam(value = "language", defaultValue = "-1") int language,
            @RequestParam(value = "size", defaultValue = "20") int size,
            final @RequestParam(value = "bottom", required = false) Long bottom,
            @RequestParam(value = "score", required = false) Integer sc,
            @RequestParam(value = "user_id", defaultValue = "") String userId,
            final @RequestParam(value = "top", required = false) Long top)
            throws IOException {
        long problemId = 0;
        String query;
        try {
            query = URLBuilder.fromRequest(request)
                    .replaceQueryParam("top")
                    .replaceQueryParam("bottom")
                    .toString();
        } catch (IllegalStateException | IllegalArgumentException ex) {
            throw new BadRequestException();
        }
        log.debug("query={}", query);
        try {
            problemId = Long.parseLong(pid);
        } catch (NumberFormatException ex) {
            if (contestId != null && pid.length() == 1) {
                // TODO the character is the index in the list.
                int x = Character.toUpperCase(pid.charAt(0)) - 'A';
                try {
                    problemId = contestMapper.getProblems(contestId, null).get(x).getOrign();
                } catch (IndexOutOfBoundsException ignore) {
                }
            }
        }
        if (size > 500) {
            size = 500;
        }
        SubmissionCriteria criteria = SubmissionCriteria.builder()
                .problem(problemId == 0 ? null : problemId)
                .contest(contestId)
                .score(sc)
                .size(size)
                .top(top)
                .bottom(bottom)
                .user(StringUtils.hasText(userId) ? userId : null)
                .language(language != -1 ? language : null)
                .build();
        log.info("{}", criteria);
        List<Submission> submissions = submissionMapper.findAllByCriteria(criteria);

        Long min = submissions.stream()
                .map(Submission::getId)
                .min(Comparator.naturalOrder())
                .orElseGet(() -> top != null ? top : bottom != null ? bottom : null);
        Long max = submissions.stream()
                .map(Submission::getId)
                .max(Comparator.naturalOrder())
                .orElseGet(() -> bottom != null ? bottom : top != null ? top : null);

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        request.setAttribute("contestId", contestId);
        out.print("<html><head><title>Problem Status List</title></head><body>"
                + "<p align=center><font size=4 color=#333399>Problem Status List</font></p>"
                + "<form method=get action='status'/>Problem ID:<input type=text name=problem_id size=8 value=\""
                + StringEscapeUtils.escapeHtml4(pid) + "\"/> User ID:<input type=text name=user_id size=15 value=\""
                + StringEscapeUtils.escapeHtml4(userId) + "\"/>"
                + " Language:"
                + "<select size=\"1\" name=\"language\">"
                + "<option value=\"\">All</option>");
        for (Map.Entry<Integer, Language> entry : LanguageFactory.getLanguages().entrySet()) {
            int key = entry.getKey();
            Language value = entry.getValue();
            out.print("<option value=\"" + key + "\"" + (key == language ? " selected" : "")
                    + ">" + StringEscapeUtils.escapeHtml4(value.getName()) + "</option>");
        }
        out.print("</select>");
        if (contestId != null) {
            out.print("<input type=hidden name=contest_id value='" + contestId + "' />");
        }
        out.print(" <input type=submit value='Go'/></form>"
                + "<TABLE cellSpacing=0 cellPadding=0 width=100% border=1 class=table-back style=\"border-collapse: collapse\" bordercolor=#FFF>"
                + "<tr bgcolor=#6589D1><td align=center width=8%><b>Run ID</b></td><td align=center width=10%><b>User</b></td><td align=center width=6%><b>Problem</b></td>"
                + "<td align=center width=10%><b>Result</b></td><td align=center width=10%><b>Score</b></td><td align=center width=7%><b>Memory</b></td><td align=center width=7%><b>Time</b></td><td align=center width=7%><b>Language</b></td><td align=center width=7%><b>Code Length</b></td><td align=center width=17%><b>Submit Time</b></td></tr>");
        boolean admin = UserDetailService.isAdminLoginned(request);
        boolean SourceBrowser = UserDetailService.isSourceBrowser(request);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Submission submission : submissions) {
            long id = submission.getId();
            String user_id1 = submission.getUser();
            long problem_id1 = submission.getProblem();
            Long contest_id1 = submission.getContest();
            long num = submission.getNum();
            int score = submission.getScore();
            Instant inDate = submission.getInDate();
            String language1 = LanguageFactory.getLanguage(submission.getLanguage()).getName();
            String color;
            if (score == 100) {
                color = "blue";
            } else {
                color = "red";
            }
            out.print("<tr align=center><td>" + id + "</td>");
            String problemString;
            if (contestId == null || num == -1) {
                problemString = "" + problem_id1;
            } else {
                problemString = Character.toString((char) (num + 'A'));
            }
            out.print("<td><a href=userstatus?user_id=" + user_id1 + ">"
                    + user_id1 + "</a></td><td><a href=showproblem?problem_id="
                    + problem_id1 + ">" + problemString + "</a></td>");

            if (score == ResultType.COMPILE_ERROR) {
                if (submissionService.canView(request, submission)) {
                    out.print("<td><a href=\"showcompileinfo?solution_id="
                            + id + "\" target=_blank><font color=green>"
                            + ResultType.getResultDescription(score) + "</font></a></td>");
                } else {
                    out.print("<td><font color=green>" + ResultType.getResultDescription(score) + "</font></td>");
                }
            } else if (submissionService.canView(request, submission)) {
                out.print("<td><a href=showsolutiondetails?solution_id=" + id + " target=_blank><strong><font color=" + color + ">"
                        + ResultType.getResultDescription(score)
                        + "</font></strong></a></td>");
            } else {
                out.print("<td><font color=" + color + ">" + ResultType.getResultDescription(score)
                        + "</font></a></td>");
            }
            if (score <= 100 && score >= 0) {
                out.print("<td>" + score + "</td>");
            } else {
                out.print("<td>&nbsp;</td>");
            }
            boolean ended = true;
            if (!admin && contest_id1 != null) {
                ended = contestMapper.findOne(contest_id1).isEnded();
            }
            if (score == 100 && ended) {
                out.print("<td>" + submission.getMemory() + "K</td><td>"
                        + submission.getTime() + "MS</td>");
            } else {
                out.print("<td>&nbsp;</td><td>&nbsp;</td>");
            }
            if (admin || SourceBrowser || UserDetailService.isUser(request, user_id1)) {
                out.print("<td><a href=showsource?solution_id="
                        + id + " target=_blank>" + language1 + "</a></td>");
            } else {
                out.print("<td>" + language1 + "</td>");
            }
            if (ended) {
                int sourceLength = submission.getSourceLength();
                String t = sourceLength > 2048 ? new DecimalFormat("0.00").format(sourceLength / 1024.) + " KB" : sourceLength + " B";
                out.print("<td>" + t + "</td>");
            } else {
                out.print("<td>&nbsp;</td>");
            }
            out.print("<td>" + sdf.format(Timestamp.from(inDate)) + "</td></tr>");
        }
        query = request.getContextPath() + query;
        out.print("</table><p align=center>[<a href=\"" + query + "\">Top</a>]&nbsp;&nbsp;");
        query += query.contains("?") ? '&' : '?';
        out.print("[<a href=\"" + query + "bottom=" + (max != null ? max : "") + "\"><font color=blue>Previous Page</font></a>]"
                + "&nbsp;&nbsp;[<a href=\"" + query + "top=" + (min != null ? min : "") + "\"><font color=blue>Next Page</font></a>]&nbsp;&nbsp;</p>"
                + "<script>!function(w){setTimeout(function(){w.location.reload()},60000)}(this)</script></body></html>");
    }

}
