package cn.edu.zjnu.acm.judge.user;

import cn.edu.zjnu.acm.judge.domain.User;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.UserMapper;
import cn.edu.zjnu.acm.judge.util.ValueCheck;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

@Controller
@Slf4j
public class RegisterController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserMapper userMapper;

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
        ValueCheck.checkUserId(userId);
        ValueCheck.checkPassword(password);
        ValueCheck.checkEmail(email);
        if (!Objects.equals(password, rptPassword)) {
            throw new MessageException("Passwords are not match", HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isEmpty(nick)) {
            nick = userId;
        } else {
            nick = nick.trim();
        }
        ValueCheck.checkNick(nick);
        if (userMapper.findOne(userId) != null) {
            throw new MessageException("The ID( " + userId + ") existed", HttpStatus.NOT_FOUND);
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
