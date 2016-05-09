package cn.edu.zjnu.acm.judge.bbs;

import cn.edu.zjnu.acm.judge.domain.Message;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.MessageMapper;
import cn.edu.zjnu.acm.judge.service.UserDetailService;
import cn.edu.zjnu.acm.judge.util.JudgeUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author zhanhb
 */
@Controller
public class ShowMessageController {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private MessageMapper messageMapper;

    @RequestMapping(value = "/showmessage", method = {RequestMethod.GET, RequestMethod.HEAD})
    public void showmessage(HttpServletRequest request, HttpServletResponse response,
            @RequestParam("message_id") long msgid)
            throws IOException {
        UserDetailService.requireLoginned(request);
        Message message = messageMapper.findOne(msgid);
        if (message == null) {
            throw new MessageException("No such message");
        }
        final DateTimeFormatter formatter = dtf.withZone(ZoneId.systemDefault());
        final Instant inDate = message.getInDate();
        final Long parentId = message.getParent();
        final String uid = message.getUser();
        final String content = message.getContent();
        String title = message.getTitle();
        final Long pid = message.getProblem();
        long depth = message.getDepth() + 1;
        final long thread = message.getThread();
        final long order = message.getOrder();
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print("<html><head><title>Detail of message</title></head><body>"
                + "<table border=0 width=980 class=table-back><tr><td>"
                + "<center><h2><font color=blue>" + StringEscapeUtils.escapeHtml4(title) + "</font></h2></center>"
                + "Posted by <b><a href=userstatus?user_id=" + uid + "><font color=black>" + uid + "</font></a></b>"
                + "at " + formatter.format(inDate));
        if (pid != null && pid != 0) {
            out.print("on <b><a href=showproblem?problem_id=" + pid + "><font color=black>Problem " + pid + "</font></a></b>");
        }
        if (parentId != null && parentId != 0) {
            Message parent = messageMapper.findOne(parentId);
            if (parent != null) {
                String title1 = parent.getTitle();
                Instant inDate1 = parent.getInDate();
                out.print("<br/>In Reply To:<a href=showmessage?message_id=" + parentId + "><font color=blue>" + StringEscapeUtils.escapeHtml4(title1) + "</font></a>"
                        + "Posted by:<b><a href=userstatus?user_id=" + parent.getUser() + "><font color=black>" + parent.getUser() + "</font></a></b>"
                        + "at " + formatter.format(inDate1));
            }
        }
        out.print("<HR noshade color=#FFF><pre>");
        out.print(StringEscapeUtils.escapeHtml4(content));
        out.print("</pre><HR noshade color='#FFF'><b>Followed by:</b><br/><ul>");
        final long l8 = depth;
        List<Message> messages = messageMapper.findAllByThreadIdAndOrderNumGreaterThanOrderByOrderNum(thread, order);
        for (Message m : messages) {
            String user = m.getUser();
            long id = m.getId();
            String title1 = m.getTitle();
            Instant inDate1 = m.getInDate();
            final long depth1 = m.getDepth();
            if (depth1 < l8) {
                break;
            }
            for (long i = depth; i < depth1; ++i) {
                out.print("<ul>");
            }
            for (long i = depth1; i < depth; i++) {
                out.print("</ul>");
            }
            out.print("<li><a href=showmessage?message_id=" + id + "><font color=blue>" + StringEscapeUtils.escapeHtml4(title1) + "</font></a>"
                    + " -- <b><a href=userstatus?user_id=" + user + "><font color=black>" + user + "</font></a></b> ");
            out.print(formatter.format(inDate1));
            depth = depth1;
        }
        for (long i = l8; i < depth; ++i) {
            out.print("</ul>");
        }
        out.print("</ul>"
                + "<HR noshade color=#FFF>"
                + "<font color=blue>Post your reply here:</font><br/>"
                + "<form method=POST action=post>"
                + "<input type=hidden name=problem_id value=" + pid + (">"
                + "<input type=hidden name=parent_id value=") + msgid + ">");
        if (!title.regionMatches(true, 0, "re:", 0, 3)) {
            title = "Reply:" + title;
        }
        out.print("Title:<br/><input type=text name=title value=\"" + StringEscapeUtils.escapeHtml4(title) + "\" size=75><br/>"
                + "Content:<br/><textarea rows=15 name=content cols=75>"
                + JudgeUtils.getReplyString(content)
                + "</textarea><br/><input type=Submit value=reply></td></tr></table></body></html>");
    }

}
