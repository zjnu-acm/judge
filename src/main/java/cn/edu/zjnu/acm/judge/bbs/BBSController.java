package cn.edu.zjnu.acm.judge.bbs;

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
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BBSController {

    @Autowired
    private DataSource dataSource;

    @RequestMapping(value = "/bbs", method = {RequestMethod.GET, RequestMethod.HEAD})
    protected void bbs(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(value = "problem_id", required = false) Long problemId,
            @RequestParam(value = "size", defaultValue = "50") long threadLimit,
            @RequestParam(value = "top", defaultValue = "99999999") long top)
            throws IOException, SQLException {
        threadLimit = Math.min(threadLimit, 500);
        threadLimit = Math.max(threadLimit, 0);
        long mint = 0;
        String sql = "select thread_id from message where thread_id<?";
        if (problemId != null) {
            sql += " and problem_id=? ";
        }
        sql = sql + " order by thread_id desc limit " + threadLimit;
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement("select min(thread_id) as mint from (" + sql + ") as temp")) {
            ps.setLong(1, top);
            if (problemId != null) {
                ps.setLong(2, problemId);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    mint = rs.getLong("mint");
                }
            }
        }
        String sql2 = "select title,depth,user_id,message_id,in_date,thread_id,problem_id from message where thread_id<? and thread_id>=? ";
        if (problemId != null) {
            sql2 += " and problem_id=? ";
        }
        sql2 += " order by thread_id desc,orderNum";
        long l5 = 0;
        long depth = 0;
        long l11 = 0;
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print("<html><head><title>Messages</title></head><body>"
                + "<table border=0 width=100% class=table-back><tr><td><ul>");
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql2)) {
            ps.setLong(1, top);
            ps.setLong(2, mint);
            if (problemId != null) {
                ps.setLong(3, problemId);
            }
            top = 0;
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    depth = rs.getLong("depth");
                    String title = rs.getString("title");
                    String userId = rs.getString("user_id");
                    long messageId = rs.getLong("message_id");
                    Timestamp timestamp = rs.getTimestamp("in_date");
                    long threadId = rs.getLong("thread_id");
                    Long problem = rs.getLong("problem_id");
                    if (rs.wasNull()) {
                        problem = null;
                    }
                    if (threadId > top) {
                        top = threadId;
                    }

                    for (long l7 = l5; l7 < depth; l7++) {
                        out.print("<ul>");
                    }
                    for (long l7 = depth; l7 < l5; l7++) {
                        out.print("</ul>");
                    }
                    if ((l11 != 0) && (threadId != l11) && (depth == 0)) {
                        out.print("<hr/>");
                    }
                    l11 = threadId;
                    out.print("<li><a href=showmessage?message_id="
                            + messageId + "><font color=blue>" + StringEscapeUtils.escapeHtml4(title) + "</font></a> <b><a href=userstatus?user_id="
                            + userId + "><font color=black>" + userId + "</font></a></b> " + timestamp);
                    if (problem != null && problem != 0L && depth == 0) {
                        out.print(" <b><a href=showproblem?problem_id=" + problem + "><font color=#000>Problem " + problem + "</font></a></b>");
                    }
                    l5 = depth;
                }
            }
        }
        for (long l7 = 0; l7 < depth; l7++) {
            out.print("</ul>");
        }
        out.print("</ul></td></tr></table><center>");
        String sql3 = "select thread_id from message where thread_id>=? ";
        if (problemId != null) {
            sql3 += " and problem_id=? ";
        }
        sql3 = sql3 + " order by thread_id limit " + threadLimit;
        long maxt;
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement("select max(thread_id) as maxt from (" + sql3 + ") temp")) {
            ps.setLong(1, top);
            if (problemId != null) {
                ps.setLong(2, problemId);
            }
            try (ResultSet rs = ps.executeQuery()) {
                maxt = 999999999999L;
                if (rs.next()) {
                    maxt = rs.getLong("maxt") + 1L;
                }
            }
        }
        String query = "";
        if (problemId != null) {
            query = query + "?problem_id=" + problemId;
        }
        out.print("<hr/>[<a href=bbs" + query + ">Top</a>]");
        query = "?top=" + maxt;
        if (problemId != null) {
            query = query + "&problem_id=" + problemId;
        }
        out.print("&nbsp;&nbsp;&nbsp;[<a href=bbs" + query + ">Previous</a>]");
        query = "?top=" + mint;
        if (problemId != null) {
            query = query + "&problem_id=" + problemId;
        }
        out.print("&nbsp;&nbsp;&nbsp;[<a href=bbs" + query + ">Next</a>]<br/></center><form action=postpage>");
        if (problemId != null) {
            out.print("<input type=hidden name=problem_id value=" + problemId + ">");
        }
        out.print("<button type=submit>Post new message</button></form></body></html>");
    }
}
