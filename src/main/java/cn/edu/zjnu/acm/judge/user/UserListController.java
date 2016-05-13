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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Slf4j
public class UserListController {

    private static final Sort DEFAULT_SORT = new Sort(new Sort.Order(Sort.Direction.DESC, "solved"), new Sort.Order(Sort.Direction.ASC, "submit"));

    @Autowired
    private DataSource dataSource;
    @Autowired
    private UserMapper userMapper;

    @RequestMapping(value = {"/userlist", "/users"}, method = {RequestMethod.GET, RequestMethod.HEAD})
    public String userlist(HttpServletRequest request,
            @PageableDefault(50) Pageable pageable) throws SQLException {
        int start = pageable.getOffset();
        int size = pageable.getPageSize();
        Sort sort = pageable.getSort();
        if (sort == null || !sort.iterator().hasNext()) {
            sort = DEFAULT_SORT;
        }

        StringBuilder sb = new StringBuilder(40);

        for (Sort.Order order : sort) {
            if (sb.length() != 0) {
                sb.append(",");
            }
            String property = order.getProperty();
            switch (property) {
                case "user_id":
                case "nick":
                case "solved":
                case "submit":
                    break;
                case "ratio":
                    property = "solved/submit";
                    break;
                default:
                    property = "solved";
                    break;
            }
            sb.append(property);
            if (!order.isAscending()) {
                sb.append(" desc");
            }
        }

        start = Math.max(0, start);
        if (size < 1) {
            size = 50;
        }
        size = Math.min(500, size);
        String query;
        try {
            query = URLBuilder.fromRequest(request)
                    .replaceQueryParam("page")
                    .toString();
        } catch (IllegalStateException | IllegalArgumentException ex) {
            throw new BadRequestException();
        }

        long totalUsers = userMapper.countByDefunctN();
        ArrayList<User> users = new ArrayList<>(size);
        String sql = "SELECT * FROM users WHERE defunct = 'N' ORDER BY " + sb + " limit ?,?";
        log.debug(sql);
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
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
