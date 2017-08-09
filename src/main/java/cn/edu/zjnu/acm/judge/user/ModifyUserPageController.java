package cn.edu.zjnu.acm.judge.user;

import cn.edu.zjnu.acm.judge.domain.User;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ModifyUserPageController {

    @Autowired
    private UserMapper userMapper;

    @Secured("ROLE_USER")
    @GetMapping("/modifyuserpage")
    public String modifyUserPage(Model model,
            Authentication authentication) {
        String userId = authentication != null ? authentication.getName() : null;
        User user = userMapper.findOne(userId);

        if (user == null) {
            throw new MessageException("user not exists.", HttpStatus.NOT_FOUND);
        }
        model.addAttribute("user", user);
        return "users/edit";
    }
}
