package cn.edu.zjnu.acm.judge.bbs;

import cn.edu.zjnu.acm.judge.domain.Message;
import cn.edu.zjnu.acm.judge.mapper.MessageMapper;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

@Controller
public class BBSController {

    @Autowired
    private MessageMapper messageMapper;

    @GetMapping(value = "/bbs", produces = TEXT_HTML_VALUE)
    public ResponseEntity<String> bbs(
            @RequestParam(value = "problem_id", required = false) Long problemId,
            @RequestParam(value = "size", defaultValue = "50") final int threadLimit,
            @RequestParam(value = "top", defaultValue = "99999999") final long top) {
        int limit = Math.max(Math.min(threadLimit, 500), 0);

        final long mint = messageMapper.mint(top, problemId, limit, 0);
        final List<Message> messages = messageMapper.findAllByThreadIdBetween(mint, top, problemId, null);

        long currentDepth = 0;
        long lastThreadId = 0;

        StringBuilder sb = new StringBuilder("<html><head><title>Messages</title></head><body>"
                + "<table width=100% class='table-default table-back'><tr><td><ul>");
        long maxThreadId = messages.stream().mapToLong(Message::getThread).max().orElse(0);
        final long maxt = messageMapper.maxt(maxThreadId, problemId, limit, 999999999999L);
        for (Message message : messages) {
            long depth = message.getDepth();
            String title = message.getTitle();
            String userId = message.getUser();
            long messageId = message.getId();
            Timestamp timestamp = Timestamp.from(message.getInDate());
            long threadId = message.getThread();
            Long problem = message.getProblem();

            for (; currentDepth < depth; currentDepth++) {
                sb.append("<ul>");
            }
            for (; currentDepth > depth; currentDepth--) {
                sb.append("</ul>");
            }
            if (lastThreadId != 0 && threadId != lastThreadId && depth == 0) {
                sb.append("<hr/>");
            }
            lastThreadId = threadId;
            sb.append("<li><a href=\"showmessage?message_id=").append(messageId).append("\"><font color=blue>").append(StringUtils.escapeXml(title)).append("</font></a> <b><a href=\"userstatus?user_id=").append(userId).append("\"><font color=black>").append(userId).append("</font></a></b> ").append(timestamp);
            if (problem != null && problem != 0L && depth == 0) {
                sb.append(" <b><a href=\"showproblem?problem_id=").append(problem).append("\"><font color=#000>Problem ").append(problem).append("</font></a></b>");
            }
        }
        for (; currentDepth > 0; currentDepth--) {
            sb.append("</ul>");
        }
        sb.append("</ul></td></tr></table><center>");
        String query = "?";
        if (problemId != null) {
            query = query + "problem_id=" + problemId;
        }
        sb.append("<hr/>[<a href=\"").append(query).append("\">Top</a>]");
        query = "?top=" + maxt;
        if (problemId != null) {
            query = query + "&problem_id=" + problemId;
        }
        sb.append("&nbsp;&nbsp;&nbsp;[<a href=\"").append(query).append("\">Previous</a>]");
        query = "?top=" + mint;
        if (problemId != null) {
            query = query + "&problem_id=" + problemId;
        }
        sb.append("&nbsp;&nbsp;&nbsp;[<a href=\"").append(query).append("\">Next</a>]<br/></center><form action=postpage>");
        if (problemId != null) {
            sb.append("<input type=\"hidden\" name=\"problem_id\" value=\"").append(problemId).append("\">");
        }
        sb.append("<button type=\"submit\">Post new message</button></form></body></html>");
        return ResponseEntity.ok().contentType(MediaType.valueOf("text/html;charset=UTF-8")).body(sb.toString());
    }

}
