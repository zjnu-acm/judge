/*
 * Copyright 2014 zhanhb.
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
import cn.edu.zjnu.acm.judge.mapper.UserMapper;
import cn.edu.zjnu.acm.judge.service.EmailService;
import cn.edu.zjnu.acm.judge.service.ResetPasswordService;
import cn.edu.zjnu.acm.judge.service.SystemService;
import cn.edu.zjnu.acm.judge.util.ValueCheck;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

@Controller
@Slf4j
public class ResetPasswordController {

    @Autowired
    private EmailService emailService;
    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SystemService systemService;
    @Autowired
    private ResetPasswordService resetPasswordService;

    @GetMapping(value = "/resetPassword", produces = TEXT_HTML_VALUE)
    public String doGet(
            @RequestParam(value = "u", required = false) String userId,
            @RequestParam(value = "vc", required = false) String vcode) {
        if (resetPasswordService.checkVcode(userId, vcode).isPresent()) {
            return "resetPassword";
        } else {
            return "invalid";
        }
    }

    @PostMapping("/resetPassword")
    public void doPost(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(value = "action", required = false) String action,
            @RequestParam(value = "verify", required = false) String verify,
            @RequestParam(value = "username", required = false) String username,
            Locale locale) throws IOException {
        response.setContentType("text/javascript;charset=UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        String word = null;
        if (session != null) {
            word = (String) session.getAttribute("word");
            session.removeAttribute("word");
        }
        if (word == null || !word.equalsIgnoreCase(verify)) {
            out.print("alert('验证码错误');");
            return;
        }

        User user = userMapper.findOne(username);
        if (user == null) {
            out.print("alert('用户不存在');");
            return;
        }
        String email = user.getEmail();
        if (email == null || !email.toLowerCase().matches(ValueCheck.EMAIL_PATTERN)) {
            out.print("alert('该用户未设置邮箱或邮箱格式不对，如需重置密码，请联系管理员！');");
            return;
        }
        try {
            String vc = resetPasswordService.getOrCreate(user.getId());
            String url = getPath(request, "/resetPassword.html?vc=", vc + "&u=", user.getId());
            String title = systemService.getResetPasswordTitle();
            Map<String, Object> map = ImmutableMap.of("url", url, "title", Objects.toString(title, ""));

            String content = templateEngine.process("users/password", new Context(locale, map));

            emailService.send(email, title, content);
        } catch (MailException | MessagingException ex) {
            log.error("fail to send email", ex);
            out.print("alert('邮件发送失败，请稍后再试')");
            return;
        }
        out.print("alert('已经将邮件发送到" + user.getEmail() + "，请点击链接重设密码');");
    }

    @PostMapping(value = "/resetPassword", params = "action=changePassword")
    public void changePassword(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(value = "u", required = false) String userId,
            @RequestParam(value = "vc", required = false) String vcode)
            throws IOException {
        response.setContentType("text/javascript;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Optional<User> optional = resetPasswordService.checkVcode(userId, vcode);
        if (!optional.isPresent()) {
            out.print("alert(\"效链接已失效，请重新获取链接\");");
            return;
        }
        String newPassword = request.getParameter("newPassword");
        ValueCheck.checkPassword(newPassword);
        User user = optional.get();
        userMapper.updateSelective(user.getId(), User.builder()
                .password(passwordEncoder.encode(newPassword))
                .build());
        resetPasswordService.remove(userId);
        out.print("alert(\"密码修改成功！\");");
        out.print("document.location='" + request.getContextPath() + "'");
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
