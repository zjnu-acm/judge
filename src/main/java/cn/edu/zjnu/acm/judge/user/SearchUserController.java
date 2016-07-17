package cn.edu.zjnu.acm.judge.user;

import cn.edu.zjnu.acm.judge.domain.User;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.UserMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchUserController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/searchuser")
    protected String searchuser(Model model,
            @RequestParam(value = "user_id", defaultValue = "") String keyword,
            @RequestParam(value = "position", required = false) String position) {
        if (keyword.length() < 2) {
            throw new MessageException("search key word whose length must be greater than 2", HttpStatus.BAD_REQUEST);
        }
        String like = keyword;
        if (position == null) {
            like = "%" + like + "%";
        } else if (position.equalsIgnoreCase("begin")) {
            like += "%";
        } else {
            like = "%" + like;
        }
        List<User> users = userMapper.findAllBySearch(like);
        model.addAttribute("query", keyword);
        model.addAttribute("users", users);
        return "users/search";

    }

}
