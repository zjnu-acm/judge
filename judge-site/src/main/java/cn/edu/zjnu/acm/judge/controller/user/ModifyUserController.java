package cn.edu.zjnu.acm.judge.controller.user;

import cn.edu.zjnu.acm.judge.domain.User;
import cn.edu.zjnu.acm.judge.exception.BusinessCode;
import cn.edu.zjnu.acm.judge.exception.BusinessException;
import cn.edu.zjnu.acm.judge.service.AccountService;
import cn.edu.zjnu.acm.judge.util.ValueCheck;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
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
@Secured("ROLE_USER")
public class ModifyUserController {

    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping({"modifyuserpage", "modifyuser"})
    public String updatePage(Model model, Authentication authentication) {
        String userId = authentication != null ? authentication.getName() : null;
        User user = accountService.findOne(userId);
        model.addAttribute("user", user);
        return "users/edit";
    }

    @PostMapping("modifyuser")
    @SuppressWarnings("AssignmentToMethodParameter")
    public String update(Model model,
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("rptPassword") String rptPassword,
            @RequestParam("email") String email,
            @RequestParam("nick") String nick,
            @RequestParam("school") String school,
            Authentication authentication) {
        if (!Objects.equals(newPassword, rptPassword)) {
            throw new BusinessException(BusinessCode.REPEAT_PASSWORD_MISMATCH);
        }
        String userId = authentication.getName();
        User user = accountService.findOne(userId);
        String password = user.getPassword();
        if (!passwordEncoder.matches(oldPassword, password)) {
            throw new BusinessException(BusinessCode.INCORRECT_PASSWORD);
        }
        if (StringUtils.isEmpty(nick)) {
            nick = userId;
        }
        if (StringUtils.isEmpty(newPassword)) {
            newPassword = oldPassword;
        } else {
            ValueCheck.checkPassword(newPassword);
        }
        if (StringUtils.hasText(email)) {
            ValueCheck.checkEmail(email);
        } else {
            email = "";
        }
        ValueCheck.checkNick(nick);
        user = User.builder()
                .id(userId)
                .email(email)
                .nick(nick)
                .password(newPassword)
                .school(school)
                .build();
        accountService.updateSelective(userId, user);
        model.addAttribute("user", user);
        return "users/modifySuccess";
    }

}
