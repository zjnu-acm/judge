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

import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.domain.ScoreCount;
import cn.edu.zjnu.acm.judge.domain.Submission;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.ProblemMapper;
import cn.edu.zjnu.acm.judge.service.LanguageService;
import cn.edu.zjnu.acm.judge.service.UserDetailService;
import cn.edu.zjnu.acm.judge.util.ResultType;
import com.google.gson.Gson;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

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
    private LanguageService languageService;

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

    @RequestMapping(value = "/problemstatus", method = {RequestMethod.GET, RequestMethod.HEAD}, produces = TEXT_HTML_VALUE)
    public ResponseEntity<String> status(HttpServletRequest request,
            @RequestParam("problem_id") long id,
            @RequestParam(value = "start", defaultValue = "0") long start,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "orderby", defaultValue = "time") String orderby) {
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
        Problem problem = problemMapper.findOneNoI18n(id);
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
        List<ScoreCount> list = problemMapper.groupByScore(id);
        ArrayList<String> scores = new ArrayList<>(list.size());
        ArrayList<Long> counts = new ArrayList<>(list.size());
        ArrayList<String> urls = new ArrayList<>(list.size());
        for (ScoreCount scoreCount : list) {
            int score = scoreCount.getScore();
            scores.add(ResultType.getShowsourceString(score));
            counts.add(scoreCount.getCount());
            urls.add("status?problem_id=" + id + "&score=" + score);
        }
        StringBuilder sb = new StringBuilder(groupBy);

        sb.append("<html><head><title>").append(id).append("'s Status List</title></head><body>" + "<STYLE> v\\:* { Behavior: url(#default#VML) }o\\:* { behavior: url(#default#VML) }</STYLE>" + "<table><tr><td valign=top><div style='position:relative; height:650px; width:260px'>" + "<script src='js/problemstatus.js'></script>" + "<script>var sa = ").append(new Gson().toJson(Arrays.asList(counts, scores, urls))).append(";var len = 0;");
        sb.append("if (sa[0].length > sa[1].length){ len = sa[1].length; }else { len = sa[0].length; }" + "table(len,0,0,600,600,'Statistics','',200,250,").append(submitUser).append(",").append(solved).append(",'status?problem_id=").append(id).append("'); </script></div></td>" + "<td valign=top><p align=center><font size=5 color=#333399>Best solutions of Problem <a href=showproblem?problem_id=").append(id).append(">").append(id).append("</a></font></p>" + "<TABLE cellSpacing='0' cellPadding='0' width='700' border='1' class='problem-status table-back' bordercolor='#FFF'>" + "<tr class=inc><th width=5%>Rank</th><th align=center width=15%>Run ID</th>" + "<th width=15%>User</th>" + "<th width=10%><a class=sortable href=problemstatus?problem_id=").append(id).append("&orderby=memory>Memory</a></th>" + "<th width=10%><a class=sortable href=problemstatus?problem_id=").append(id).append("&orderby=time>Time</a></th>" + "<th width=10%>Language</td>" + "<th width=10%><a class=sortable href=problemstatus?problem_id=").append(id).append("&orderby=clen>Code Length</a></th>"
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
            String language = languageService.getLanguage(s.getLanguage()).getName();
            String length = df.format(s.getSourceLength() / 1024.);
            sb.append("<tr align=center><td>").append(rank).append("</td><td>").append(submissionId).append("</td>" + "<td><a href=userstatus?user_id=").append(userId).append(">").append(userId).append("</a></td>");
            if (canView || (contestId == null)) {
                sb.append("<td>").append(memory).append("K</td><td>").append(time).append("MS</td>");
            } else {
                sb.append("<td>&nbsp;</td><td>&nbsp;</td>");
            }
            if (canView || UserDetailService.isUser(request, userId)) {
                sb.append("<td><a href=showsource?solution_id=").append(submissionId).append(" target=_blank>").append(language).append("</a></td>");
            } else {
                sb.append("<td>").append(language).append("</td>");
            }
            if (canView || (contestId == null)) {
                sb.append("<td>").append(length).append("K</td>");
            } else {
                sb.append("<td>&nbsp;</td>");
            }
            sb.append("<td>").append(formatter.format(inDate)).append("</td></tr>");
        }
        String str10 = "[<a href=\"problemstatus?problem_id=" + id + "&size=" + size + "&orderby=" + orderby;
        sb.append("</table><p align=center>").append(str10).append("\">Top</a>]").append(str10).append("&start=").append(Math.max(start - size, 0)).append("\">Previous Page</a>]").append(str10).append("&start=").append(start + size).append("\">Next Page</a>]</p></td></tr></table></body></html>");
        return ResponseEntity.ok().contentType(MediaType.valueOf("text/html;charset=UTF-8")).body(sb.toString());
    }

}
