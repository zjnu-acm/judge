package cn.edu.zjnu.acm.judge.user;

import cn.edu.zjnu.acm.judge.domain.User;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.UserMapper;
import cn.edu.zjnu.acm.judge.util.ValueCheck;
import com.google.common.base.Strings;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class RegisterController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserMapper userMapper;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    protected String register(
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
            throw new MessageException("Passwords are not match", HttpServletResponse.SC_BAD_REQUEST);
        }
        if (Strings.isNullOrEmpty(nick)) {
            nick = userId;
        } else {
            nick = nick.trim();
        }
        ValueCheck.checkNick(nick);
        if (userMapper.findOne(userId) != null) {
            throw new MessageException("The ID( " + userId + ") existed", HttpServletResponse.SC_NOT_FOUND);
        }
        User user = User.builder().id(userId)
                .password(passwordEncoder.encode(password))
                .email(email)
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
