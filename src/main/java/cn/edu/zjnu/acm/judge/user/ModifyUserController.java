package cn.edu.zjnu.acm.judge.user;

import cn.edu.zjnu.acm.judge.domain.User;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.UserMapper;
import cn.edu.zjnu.acm.judge.service.UserDetailService;
import cn.edu.zjnu.acm.judge.util.ValueCheck;
import com.google.common.base.Strings;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ModifyUserController {
    
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @RequestMapping(value = "/modifyuser", method = RequestMethod.POST)
    public String modifyuser(HttpServletRequest request,
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("rptPassword") String rptPassword,
            @RequestParam("email") String email,
            @RequestParam("nick") String nick,
            @RequestParam("school") String school) {
        UserDetailService.requireLoginned(request);
        if (!Objects.equals(newPassword, rptPassword)) {
            throw new MessageException("Passwords are not match");
        }
        String userId = UserDetailService.getCurrentUserId(request).orElse(null);
        User user = userMapper.findOne(userId);
        if (user == null) {
            throw new MessageException("The ID( " + userId + " ) is not existed");
        }
        String password = user.getPassword();
        if (!passwordEncoder.matches(oldPassword, password)) {
            throw new MessageException("password is not correct");
        }
        if (Strings.isNullOrEmpty(nick)) {
            nick = userId;
        }
        if (Strings.isNullOrEmpty(newPassword)) {
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
                .build();
        userMapper.update(user);
        request.setAttribute("user", user);
        return "modifyusersuccess";
    }
    
}
