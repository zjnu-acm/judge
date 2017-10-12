package cn.edu.zjnu.acm.judge.controller.user;

import cn.edu.zjnu.acm.judge.domain.User;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.UserMapper;
import cn.edu.zjnu.acm.judge.service.ContestOnlyService;
import cn.edu.zjnu.acm.judge.util.ValueCheck;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class RegisterController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ContestOnlyService contestOnlyService;

    @PostMapping("/register")
    @SuppressWarnings("AssignmentToMethodParameter")
    public String register(
            HttpServletRequest request,
            @RequestParam("user_id") String userId,
            @RequestParam("school") String school,
            @RequestParam("nick") String nick,
            @RequestParam("password") String password,
            @RequestParam("email") String email,
            @RequestParam("rptPassword") String rptPassword) {
        contestOnlyService.checkRegister();
        ValueCheck.checkUserId(userId);
        ValueCheck.checkPassword(password);
        ValueCheck.checkEmail(email);
        if (!Objects.equals(password, rptPassword)) {
            throw new MessageException("Passwords are not match");
        }
        if (StringUtils.hasText(nick)) {
            nick = nick.trim();
        } else {
            nick = userId;
        }
        ValueCheck.checkNick(nick);
        if (userMapper.findOne(userId) != null) {
            throw new MessageException("The ID( " + userId + ") existed");
        }
        User user = User.builder().id(userId)
                .password(passwordEncoder.encode(password))
                .email(StringUtils.isEmpty(email) ? null : email)
                .nick(nick)
                .school(school)
                .ip(request.getRemoteAddr())
                .build();
        userMapper.save(user);
        log.info("{}", user);
        request.setAttribute("user", user);
        return "register";
    }

}
