package cn.edu.zjnu.acm.judge.user;

import cn.edu.zjnu.acm.judge.domain.User;
import cn.edu.zjnu.acm.judge.mapper.UserMapper;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RecentRankController {

    @Autowired
    private UserMapper userMapper;

    @RequestMapping(value = "/recentrank", method = {RequestMethod.GET, RequestMethod.HEAD})
    protected String recentrank(HttpServletRequest request,
            @RequestParam(value = "count", defaultValue = "10000") int count) {
        count = Math.max(0, Math.min(10000, count));
        List<User> recentrank = userMapper.recentrank(count);
        request.setAttribute("count", count);
        request.setAttribute("recentrank", recentrank);
        return "users/recent";
    }

}
