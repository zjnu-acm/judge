package cn.edu.zjnu.acm.judge.user;

import cn.edu.zjnu.acm.judge.domain.User;
import cn.edu.zjnu.acm.judge.exception.BadRequestException;
import cn.edu.zjnu.acm.judge.mapper.UserMapper;
import cn.edu.zjnu.acm.judge.util.URLBuilder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserListController {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private UserMapper userMapper;

    @RequestMapping(value = {"/userlist", "/users"}, method = {RequestMethod.GET, RequestMethod.HEAD})
    public String userlist(HttpServletRequest request,
            @RequestParam(value = "start", defaultValue = "0") long start,
            @RequestParam(value = "size", defaultValue = "50") int size,
            @RequestParam(value = "of1", defaultValue = "solved") String of1,
            @RequestParam(value = "od1", defaultValue = "desc") String od1,
            @RequestParam(value = "of2", defaultValue = "submit") String of2,
            @RequestParam(value = "od2", defaultValue = "asc") String od2)
            throws SQLException {
        switch (of1) {
            case "solved":
            case "submit":
            case "ratio":
                break;
            default:
                of1 = "solved";
        }
        if (!od1.equals("asc")) {
            od1 = "desc";
        }
        if (of1.equals("ratio")) {
            of1 = "solved/submit";
        }
        switch (of2) {
            case "solved":
            case "submit":
            case "ratio":
                break;
            default:
                of2 = "submit";
        }
        if (!od2.equals("desc") && !od2.equals("asc")) {
            od2 = "asc";
        }
        if (of2.equals("ratio")) {
            of2 = "solved/submit";
        }
        start = Math.max(0, start);
        if (size < 1) {
            size = 50;
        }
        size = Math.min(500, size);
        String query;
        try {
            query = URLBuilder.fromRequest(request)
                    .replaceQueryParam("start")
                    .toString();
        } catch (IllegalStateException | IllegalArgumentException ex) {
            throw new BadRequestException();
        }

        long totalUsers = userMapper.countByDefunctN();
        ArrayList<User> users = new ArrayList<>(size);
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE defunct = 'N' ORDER BY " + of1 + " " + od1 + "," + of2 + " " + od2 + " limit ?,?")) {
            ps.setLong(1, start);
            ps.setLong(2, size);
            try (ResultSet rs = ps.executeQuery()) {
                for (; rs.next();) {
                    String userId = rs.getString("user_id");
                    long solved = rs.getLong("solved");
                    long submit = rs.getLong("submit");
                    String nick = rs.getString("nick");
                    users.add(User.builder().id(userId).nick(nick).submit(submit).solved(solved).build());
                }
            }
        }
        request.setAttribute("url", query);
        request.setAttribute("start", start);
        request.setAttribute("users", users);
        request.setAttribute("total", totalUsers);
        request.setAttribute("size", size);
        return "users/list";
    }

}
