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
import cn.edu.zjnu.acm.judge.mapper.SubmissionMapper;
import cn.edu.zjnu.acm.judge.service.LanguageService;
import cn.edu.zjnu.acm.judge.service.UserDetailService;
import cn.edu.zjnu.acm.judge.util.ResultType;
import cn.edu.zjnu.acm.judge.util.URLBuilder;
import com.google.gson.Gson;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

/**
 *
 * @author zhanhb
 */
@Controller
@Slf4j
public class ProblemStatusController {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private SubmissionMapper submissionMapper;
    @Autowired
    private LanguageService languageService;

    @GetMapping("/gotoproblem")
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

    @GetMapping(value = "/problemstatus", produces = TEXT_HTML_VALUE)
    @SuppressWarnings("AssignmentToMethodParameter")
    public ResponseEntity<String> status(HttpServletRequest request,
            @RequestParam("problem_id") long id,
            @PageableDefault(size = 20, sort = {"time", "memory", "code_length"}) Pageable pageable,
            Authentication authentication) {
        log.debug("{}", pageable);
        if (pageable.getPageSize() > 500) {
            pageable = new PageRequest(pageable.getPageNumber(), 500, pageable.getSort());
        }
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
        StringBuilder sb = new StringBuilder();

        sb.append("<html><head><title>").append(id).append("'s Status List</title></head><body>" + "<STYLE> v\\:* { Behavior: url(#default#VML) }o\\:* { behavior: url(#default#VML) }</STYLE>" + "<table><tr><td valign=top><div style='position:relative; height:650px; width:260px'>" + "<script src='js/problemstatus.js'></script>" + "<script>var sa = ").append(new Gson().toJson(Arrays.asList(counts, scores, urls))).append(";var len = 0;");
        sb.append("if (sa[0].length > sa[1].length){ len = sa[1].length; }else { len = sa[0].length; }" + "table(len,0,0,600,600,'Statistics','',200,250,").append(submitUser).append(",").append(solved).append(",'status?problem_id=").append(id).append("'); </script></div></td>" + "<td valign=top><p align=center><font size=5 color=#333399>Best solutions of Problem <a href=showproblem?problem_id=").append(id).append(">").append(id).append("</a></font></p>" + "<TABLE cellSpacing='0' cellPadding='0' width='700' border='1' class='problem-status table-back' bordercolor='#FFF'>" + "<tr class=inc><th width=5%>Rank</th><th align=center width=15%>Run ID</th>" + "<th width=15%>User</th>" + "<th width=10%><a class=sortable href=problemstatus?problem_id=").append(id).append("&sort=memory,time,code_length>Memory</a></th>" + "<th width=10%><a class=sortable href=problemstatus?problem_id=").append(id).append("&sort=time,memory,code_length>Time</a></th>" + "<th width=10%>Language</td>" + "<th width=10%><a class=sortable href=problemstatus?problem_id=").append(id).append("&sort=code_length,time,memory>Code Length</a></th>"
                + "<th width=25%>Submit Time</th></tr>");
        List<Submission> bestSubmissions = submissionMapper.bestSubmission(id, pageable);

        boolean isAdmin = UserDetailService.isAdminLoginned(request);
        boolean isSourceBrowser = UserDetailService.isSourceBrowser(request);
        boolean canView = isAdmin || isSourceBrowser;
        long rank = pageable.getOffset();
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
            if (canView || contestId == null) {
                sb.append("<td>").append(memory).append("K</td><td>").append(time).append("MS</td>");
            } else {
                sb.append("<td>&nbsp;</td><td>&nbsp;</td>");
            }
            if (canView || UserDetailService.isUser(authentication, userId)) {
                sb.append("<td><a href=showsource?solution_id=").append(submissionId).append(" target=_blank>").append(language).append("</a></td>");
            } else {
                sb.append("<td>").append(language).append("</td>");
            }
            if (canView || contestId == null) {
                sb.append("<td>").append(length).append("K</td>");
            } else {
                sb.append("<td>&nbsp;</td>");
            }
            sb.append("<td>").append(formatter.format(inDate)).append("</td></tr>");
        }
        String url = request.getContextPath() + URLBuilder.fromRequest(request).replaceQueryParam("page").toString();

        sb.append("</table><p align=center>" + "[<a href=\"").append(url).append("\">Top</a>]" + "[<a href=\"").append(url).append("&page=").append(Math.max(pageable.getPageNumber() - 1, 0)).append("\">Previous Page</a>]" + "[<a href=\"").append(url).append("&page=").append(pageable.getPageNumber() + 1).append("\">Next Page</a>]</p></td></tr></table></body></html>");
        return ResponseEntity.ok().contentType(MediaType.valueOf("text/html;charset=UTF-8")).body(sb.toString());
    }

}
