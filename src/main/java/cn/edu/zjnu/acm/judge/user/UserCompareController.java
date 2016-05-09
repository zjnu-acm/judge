package cn.edu.zjnu.acm.judge.user;

import cn.edu.zjnu.acm.judge.domain.UserProblem;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.ProblemMapper;
import cn.edu.zjnu.acm.judge.mapper.UserMapper;
import cn.edu.zjnu.acm.judge.mapper.UserProblemMapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserCompareController {

    @Autowired
    private UserProblemMapper userProblemMapper;
    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private UserMapper userMapper;

    @RequestMapping(value = "/usercmp", method = {RequestMethod.GET, RequestMethod.HEAD})
    public void compare(HttpServletResponse response,
            @RequestParam("uid1") String userId1,
            @RequestParam("uid2") String userId2)
            throws IOException {
        checkUser(userId1);
        checkUser(userId2);
        int size = (int) problemMapper.nextId();
        byte[] bytes1 = new byte[size];
        byte[] bytes2 = new byte[size];
        fill(userId1, bytes1);
        fill(userId2, bytes2);
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print("<html><head><title>");
        out.print(userId1 + " vs " + userId2);
        out.print("</title></head><body>"
                + "<div align=center><font size=5 color=blue><a href=userstatus?user_id=" + userId1 + ">" + userId1 + "</a> vs <a href=userstatus?user_id=" + userId2 + ">" + userId2 + "</a></font></div>"
                + "<TABLE align=center cellSpacing=0 cellPadding=0 width=600 border=1 class=table-back style=\"border-collapse: collapse\" bordercolor=#FFF>"
                + "<tr valign=bottom><td><br/>"
                + "<form action=usercmp method=get>"
                + "Compare <input type=text size=10 name=uid1 value=" + userId1 + ">"
                + "and <input type=text size=10 name=uid2 value=" + userId2 + ">"
                + "<input type=submit value=GO></form>"
                + "</td></tr>");
        userId1 = "<a href=userstatus?user_id=" + userId1 + ">" + userId1 + "</a>";
        userId2 = "<a href=userstatus?user_id=" + userId2 + ">" + userId2 + "</a>";
        out.print("<tr bgcolor=#6589D1><td>Problems only " + userId1 + " accepted:</td></tr><tr><td>");
        for (int j = 0; j < size; ++j) {
            if ((bytes1[j] == 1 && bytes2[j] != 1)) {
                out.print("<a href=showproblem?problem_id=" + j + ">" + j + " </a>");
            }
        }
        out.print("</td></tr><tr bgcolor=#6589D1><td>Problems only " + userId2 + " accepted:</td></tr><tr><td>");
        for (int j = 0; j < size; ++j) {
            if (bytes1[j] != 1 && bytes2[j] == 1) {
                out.print("<a href=showproblem?problem_id=" + j + ">" + j + " </a>");
            }
        }
        out.print("</td></tr><tr bgcolor=#6589D1><td>Problems both " + userId1 + " and " + userId2 + " accepted:</td></tr><tr><td>");
        for (int j = 0; j < size; ++j) {
            if (bytes1[j] == 1 && bytes2[j] == 1) {
                out.print("<a href=showproblem?problem_id=" + j + ">" + j + " </a>");
            }
        }
        out.print("</td></tr><tr bgcolor=#6589D1><td>Problems only " + userId1 + " tried but failed:</td></tr><tr><td>");
        for (int j = 0; j < size; ++j) {
            if (bytes1[j] == 2 && bytes2[j] != 2) {
                out.print("<a href=showproblem?problem_id=" + j + ">" + j + " </a>");
            }
        }
        out.print("</td></tr><tr bgcolor=#6589D1><td>Problems only " + userId2 + " tried but failed:</td></tr><tr><td>");
        for (int j = 0; j < size; ++j) {
            if (bytes1[j] != 2 && bytes2[j] == 2) {
                out.print("<a href=showproblem?problem_id=" + j + ">" + j + " </a>");
            }
        }
        out.print("</td></tr><tr bgcolor=#6589D1><td>Problems both " + userId1 + " and " + userId2 + " tried but failed:</td></tr><tr><td>");
        for (int j = 0; j < size; ++j) {
            if (bytes1[j] == 2 && bytes2[j] == 2) {
                out.print("<a href=showproblem?problem_id=" + j + ">" + j + " </a>");
            }
        }
        out.print("</td></tr></table></body></html>");
    }

    private void checkUser(String uid) {
        if (userMapper.findOne(uid) == null) {
            throw new MessageException("No such user:" + uid);
        }
    }

    private void fill(String userId, byte[] bytes) {
        List<UserProblem> findAllByUserId = userProblemMapper.findAllByUserId(userId);
        for (UserProblem userProblem : findAllByUserId) {
            bytes[(int) userProblem.getProblem()] = (byte) (userProblem.getAccepted() != 0 ? 1 : 2);
        }
    }

}
