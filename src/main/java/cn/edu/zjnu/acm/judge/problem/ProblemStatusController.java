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
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.ProblemMapper;
import cn.edu.zjnu.acm.judge.service.UserDetailService;
import cn.edu.zjnu.acm.judge.util.ResultType;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private DataSource dataSource;
    @Autowired
    private ProblemMapper problemMapper;

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
            @RequestParam(value = "size", defaultValue = "20") long size,
            @RequestParam(value = "orderby", defaultValue = "time") String orderby)
            throws IOException, SQLException {
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
            throw new MessageException("No such problem");
        }
        Long contestId = problem.getContest();
        long submitUser = problem.getSubmitUser();
        long solved = problem.getSolved();
        if (contestId != null) {
            request.setAttribute("contestId", contestId);
        }
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print("<html><head><title>" + id + "'s Status List</title></head><body>"
                + "<STYLE> v\\:* { Behavior: url(#default#VML) }o\\:* { behavior: url(#default#VML) }</STYLE>"
                + "<table><tr><td valign=top><div style='position:relative; height:650px; width:260px'>"
                + "<script src='js/problemstatus.js'></script>"
                + "<script>var sa = [[],[],[]];var len = 0;");
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement("select score,count(*) as sum from solution where problem_id=? group by score")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                int ii = 0;
                while (rs.next()) {
                    int i = rs.getInt("score");
                    String score = ResultType.getShowsourceString(i);
                    out.print("sa[1][" + ii + "]='" + score + "'; sa[0][" + ii + "]=" + rs.getLong("sum") + "; sa[2][" + ii + "]='status?problem_id=" + id + "&score=" + i + "'; ");
                    ii++;
                }
            }
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
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement("select solution_id,user_id,in_date,language,memory,time,format(code_length/1024,2) as len from solution where problem_id=? and score=100 group by " + groupBy + " limit ?,?")) {
            ps.setLong(1, id);
            ps.setLong(2, start);
            ps.setLong(3, size);
            try (ResultSet rs = ps.executeQuery()) {
                boolean isAdmin = UserDetailService.isAdminLoginned(request);
                boolean isSourceBrowser = UserDetailService.isSourceBrowser(request);
                boolean canView = isAdmin || isSourceBrowser;
                long rank = start;
                while (rs.next()) {
                    rank++;
                    long submissionId = rs.getLong("solution_id");
                    String userId = rs.getString("user_id");
                    Timestamp inDate = rs.getTimestamp("in_date");
                    long memory = rs.getLong("memory");
                    long time = rs.getLong("time");
                    String language = LanguageFactory.getLanguage(rs.getInt("language")).getName();
                    String length = rs.getString("len");
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
                    out.print("<td>" + inDate + "</td></tr>");
                }
            }
        }
        String str10 = "[<a href=\"problemstatus?problem_id=" + id + "&size=" + size + "&orderby=" + orderby;
        out.print("</table><p align=center>"
                + str10 + "\">Top</a>]"
                + str10 + "&start=" + Math.max(start - size, 0) + "\">Previous Page</a>]"
                + str10 + "&start=" + (start + size) + "\">Next Page</a>]</p></td></tr></table></body></html>");
    }

}
