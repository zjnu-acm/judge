package cn.edu.zjnu.acm.judge.bbs;

import cn.edu.zjnu.acm.judge.domain.Message;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.MessageMapper;
import cn.edu.zjnu.acm.judge.util.JudgeUtils;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

/**
 *
 * @author zhanhb
 */
@Controller
public class ShowMessageController {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private MessageMapper messageMapper;

    @Secured("ROLE_USER")
    @GetMapping(value = "/showmessage", produces = TEXT_HTML_VALUE)
    public ResponseEntity<String> showMessage(
            @RequestParam("message_id") long messageId) {
        Message message = messageMapper.findOne(messageId);
        if (message == null) {
            throw new MessageException("No such message", HttpStatus.NOT_FOUND);
        }
        final DateTimeFormatter formatter = dtf.withZone(ZoneId.systemDefault());
        final Instant inDate = message.getInDate();
        final Long parentId = message.getParent();
        final String uid = message.getUser();
        final String title = message.getTitle();
        final Long pid = message.getProblem();
        final long depth = message.getDepth() + 1;
        final long thread = message.getThread();
        final long order = message.getOrder();

        StringBuilder sb = new StringBuilder("<html><head><title>Detail of message</title></head><body>" + "<table border=0 width=980 class=table-back><tr><td>" + "<center><h2><font color=blue>").append(StringUtils.escapeXml(title)).append("</font></h2></center>" + "Posted by <b><a href=userstatus?user_id=").append(uid).append("><font color=black>").append(uid).append("</font></a></b>" + "at ").append(formatter.format(inDate));
        if (pid != null && pid != 0) {
            sb.append("on <b><a href=showproblem?problem_id=").append(pid).append("><font color=black>Problem ").append(pid).append("</font></a></b>");
        }
        if (parentId != null && parentId != 0) {
            Message parent = messageMapper.findOne(parentId);
            if (parent != null) {
                String title1 = parent.getTitle();
                Instant inDate1 = parent.getInDate();
                sb.append("<br/>In Reply To:<a href=showmessage?message_id=").append(parentId).append("><font color=blue>").append(StringUtils.escapeXml(title1)).append("</font></a>" + "Posted by:<b><a href=userstatus?user_id=").append(parent.getUser()).append("><font color=black>").append(parent.getUser()).append("</font></a></b>" + "at ").append(formatter.format(inDate1));
            }
        }
        sb.append("<HR noshade color=#FFF><pre>");
        sb.append(StringUtils.escapeXml(message.getContent()));
        sb.append("</pre><HR noshade color='#FFF'><b>Followed by:</b><br/><ul>");
        long dep = depth;
        List<Message> messages = messageMapper.findAllByThreadIdAndOrderNumGreaterThanOrderByOrderNum(thread, order);
        for (Message m : messages) {
            String user = m.getUser();
            long id = m.getId();
            String title1 = m.getTitle();
            Instant inDate1 = m.getInDate();
            final long depth1 = m.getDepth();
            if (depth1 < depth) {
                break;
            }
            for (long i = dep; i < depth1; ++i) {
                sb.append("<ul>");
            }
            for (long i = depth1; i < dep; i++) {
                sb.append("</ul>");
            }
            sb.append("<li><a href=showmessage?message_id=").append(id).append("><font color=blue>").append(StringUtils.escapeXml(title1)).append("</font></a>" + " -- <b><a href=userstatus?user_id=").append(user).append("><font color=black>").append(user).append("</font></a></b> ");
            sb.append(formatter.format(inDate1));
            dep = depth1;
        }
        for (long i = depth; i < dep; ++i) {
            sb.append("</ul>");
        }
        sb.append("</ul>" + "<HR noshade color=#FFF>" + "<font color=blue>Post your reply here:</font><br/>" + "<form method=POST action=post>");
        if (pid != null) {
            sb.append("<input type=hidden name=problem_id value=").append(pid).append(">");
        }
        sb.append("<input type=hidden name=parent_id value=").append(messageId).append(">");
        sb.append("Title:<br/><input type=text name=title value=\"").append(StringUtils.escapeXml(!title.regionMatches(true, 0, "re:", 0, 3) ? "Reply:" + title : title)).append("\" size=75><br/>" + "Content:<br/><textarea rows=15 name=content cols=75>").append(JudgeUtils.getReplyString(message.getContent())).append("</textarea><br/><button type=Submit>reply</button></td></tr></table></body></html>");
        return ResponseEntity.ok().contentType(MediaType.valueOf("text/html;charset=UTF-8")).body(sb.toString());
    }

}
