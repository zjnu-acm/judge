package cn.edu.zjnu.acm.judge.submission;

import cn.edu.zjnu.acm.judge.domain.Language;
import cn.edu.zjnu.acm.judge.domain.Submission;
import cn.edu.zjnu.acm.judge.domain.SubmissionCriteria;
import cn.edu.zjnu.acm.judge.exception.BadRequestException;
import cn.edu.zjnu.acm.judge.mapper.ContestMapper;
import cn.edu.zjnu.acm.judge.mapper.SubmissionMapper;
import cn.edu.zjnu.acm.judge.service.LanguageService;
import cn.edu.zjnu.acm.judge.service.SubmissionService;
import cn.edu.zjnu.acm.judge.service.UserDetailService;
import cn.edu.zjnu.acm.judge.util.ResultType;
import cn.edu.zjnu.acm.judge.util.URLBuilder;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

@Slf4j
@Controller
public class StatusController {

    @Autowired
    private SubmissionMapper submissionMapper;
    @Autowired
    private ContestMapper contestMapper;
    @Autowired
    private SubmissionService submissionService;
    @Autowired
    private LanguageService languageService;

    @GetMapping(value = {"/status", "/submissions"}, produces = TEXT_HTML_VALUE)
    @SuppressWarnings("AssignmentToMethodParameter")
    public ResponseEntity<String> status(HttpServletRequest request,
            @RequestParam(value = "problem_id", defaultValue = "") String pid,
            @RequestParam(value = "contest_id", required = false) Long contestId,
            @RequestParam(value = "language", defaultValue = "-1") int language,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "bottom", required = false) final Long bottom,
            @RequestParam(value = "score", required = false) Integer sc,
            @RequestParam(value = "user_id", defaultValue = "") String userId,
            @RequestParam(value = "top", required = false) final Long top,
            Authentication authentication) {
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
                    problemId = contestMapper.getProblems(contestId, null, null).get(x).getOrign();
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
                .user(!StringUtils.isEmptyOrWhitespace(userId) ? userId : null)
                .language(language != -1 ? language : null)
                .build();
        log.debug("{}", criteria);
        List<Submission> submissions = submissionMapper.findAllByCriteria(criteria);

        Long min = submissions.stream()
                .map(Submission::getId)
                .min(Comparator.naturalOrder())
                .orElseGet(() -> top != null ? top : bottom != null ? bottom : null);
        Long max = submissions.stream()
                .map(Submission::getId)
                .max(Comparator.naturalOrder())
                .orElseGet(() -> bottom != null ? bottom : top != null ? top : null);

        request.setAttribute("contestId", contestId);

        StringBuilder sb = new StringBuilder("<html><head><title>Problem Status List</title></head><body>"
                + "<p align=center><font size=4 color=#333399>Problem Status List</font></p>"
                + "<form method=get action='status'/><label for='pid'>Problem ID:</label><input id='pid' type=text name=problem_id size=8 value=\"")
                .append(StringUtils.escapeXml(pid)).append("\"/> <label for='uid'>User ID:</label><input id='uid' type=text name=user_id size=15 value=\"")
                .append(StringUtils.escapeXml(userId)).append("\"/>"
                + " <label for='languag'>Language:</label>"
                + "<select id='languag' size=\"1\" name=\"language\">"
                + "<option value=\"\">All</option>");
        for (Map.Entry<Integer, Language> entry : languageService.getAvailableLanguages().entrySet()) {
            int key = entry.getKey();
            Language value = entry.getValue();
            sb.append("<option value=\"").append(key).append("\"").append(key == language ? " selected" : "").append(">").append(StringUtils.escapeXml(value.getName())).append("</option>");
        }
        sb.append("</select>");
        if (contestId != null) {
            sb.append("<input type=hidden name=contest_id value='").append(contestId).append("' />");
        }
        sb.append(" <button type=submit>Go</button></form>"
                + "<TABLE cellSpacing=0 cellPadding=0 width=100% border=1 class=table-back style=\"border-collapse: collapse\" bordercolor=#FFF>"
                + "<tr bgcolor=#6589D1><td align=center width=8%><b>Run ID</b></td><td align=center width=10%><b>User</b></td><td align=center width=6%><b>Problem</b></td>"
                + "<td align=center width=10%><b>Result</b></td><td align=center width=10%><b>Score</b></td><td align=center width=7%><b>Memory</b></td><td align=center width=7%><b>Time</b></td><td align=center width=7%><b>Language</b></td><td align=center width=7%><b>Code Length</b></td><td align=center width=17%><b>Submit Time</b></td></tr>");
        boolean admin = UserDetailService.isAdminLoginned(request);
        boolean sourceBrowser = UserDetailService.isSourceBrowser(request);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Submission submission : submissions) {
            long id = submission.getId();
            String user_id1 = submission.getUser();
            long problem_id1 = submission.getProblem();
            Long contest_id1 = submission.getContest();
            long num = submission.getNum();
            int score = submission.getScore();
            Instant inDate = submission.getInDate();
            String language1 = languageService.getLanguageName(submission.getLanguage());
            String color;
            if (score == 100) {
                color = "blue";
            } else {
                color = "red";
            }
            sb.append("<tr align=center><td>").append(id).append("</td>");
            String problemString;
            if (contestId == null || num == -1) {
                problemString = "" + problem_id1;
            } else {
                problemString = Character.toString((char) (num + 'A'));
            }
            sb.append("<td><a href=userstatus?user_id=").append(user_id1)
                    .append(">").append(user_id1).append("</a></td><td><a href=showproblem?problem_id=")
                    .append(problem_id1).append(">").append(problemString).append("</a></td>");

            if (score == ResultType.COMPILE_ERROR) {
                if (submissionService.canView(request, submission)) {
                    sb.append("<td><a href=\"showcompileinfo?solution_id=").append(id).append("\" target=_blank><font color=green>").append(ResultType.getResultDescription(score)).append("</font></a></td>");
                } else {
                    sb.append("<td><font color=green>").append(ResultType.getResultDescription(score)).append("</font></td>");
                }
            } else if (submissionService.canView(request, submission)) {
                sb.append("<td><a href=showsolutiondetails?solution_id=").append(id).append(" target=_blank><strong><font color=").append(color).append(">").append(ResultType.getResultDescription(score)).append("</font></strong></a></td>");
            } else {
                sb.append("<td><font color=").append(color).append(">").append(ResultType.getResultDescription(score)).append("</font></a></td>");
            }
            if (score <= 100 && score >= 0) {
                sb.append("<td>").append(score).append("</td>");
            } else {
                sb.append("<td>&nbsp;</td>");
            }
            boolean ended = true;
            if (!admin && contest_id1 != null) {
                ended = contestMapper.findOne(contest_id1).isEnded();
            }
            if (score == 100 && ended) {
                sb.append("<td>").append(submission.getMemory()).append("K</td><td>").append(submission.getTime()).append("MS</td>");
            } else {
                sb.append("<td>&nbsp;</td><td>&nbsp;</td>");
            }
            if (admin || sourceBrowser || UserDetailService.isUser(authentication, user_id1)) {
                sb.append("<td><a href=showsource?solution_id=").append(id).append(" target=_blank>").append(language1).append("</a></td>");
            } else {
                sb.append("<td>").append(language1).append("</td>");
            }
            if (ended) {
                int sourceLength = submission.getSourceLength();
                String t = sourceLength > 2048 ? new DecimalFormat("0.00").format(sourceLength / 1024.) + " KB" : sourceLength + " B";
                sb.append("<td>").append(t).append("</td>");
            } else {
                sb.append("<td>&nbsp;</td>");
            }
            sb.append("<td>").append(sdf.format(Timestamp.from(inDate))).append("</td></tr>");
        }
        query = request.getContextPath() + query;
        sb.append("</table><p align=center>[<a href=\"").append(query).append("\">Top</a>]&nbsp;&nbsp;");
        query += query.contains("?") ? '&' : '?';
        sb.append("[<a href=\"").append(query).append("bottom=").append(max != null ? max : "").append("\"><font color=blue>Previous Page</font></a>]" + "&nbsp;&nbsp;[<a href=\"").append(query).append("top=").append(min != null ? min : "").append("\"><font color=blue>Next Page</font></a>]&nbsp;&nbsp;</p>"
                + "<script>!function(w){setTimeout(function(){w.location.reload()},60000)}(this)</script></body></html>");
        return ResponseEntity.ok().contentType(MediaType.valueOf("text/html;charset=UTF-8")).body(sb.toString());
    }

}
