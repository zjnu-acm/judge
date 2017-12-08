package cn.edu.zjnu.acm.judge.controller;

import cn.edu.zjnu.acm.judge.domain.Message;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.MessageMapper;
import cn.edu.zjnu.acm.judge.service.MessageService;
import cn.edu.zjnu.acm.judge.util.JudgeUtils;
import cn.edu.zjnu.acm.judge.util.URLBuilder;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.unbescape.html.HtmlEscape;

import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

@Controller
public class BBSController {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private MessageService messageService;

    @GetMapping(value = "/bbs", produces = TEXT_HTML_VALUE)
    public String bbs(HttpServletRequest request,
            @RequestParam(value = "problem_id", required = false) Long problemId,
            @RequestParam(value = "size", defaultValue = "50") final int threadLimit,
            @RequestParam(value = "top", defaultValue = "99999999") final long top,
            Model model) {
        int limit = Math.max(Math.min(threadLimit, 500), 0);

        final long mint = messageMapper.mint(top, problemId, limit, 0);
        final List<Message> messages = messageMapper.findAllByThreadIdBetween(mint, top, problemId, null);

        long currentDepth = 0;
        long lastThreadId = 0;
        model.addAttribute("title", "Messages");

        StringBuilder sb = new StringBuilder("<table width=100% class=\"table-default table-back\">"
                + "<tr><td><ul>");
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
            sb.append("<li><a href=\"showmessage?message_id=").append(messageId)
                    .append("\"><font color=\"blue\">")
                    .append(HtmlEscape.escapeHtml4Xml(title))
                    .append("</font></a> <b><a href=\"userstatus?user_id=")
                    .append(userId).append("\"><font color=\"black\">")
                    .append(userId).append("</font></a></b> ")
                    .append(timestamp);
            if (problem != null && problem != 0L && depth == 0) {
                sb.append(" <b><a href=\"showproblem?problem_id=").append(problem)
                        .append("\"><font color=\"black\">Problem ").append(problem)
                        .append("</font></a></b>");
            }
        }
        for (; currentDepth > 0; currentDepth--) {
            sb.append("</ul>");
        }
        sb.append("</ul></td></tr></table><center>");
        URLBuilder query = URLBuilder.fromRequest(request)
                .replacePath("bbs")
                .replaceQueryParam("top");
        sb.append("<hr/>[<a href=\"").append(query).append("\">Top</a>]");
        query.replaceQueryParam("top", Long.toString(maxt));
        sb.append("&nbsp;&nbsp;&nbsp;[<a href=\"").append(query).append("\">Previous</a>]");
        query.replaceQueryParam("top", Long.toString(mint));
        sb.append("&nbsp;&nbsp;&nbsp;[<a href=\"").append(query)
                .append("\">Next</a>]<br/></center><form action=\"postpage\">");
        if (problemId != null) {
            sb.append("<input type=\"hidden\" name=\"problem_id\" value=\"").append(problemId).append("\">");
        }
        sb.append("<button type=\"submit\">Post new message</button></form>");
        model.addAttribute("content", sb.toString());
        return "legacy";
    }

    @Secured("ROLE_USER")
    @GetMapping({"/postpage", "/post"})
    public String postpage(Model model,
            @RequestParam(value = "problem_id", required = false) Long problemId) {
        model.addAttribute("problemId", problemId);
        return "bbs/postpage";
    }

    @PostMapping("/post")
    @Secured("ROLE_USER")
    @Transactional
    public String post(@RequestParam(value = "problem_id", required = false) Long problemId,
            @RequestParam(value = "parent_id", required = false) Long parentId,
            @RequestParam(value = "content", defaultValue = "") String content,
            @RequestParam(value = "title", defaultValue = "") String title,
            RedirectAttributes redirectAttributes,
            Authentication authentication) {
        final String userId = authentication != null ? authentication.getName() : null;
        if (!StringUtils.hasText(title)) {
            throw new MessageException("Title can't be blank");
        }

        messageService.save(parentId, problemId, userId, title, content);

        if (problemId != null) {
            redirectAttributes.addAttribute("problem_id", problemId);
        }
        return "redirect:/bbs";
    }

    @Secured("ROLE_USER")
    @GetMapping(value = "/showmessage", produces = TEXT_HTML_VALUE)
    public String showMessage(
            @RequestParam("message_id") long messageId,
            Model model) {
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

        model.addAttribute("title", "Detail of message");
        StringBuilder sb = new StringBuilder("<table border=\"0\" width=\"980\" class=\"table-back\">"
                + "<tr><td>" + "<center><h2><font color=\"blue\">")
                .append(HtmlEscape.escapeHtml4Xml(title))
                .append("</font></h2></center>" + "Posted by <b><a href=\"userstatus?user_id=")
                .append(uid).append("\"><font color=\"black\">")
                .append(uid).append("</font></a></b>" + "at ")
                .append(formatter.format(inDate));
        if (pid != null && pid != 0) {
            sb.append("on <b><a href=\"showproblem?problem_id=").append(pid)
                    .append("\"><font color=\"black\">Problem ").append(pid)
                    .append("</font></a></b>");
        }
        if (parentId != null && parentId != 0) {
            Message parent = messageMapper.findOne(parentId);
            if (parent != null) {
                String title1 = parent.getTitle();
                Instant inDate1 = parent.getInDate();
                sb.append("<br/>In Reply To:<a href=\"showmessage?message_id=")
                        .append(parentId).append("\"><font color=\"blue\">").append(HtmlEscape.escapeHtml4Xml(title1)).append("</font></a>" + "Posted by:<b><a href=\"userstatus?user_id=").append(parent.getUser()).append("\"><font color=\"black\">").append(parent.getUser()).append("</font></a></b>" + "at ").append(formatter.format(inDate1));
            }
        }
        sb.append("<HR noshade color=#FFF><pre>");
        sb.append(HtmlEscape.escapeHtml4Xml(message.getContent()));
        sb.append("</pre><HR noshade color=\"#FFF\"><b>Followed by:</b><br/><ul>");
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
            sb.append("<li><a href=\"showmessage?message_id=")
                    .append(id).append("\"><font color=\"blue\">")
                    .append(HtmlEscape.escapeHtml4Xml(title1))
                    .append("</font></a>" + " -- <b><a href=\"userstatus?user_id=")
                    .append(user)
                    .append("\"><font color=\"black\">")
                    .append(user)
                    .append("</font></a></b> ");
            sb.append(formatter.format(inDate1));
            dep = depth1;
        }
        for (long i = depth; i < dep; ++i) {
            sb.append("</ul>");
        }
        sb.append("</ul>" + "<HR noshade color=\"#FFF\">" + "<font color=\"blue\">Post your reply here:</font><br/>" + "<form method=\"POST\" action=\"post\">");
        if (pid != null) {
            sb.append("<input type=\"hidden\" name=\"problem_id\" value=\"").append(pid).append("\"/>");
        }
        sb.append("<input type=\"hidden\" name=\"parent_id\" value=\"").append(messageId).append("\"/>");
        sb.append("Title:<br/><input type=\"text\" name=\"title\" value=\"")
                .append(HtmlEscape.escapeHtml4Xml(!title.regionMatches(true, 0, "re:", 0, 3) ? "Reply:" + title : title))
                .append("\" size=75><br/>" + "Content:<br/><textarea rows=\"15\" name=\"content\" cols=\"75\">")
                .append(JudgeUtils.getReplyString(message.getContent()))
                .append("</textarea><br/><button type=\"submit\">reply</button></td></tr></table>");
        model.addAttribute("content", sb.toString());
        return "legacy";
    }

}
