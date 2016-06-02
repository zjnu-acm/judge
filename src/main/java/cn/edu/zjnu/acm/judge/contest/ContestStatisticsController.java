package cn.edu.zjnu.acm.judge.contest;

import cn.edu.zjnu.acm.judge.config.LanguageFactory;
import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.domain.Language;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.ContestMapper;
import cn.edu.zjnu.acm.judge.util.JudgeUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ContestStatisticsController {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private ContestMapper contestMapper;
    @Autowired
    private LanguageFactory languageFactory;

    @RequestMapping(value = "/conteststatistics", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ResponseEntity<String> conteststatistics(HttpServletRequest request,
            @RequestParam("contest_id") long contestId) throws SQLException {
        Instant now = Instant.now();
        Contest contest = contestMapper.findOneByIdAndDefunctN(contestId);
        if (contest == null || !contest.isStarted()) {
            throw new MessageException("No such contest", HttpStatus.NOT_FOUND);
        }
        String title = contest.getTitle();
        Instant endTime = contest.getEndTime();
        request.setAttribute("contestId", contestId);

        StringBuilder sb = new StringBuilder("<html><head><title>Contest Statistics</title></head><body><p align=center><font size=5 color=blue>Contest Statistics--");
        sb.append(StringEscapeUtils.escapeHtml4(title));
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

        Map<Integer, Language> languages = languageFactory.getLanguages();
        int languageCount = languages.size();
        String str2 = "select ";
        for (int i : languages.keySet()) {
            str2 = str2 + "count(if(language=" + i + ",1,null)) t" + i + ",";
        }
        str2 += "problem_id,num,count(if(score=100,1,0)) as AC,count(if(score<100 and score >=70,1,0)) as PE,count(if(score<70 and score >30,1,null)) as TLE,count(if(score>0 and score <=30,1,null)) as WA,count(if(score=0,1,null)) as RE,count(if(score=-7,1,null)) as CE,count(if(score<-7 or score > 100,1,null)) as Others,count(*) as Total from solution where contest_id=? group by problem_id order by num";

        String[] judgeStatus = {"AC", "PE", "WA", "TLE", "RE", "CE", "Others", "Total"};
        long[] arrayOfLong1 = new long[judgeStatus.length];
        long[] arrayOfLong2 = new long[languageCount];
        sb.append("<th>&nbsp;</th>");
        languages.values().forEach(language -> sb.append("<th>").append(StringEscapeUtils.escapeHtml4(language.getName())).append("</th>"));
        sb.append("</tr>");
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(str2)) {
            ps.setLong(1, contestId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    long problemId = rs.getLong("problem_id");
                    long num = rs.getLong("num");
                    sb.append("<tr><th><a href=showproblem?problem_id=").append(problemId).append(">").append((char) ('A' + num)).append("</a></th>");
                    for (int i = 0; i < judgeStatus.length; ++i) {
                        long l6 = rs.getLong(judgeStatus[i]);
                        arrayOfLong1[i] += l6;
                        if (l6 == 0) {
                            sb.append("<td>&nbsp;</td>");
                        } else if (i == judgeStatus.length - 1) {
                            sb.append("<th><a href=status?contest_id=").append(contestId).append("&problem_id=").append(problemId).append(">").append(l6).append("</a></th>");
                        } else {
                            sb.append("<td>").append(l6).append("</td>");
                        }
                    }
                    sb.append("<td>&nbsp;</td>");
                    for (int i = 0; i < languageCount; ++i) {
                        long l6 = rs.getLong(i + 1);
                        arrayOfLong2[i] += l6;
                        if (l6 == 0) {
                            sb.append("<td>&nbsp;</td>");
                        } else {
                            sb.append("<td>").append(l6).append("</td>");
                        }
                    }
                    sb.append("</tr>");
                }
            }
        }
        sb.append("<tr><th>Total</th>");
        for (int i = 0; i < judgeStatus.length; ++i) {
            if (arrayOfLong1[i] == 0) {
                sb.append("<td>&nbsp;</td>");
            } else if (i == judgeStatus.length - 1) {
                sb.append("<th>").append(arrayOfLong1[i]).append("</th>");
            } else {
                sb.append("<td>").append(arrayOfLong1[i]).append("</td>");
            }
        }
        sb.append("<td>&nbsp;</td>");
        for (int i = 0; i < languageCount; ++i) {
            if (arrayOfLong2[i] == 0) {
                sb.append("<td>&nbsp;</td>");
            } else {
                sb.append("<td>").append(arrayOfLong2[i]).append("</td>");
            }
        }
        sb.append("</tr></table></body></html>");
        return ResponseEntity.ok(sb.toString());
    }
}
