package cn.edu.zjnu.acm.judge.controller.user;

import cn.edu.zjnu.acm.judge.domain.User;
import cn.edu.zjnu.acm.judge.exception.BusinessCode;
import cn.edu.zjnu.acm.judge.exception.BusinessException;
import cn.edu.zjnu.acm.judge.mapper.UserMapper;
import cn.edu.zjnu.acm.judge.service.ContestOnlyService;
import cn.edu.zjnu.acm.judge.util.ValueCheck;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

/**
 * @author zhanhb
 */
@Controller
@RequestMapping(produces = TEXT_HTML_VALUE)
@RequiredArgsConstructor
@Slf4j
public class RegisterController {

    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final ContestOnlyService contestOnlyService;

    @PostMapping("register")
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
            throw new BusinessException(BusinessCode.REPEAT_PASSWORD_MISMATCH);
        }
        if (StringUtils.hasText(nick)) {
            nick = nick.trim();
        } else {
            nick = userId;
        }
        ValueCheck.checkNick(nick);
        if (userMapper.findOne(userId) != null) {
            throw new BusinessException(BusinessCode.IMPORT_USER_EXISTS, userId);
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
        return "users/registerSuccess";
    }

}
