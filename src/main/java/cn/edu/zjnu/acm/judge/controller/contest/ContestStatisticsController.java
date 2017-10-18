package cn.edu.zjnu.acm.judge.controller.contest;

import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.domain.Language;
import cn.edu.zjnu.acm.judge.service.ContestService;
import cn.edu.zjnu.acm.judge.service.LanguageService;
import cn.edu.zjnu.acm.judge.util.JudgeUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.unbescape.html.HtmlEscape;

import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

@Controller
@Slf4j
public class ContestStatisticsController {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private ContestService contestService;
    @Autowired
    private LanguageService languageService;

    @GetMapping(value = "/conteststatistics", produces = TEXT_HTML_VALUE)
    public String contestStatistics(
            HttpServletRequest request,
            Model model,
            @RequestParam("contest_id") long contestId) throws SQLException {
        Instant now = Instant.now();
        Contest contest = contestService.findOneByIdAndNotDisabled(contestId);
        String title = contest.getTitle();
        Instant endTime = contest.getEndTime();
        model.addAttribute("contestId", contestId);

        model.addAttribute("title", "Contest Statistics");
        StringBuilder sb = new StringBuilder("<p align=center><font size=5 color=blue>Contest Statistics--");
        sb.append(HtmlEscape.escapeHtml4Xml(title));
        if (!contest.isEnded()) {
            if (endTime != null) {
                sb.append("<br/>Time to go:").append(JudgeUtils.formatTime(now, endTime));
            } else {
                sb.append("<br/>Time to go Infinity");
            }
        }
        sb.append("</font></p>"
                + "<TABLE align=center cellSpacing=0 cellPadding=0 width=600 border=1 class=table-back style=\"border-collapse: collapse\" bordercolor=#FFF>"
                + "<tr bgcolor=#6589D1><th>&nbsp;</th><th>100</th><th>99~70</th><th>69~31</th><th>30~1</th><th>0</th><th>CE</th><th>Others</th><th>Total</th>");

        Map<Integer, Language> languages = languageService.getAvailableLanguages();
        int languageCount = languages.size();
        StringBuilder sql = new StringBuilder(600).append("select ");
        for (int i : languages.keySet()) {
            sql.append("sum(if(language=").append(i).append(",1,0)) g").append(i).append(",");
        }
        sql.append("s.problem_id,sum(if(score=100,1,0)) A,sum(if(score<100 and score >=70,1,0)) B,sum(if(score<70 and score >30,1,0)) D,sum(if(score>0 and score <=30,1,0)) C,sum(if(score=0,1,0)) E,sum(if(score=-7,1,0)) F,sum(if(score<-7 or score > 100,1,0)) G,count(*) Total from contest_problem cp left join solution s on cp.problem_id=s.problem_id and cp.contest_id=s.contest_id where s.contest_id=? group by cp.problem_id order by cp.num");

        String[] judgeStatus = {"A", "B", "C", "D", "E", "F", "G", "Total"};
        long[] byScore = new long[judgeStatus.length];
        long[] byLanguage = new long[languageCount];
        sb.append("<th>&nbsp;</th>");
        languages.values()
                .stream()
                .map(Language::getName)
                .map(HtmlEscape::escapeHtml4Xml)
                .forEach(languageName -> sb.append("<th>").append(languageName).append("</th>"));
        sb.append("</tr>");
        log.debug("{}", sql);

        Map<Long, long[]> problemsMap = contestService.getProblemsMap(contestId);
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            ps.setLong(1, contestId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    long problemId = rs.getLong("problem_id");
                    long[] num = problemsMap.get(problemId);
                    if (num != null) {
                        sb.append("<tr><th><a href=contests/").append(contestId)
                                .append("/problems/").append(num[1]).append(".html>")
                                .append(contestService.toProblemIndex(num[0])).append("</a></th>");
                    }
                    for (int i = 0; i < judgeStatus.length; ++i) {
                        long value = rs.getLong(judgeStatus[i]);
                        byScore[i] += value;
                        if (value == 0) {
                            sb.append("<td>&nbsp;</td>");
                        } else if (i == judgeStatus.length - 1) {
                            sb.append("<th><a href=status?contest_id=").append(contestId).append("&problem_id=").append(problemId).append(">").append(value).append("</a></th>");
                        } else {
                            sb.append("<td>").append(value).append("</td>");
                        }
                    }
                    sb.append("<td>&nbsp;</td>");
                    for (int i = 0; i < languageCount; ++i) {
                        long value = rs.getLong(i + 1);
                        byLanguage[i] += value;
                        if (value == 0) {
                            sb.append("<td>&nbsp;</td>");
                        } else {
                            sb.append("<td>").append(value).append("</td>");
                        }
                    }
                    sb.append("</tr>");
                }
            }
        }
        sb.append("<tr><th>Total</th>");
        for (int i = 0; i < judgeStatus.length; ++i) {
            if (byScore[i] == 0) {
                sb.append("<td>&nbsp;</td>");
            } else if (i == judgeStatus.length - 1) {
                sb.append("<th>").append(byScore[i]).append("</th>");
            } else {
                sb.append("<td>").append(byScore[i]).append("</td>");
            }
        }
        sb.append("<td>&nbsp;</td>");
        for (int i = 0; i < languageCount; ++i) {
            if (byLanguage[i] == 0) {
                sb.append("<td>&nbsp;</td>");
            } else {
                sb.append("<td>").append(byLanguage[i]).append("</td>");
            }
        }
        sb.append("</tr></table>");
        model.addAttribute("content", sb.toString());
        return "legacy";
    }

}
