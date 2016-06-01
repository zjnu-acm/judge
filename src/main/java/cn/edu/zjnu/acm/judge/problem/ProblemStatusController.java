/*
 * Copyright 2016 ZJNU ACM.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.edu.zjnu.acm.judge.problem;

import cn.edu.zjnu.acm.judge.config.LanguageFactory;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.domain.ScoreCount;
import cn.edu.zjnu.acm.judge.domain.Submission;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.ProblemMapper;
import cn.edu.zjnu.acm.judge.service.UserDetailService;
import cn.edu.zjnu.acm.judge.util.ResultType;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author zhanhb
 */
@Controller
public class ProblemStatusController {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private LanguageFactory languageFactory;

    @RequestMapping(value = "/gotoproblem", method = {RequestMethod.GET, RequestMethod.HEAD})
    public String gotoProblem(@RequestParam(value = "pid", required = false) String pid,
            RedirectAttributes redirectAttributes) {
        try {
            redirectAttributes.addAttribute("problem_id", Long.parseLong(pid));
            return "redirect:/showproblem";
        } catch (NumberFormatException ex) {
            redirectAttributes.addAttribute("sstr", pid);
            return "redirect:/searchproblem";
        }
    }

    @RequestMapping(value = "/problemstatus", method = {RequestMethod.GET, RequestMethod.HEAD})
    public void status(HttpServletRequest request, HttpServletResponse response,
            @RequestParam("problem_id") long id,
            @RequestParam(value = "start", defaultValue = "0") long start,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "orderby", defaultValue = "time") String orderby)
            throws IOException {
        if (size > 500) {
            size = 500;
        }
        String groupBy;
        if (orderby.equalsIgnoreCase("memory")) {
            groupBy = " memory,time,code_length";
        } else if (orderby.equalsIgnoreCase("clen")) {
            groupBy = " code_length,time,memory";
        } else {
            groupBy = " time,memory,code_length";
            orderby = "time";
        }
        groupBy += ",user_id ";
        Problem problem = problemMapper.findOne(id);
        if (problem == null) {
            throw new MessageException("No such problem", HttpStatus.NOT_FOUND);
        }
        Long contestId = problem.getContest();
        long submitUser = problem.getSubmitUser();
        long solved = problem.getSolved();
        if (contestId != null) {
            request.setAttribute("contestId", contestId);
        }
        final DateTimeFormatter formatter = dtf.withZone(ZoneId.systemDefault());
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print("<html><head><title>" + id + "'s Status List</title></head><body>"
                + "<STYLE> v\\:* { Behavior: url(#default#VML) }o\\:* { behavior: url(#default#VML) }</STYLE>"
                + "<table><tr><td valign=top><div style='position:relative; height:650px; width:260px'>"
                + "<script src='js/problemstatus.js'></script>"
                + "<script>var sa = [[],[],[]];var len = 0;");
        List<ScoreCount> list = problemMapper.groupByScore(id);
        int ii = 0;
        for (ScoreCount scoreCount : list) {
            int score = scoreCount.getScore();
            String scoreStr = ResultType.getShowsourceString(score);
            out.print("sa[1][" + ii + "]='" + scoreStr + "'; sa[0][" + ii + "]=" + scoreCount.getCount() + "; sa[2][" + ii + "]='status?problem_id=" + id + "&score=" + score + "'; ");
            ii++;
        }
        out.print("if (sa[0].length > sa[1].length){ len = sa[1].length; }else { len = sa[0].length; }"
                + "table(len,0,0,600,600,'Statistics','',200,250," + submitUser + "," + solved + ",'status?problem_id=" + id + "'); </script></div></td>"
                + "<td valign=top><p align=center><font size=5 color=#333399>Best solutions of Problem <a href=showproblem?problem_id=" + id + ">" + id + "</a></font></p>"
                + "<TABLE cellSpacing='0' cellPadding='0' width='700' border='1' class='problem-status table-back' bordercolor='#FFF'>"
                + "<tr class=inc><th width=5%>Rank</th><th align=center width=15%>Run ID</th>"
                + "<th width=15%>User</th>"
                + "<th width=10%><a class=sortable href=problemstatus?problem_id="
                + id + "&orderby=memory>Memory</a></th>"
                + "<th width=10%><a class=sortable href=problemstatus?problem_id="
                + id + "&orderby=time>Time</a></th>"
                + "<th width=10%>Language</td>"
                + "<th width=10%><a class=sortable href=problemstatus?problem_id="
                + id + "&orderby=clen>Code Length</a></th>"
                + "<th width=25%>Submit Time</th></tr>");
        List<Submission> bestSubmissions = problemMapper.bestSubmissions(groupBy, id, start, size);

        boolean isAdmin = UserDetailService.isAdminLoginned(request);
        boolean isSourceBrowser = UserDetailService.isSourceBrowser(request);
        boolean canView = isAdmin || isSourceBrowser;
        long rank = start;
        DecimalFormat df = new DecimalFormat("0.00");
        for (Submission s : bestSubmissions) {
            rank++;
            long submissionId = s.getId();
            String userId = s.getUser();
            Instant inDate = s.getInDate();
            long memory = s.getMemory();
            long time = s.getTime();
            String language = languageFactory.getLanguage(s.getLanguage()).getName();
            String length = df.format(s.getSourceLength() / 1024.);
            out.print("<tr align=center><td>" + rank + "</td><td>" + submissionId + "</td>"
                    + "<td><a href=userstatus?user_id=" + userId + ">" + userId + "</a></td>");
            if (canView || (contestId == null)) {
                out.print("<td>" + memory + "K</td><td>" + time + "MS</td>");
            } else {
                out.print("<td>&nbsp;</td><td>&nbsp;</td>");
            }
            if (canView || UserDetailService.isUser(request, userId)) {
                out.print("<td><a href=showsource?solution_id="
                        + submissionId + " target=_blank>" + language + "</a></td>");
            } else {
                out.print("<td>" + language + "</td>");
            }
            if (canView || (contestId == null)) {
                out.print("<td>" + length + "K</td>");
            } else {
                out.print("<td>&nbsp;</td>");
            }
            out.print("<td>" + formatter.format(inDate) + "</td></tr>");
        }
        String str10 = "[<a href=\"problemstatus?problem_id=" + id + "&size=" + size + "&orderby=" + orderby;
        out.print("</table><p align=center>"
                + str10 + "\">Top</a>]"
                + str10 + "&start=" + Math.max(start - size, 0) + "\">Previous Page</a>]"
                + str10 + "&start=" + (start + size) + "\">Next Page</a>]</p></td></tr></table></body></html>");
    }

}
