package cn.edu.zjnu.acm.judge.user;

import cn.edu.zjnu.acm.judge.domain.User;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchUserController {

    @Autowired
    private DataSource dataSource;

    @RequestMapping(value = "/searchuser", method = {RequestMethod.GET, RequestMethod.HEAD})
    protected String searchuser(HttpServletRequest request,
            @RequestParam(value = "length", defaultValue = "0") long length,
            @RequestParam(value = "user_id", required = false) String keyword,
            @RequestParam(value = "orderby", required = false) String orderby,
            @RequestParam(value = "position", required = false) String position)
            throws SQLException {
        if ("user_id".equals(orderby)) {
            orderby = " order by user_id";
        } else {
            orderby = " order by solved desc, submit asc";
        }
        if (StringUtils.length(keyword) < 2) {
            throw new MessageException("search key word whose length must be greater than 2");
        }
        String sql = "select user_id id,nick,solved,submit from users WHERE (user_id like ? or nick like ?) and defunct = 'N' ";
        if (length > 0) {
            sql = sql + " and length(user_id)=" + length + " ";
        }
        String like = keyword;
        if (position == null) {
            like = "%" + like + "%";
        } else if (position.equalsIgnoreCase("begin")) {
            like += "%";
        } else {
            like = "%" + like;
        }
        ArrayList<User> users = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql + orderby)) {
            ps.setString(1, like);
            ps.setString(2, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String id = rs.getString("id");
                    String nick = rs.getString("nick");
                    long solved = rs.getLong("solved");
                    long submit = rs.getLong("submit");
                    users.add(User.builder().id(id).nick(nick).solved(solved).submit(submit).build());
                }
            }
        }
        if (users.isEmpty()) {
            throw new MessageException("Sorry, the user doesn't exist.");
        }

        request.setAttribute("query", keyword);
        request.setAttribute("users", users);
        return "users/search";

    }

}
