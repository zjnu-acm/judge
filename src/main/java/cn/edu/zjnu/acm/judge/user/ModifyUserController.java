package cn.edu.zjnu.acm.judge.user;

import cn.edu.zjnu.acm.judge.domain.User;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.UserMapper;
import cn.edu.zjnu.acm.judge.util.ValueCheck;
import java.time.Instant;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

@Controller
public class ModifyUserController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Secured("ROLE_USER")
    @PostMapping("/modifyuser")
    @SuppressWarnings("AssignmentToMethodParameter")
    public String modifyuser(Model model,
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("rptPassword") String rptPassword,
            @RequestParam("email") String email,
            @RequestParam("nick") String nick,
            @RequestParam("school") String school,
            Authentication authentication) {
        if (!Objects.equals(newPassword, rptPassword)) {
            throw new MessageException("Passwords are not match", HttpStatus.BAD_REQUEST);
        }
        String userId = authentication != null ? authentication.getName() : null;
        User user = userMapper.findOne(userId);
        if (user == null) {
            throw new MessageException("The ID( " + userId + " ) is not existed", HttpStatus.CONFLICT);
        }
        String password = user.getPassword();
        if (!passwordEncoder.matches(oldPassword, password)) {
            throw new MessageException("password is not correct", HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isEmpty(nick)) {
            nick = userId;
        }
        if (StringUtils.isEmpty(newPassword)) {
            newPassword = oldPassword;
        } else {
            ValueCheck.checkPassword(newPassword);
        }
        ValueCheck.checkEmail(email);
        ValueCheck.checkNick(nick);
        user = user
                .toBuilder()
                .id(userId)
                .email(email)
                .nick(nick)
                .password(passwordEncoder.encode(newPassword))
                .school(school)
                .modifiedTime(Instant.now())
                .build();
        userMapper.update(user);
        model.addAttribute("user", user);
        return "modifyusersuccess";
    }

}
