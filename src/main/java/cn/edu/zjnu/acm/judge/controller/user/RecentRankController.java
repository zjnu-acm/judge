package cn.edu.zjnu.acm.judge.controller.user;

import cn.edu.zjnu.acm.judge.domain.User;
import cn.edu.zjnu.acm.judge.mapper.UserMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RecentRankController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/recentrank")
    @SuppressWarnings("AssignmentToMethodParameter")
    public String recentRank(Model model,
            @RequestParam(value = "count", defaultValue = "10000") int count) {
        count = Math.max(0, Math.min(10000, count));
        List<User> recentRank = userMapper.recentrank(count);
        model.addAttribute("count", count);
        model.addAttribute("recentrank", recentRank);
        return "users/recent";
    }

}
