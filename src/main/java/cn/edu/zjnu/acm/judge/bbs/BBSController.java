package cn.edu.zjnu.acm.judge.bbs;

import cn.edu.zjnu.acm.judge.domain.Message;
import cn.edu.zjnu.acm.judge.mapper.MessageMapper;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

@Controller
@Slf4j
public class BBSController {

    @Autowired
    private MessageMapper messageMapper;

    @RequestMapping(value = "/bbs", method = {RequestMethod.GET, RequestMethod.HEAD}, produces = TEXT_HTML_VALUE)
    protected ResponseEntity<String> bbs(
            @RequestParam(value = "problem_id", required = false) Long problemId,
            @RequestParam(value = "size", defaultValue = "50") int threadLimit,
            @RequestParam(value = "top", defaultValue = "99999999") long top) throws SQLException {
        threadLimit = Math.min(threadLimit, 500);
        threadLimit = Math.max(threadLimit, 0);

        final long mint = messageMapper.mint(top, problemId, threadLimit, 0);
        final List<Message> messages = messageMapper.findAllByThreadIdBetween(mint, top, problemId, null);

        long currentDepth = 0;
        long lastThreadId = 0;

        StringBuilder sb = new StringBuilder("<html><head><title>Messages</title></head><body>"
                + "<table border=0 width=100% class=table-back><tr><td><ul>");
        top = 0;
        for (Message message : messages) {
            long depth = message.getDepth();
            String title = message.getTitle();
            String userId = message.getUser();
            long messageId = message.getId();
            Timestamp timestamp = Timestamp.from(message.getInDate());
            long threadId = message.getThread();
            Long problem = message.getProblem();
            if (threadId > top) {
                top = threadId;
            }

            for (long l7 = currentDepth; l7 < depth; l7++) {
                sb.append("<ul>");
            }
            for (long l7 = depth; l7 < currentDepth; l7++) {
                sb.append("</ul>");
            }
            if ((lastThreadId != 0) && (threadId != lastThreadId) && (depth == 0)) {
                sb.append("<hr/>");
            }
            lastThreadId = threadId;
            sb.append("<li><a href=showmessage?message_id=").append(messageId).append("><font color=blue>").append(StringUtils.escapeXml(title)).append("</font></a> <b><a href=userstatus?user_id=").append(userId).append("><font color=black>").append(userId).append("</font></a></b> ").append(timestamp);
            if (problem != null && problem != 0L && depth == 0) {
                sb.append(" <b><a href=showproblem?problem_id=").append(problem).append("><font color=#000>Problem ").append(problem).append("</font></a></b>");
            }
            currentDepth = depth;
        }
        for (long l7 = 0; l7 < currentDepth; l7++) {
            sb.append("</ul>");
        }
        sb.append("</ul></td></tr></table><center>");
        String sql3 = "select thread_id from message where thread_id>=? ";
        if (problemId != null) {
            sql3 += " and problem_id=? ";
        }
        sql3 = sql3 + " order by thread_id limit " + threadLimit;
        long maxt = messageMapper.maxt(top, problemId, threadLimit, 999999999999L);
        String query = "";
        if (problemId != null) {
            query = query + "?problem_id=" + problemId;
        }
        sb.append("<hr/>[<a href=bbs").append(query).append(">Top</a>]");
        query = "?top=" + maxt;
        if (problemId != null) {
            query = query + "&problem_id=" + problemId;
        }
        sb.append("&nbsp;&nbsp;&nbsp;[<a href=bbs").append(query).append(">Previous</a>]");
        query = "?top=" + mint;
        if (problemId != null) {
            query = query + "&problem_id=" + problemId;
        }
        sb.append("&nbsp;&nbsp;&nbsp;[<a href=bbs").append(query).append(">Next</a>]<br/></center><form action=postpage>");
        if (problemId != null) {
            sb.append("<input type=hidden name=problem_id value=").append(problemId).append(">");
        }
        sb.append("<button type=submit>Post new message</button></form></body></html>");
        return ResponseEntity.ok().contentType(MediaType.valueOf("text/html;charset=UTF-8")).body(sb.toString());
    }

}
