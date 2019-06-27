/*
 * Copyright 2014 ZJNU ACM.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.edu.zjnu.acm.judge.controller.user;

import cn.edu.zjnu.acm.judge.domain.User;
import cn.edu.zjnu.acm.judge.exception.BusinessCode;
import cn.edu.zjnu.acm.judge.exception.BusinessException;
import cn.edu.zjnu.acm.judge.mapper.UserMapper;
import cn.edu.zjnu.acm.judge.service.EmailService;
import cn.edu.zjnu.acm.judge.service.ResetPasswordService;
import cn.edu.zjnu.acm.judge.service.SystemService;
import cn.edu.zjnu.acm.judge.util.ValueCheck;
import com.google.common.collect.ImmutableMap;
import java.time.Duration;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nullable;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

/**
 * @author zhanhb
 */
@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("resetPassword")
public class ResetPasswordController {

    private final EmailService emailService;
    private final TemplateEngine templateEngine;

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final SystemService systemService;
    private final ResetPasswordService resetPasswordService;

    @GetMapping(produces = TEXT_HTML_VALUE)
    public String doGet(
            @RequestParam(value = "u", required = false) @Nullable String userId,
            @RequestParam(value = "vc", required = false) @Nullable String vcode) {
        if (resetPasswordService.checkVcode(userId, vcode).isPresent()) {
            return "users/resetPassword";
        } else {
            return "users/resetPasswordInvalid";
        }
    }

    @PostMapping(produces = APPLICATION_JSON_VALUE)
    public String doPost(HttpServletRequest request,
            @RequestParam(value = "verify", required = false) @Nullable String verify,
            @RequestParam(value = "username", required = false) @Nullable String username,
            @Nullable Locale locale) {
        HttpSession session = request.getSession(false);
        String word = null;
        if (session != null) {
            word = (String) session.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
            session.removeAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        }
        if (word == null || !word.equalsIgnoreCase(verify)) {
            throw new BusinessException(BusinessCode.VERIFY_CODE_INCORRECT);
        }

        User user = userMapper.findOne(username);
        if (user == null) {
            throw new BusinessException(BusinessCode.USER_NOT_FOUND, username);
        }
        String email = user.getEmail();
        if (email == null || !email.toLowerCase().matches(ValueCheck.EMAIL_PATTERN)) {
            throw new BusinessException(BusinessCode.NO_EMAIL_PRESENT);
        }
        Integer timeout = resetPasswordService.addEmailCache(email);
        if (timeout != null) {
            throw new BusinessException(BusinessCode.RESET_PASSWORD_FREQUENCY, timeout);
        }
        try {
            String vc = resetPasswordService.getOrCreate(user.getId());
            String nick = user.getNick();
            String url = getPath(request, "/resetPassword.html?vc=", vc + "&u=", user.getId());
            String title = systemService.getResetPasswordTitle();
            Map<String, Object> map = ImmutableMap.of(
                    "url", url,
                    "title", Objects.toString(title, ""),
                    "nick", nick,
                    "expireHour", resetPasswordService.getExpireHour()
            );

            String content = templateEngine.process("users/password", new Context(locale, map));

            emailService.send(email, title, content);
        } catch (MessagingException | RuntimeException | Error ex) {
            log.error("fail to send email", ex);
            resetPasswordService.removeEmailCache(email);
            throw new BusinessException(BusinessCode.FAIL_TO_SEND_EMAIL);
        }
        throw new BusinessException(BusinessCode.EMAIL_SEND_SUCCEED, user.getEmail());
    }

    @PostMapping(params = "action=changePassword",
            produces = APPLICATION_JSON_VALUE)
    public String changePassword(HttpServletRequest request,
            @RequestParam(value = "u", required = false) @Nullable String userId,
            @RequestParam(value = "vc", required = false) @Nullable String vcode) {
        Optional<User> optional = resetPasswordService.checkVcode(userId, vcode);
        if (!optional.isPresent()) {
            throw new BusinessException(BusinessCode.LINK_EXPIRED);
        }
        String newPassword = request.getParameter("newPassword");
        ValueCheck.checkPassword(newPassword);
        User user = optional.get();
        userMapper.updateSelective(user.getId(), User.builder()
                .password(passwordEncoder.encode(newPassword))
                .build());
        resetPasswordService.remove(userId);
        throw new BusinessException(BusinessCode.MODIFIY_PASSWORD_SUCCEED);
    }

    private String getPath(HttpServletRequest request, String... params) {
        int serverPort = request.getServerPort();
        int defaultPort = request.isSecure() ? 443 : 80;
        StringBuilder sb = new StringBuilder(80);

        sb.append(request.getScheme()).append("://").append(request.getServerName());
        if (serverPort != defaultPort) {
            sb.append(":").append(serverPort);
        }
        sb.append(request.getContextPath());
        for (String param : params) {
            sb.append(param);
        }
        return sb.toString();
    }

}
