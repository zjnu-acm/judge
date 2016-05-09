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
package cn.edu.zjnu.acm.judge.user;

import cn.edu.zjnu.acm.judge.config.JudgeConfiguration;
import cn.edu.zjnu.acm.judge.domain.User;
import cn.edu.zjnu.acm.judge.mapper.UserMapper;
import cn.edu.zjnu.acm.judge.util.Utility;
import cn.edu.zjnu.acm.judge.util.ValueCheck;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Locale;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

@Controller
@Slf4j
public class ResetPasswordController {

    @Autowired
    private JavaMailSenderImpl javaMailSender;
    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JudgeConfiguration judgeConfiguration;

    @RequestMapping(value = "/resetPassword", method = {RequestMethod.GET, RequestMethod.HEAD})
    protected String doGet(HttpServletRequest request) {
        if (checkVcode(request)) {
            return "resetPassword";
        } else {
            return "invalid";
        }
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    protected void doPost(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(value = "action", required = false) String action,
            @RequestParam(value = "verify", required = false) String verify,
            @RequestParam(value = "username", required = false) String username,
            Locale locale) throws IOException {
        if ("changePassword".equalsIgnoreCase(action)) {
            changePassword(request, response);
        } else {
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
                String basePath = getBasePath(request);
                String vc = user.getVcode();
                if (vc == null) {
                    vc = Utility.getRandomString(16);
                    user = user.toBuilder().vcode(vc).build();
                    userMapper.update(user);
                }
                String url = basePath + request.getServletPath() + "?vc=" + vc + "&u=" + user.getId();
                HashMap<String, Object> map = new HashMap<>(2);
                map.put("url", url);
                map.put("ojName", judgeConfiguration.getContextPath() + " OJ");

                String content = springTemplateEngine.process("users/password", new Context(locale, map));
                String title = springTemplateEngine.process("users/passwordTitle", new Context(locale, map));

                MimeMessage mimeMessage = javaMailSender.createMimeMessage();

                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                helper.setTo(email);
                helper.setSubject(title);
                helper.setText(content, true);
                helper.setFrom(javaMailSender.getUsername());

                javaMailSender.send(mimeMessage);
            } catch (MailException | MessagingException ex) {
                log.error("", ex);
                out.print("alert('邮件发送失败，请稍后再试')");
                return;
            }
            out.print("alert('已经将邮件发送到" + user.getEmail() + "，请点击链接重设密码');");
        }
    }

    private void changePassword(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/javascript;charset=UTF-8");
        PrintWriter out = response.getWriter();
        if (!checkVcode(request)) {
            out.print("alert(\"效链接已失效，请重新获取链接\");");
            return;
        }
        String newPassword = request.getParameter("newPassword");
        ValueCheck.checkPassword(newPassword);
        User user = userMapper.findOne(request.getParameter("u"));
        userMapper.update(user.toBuilder()
                .password(passwordEncoder.encode(newPassword))
                .vcode(null)
                .build());
        out.print("alert(\"密码修改成功！\");");
        out.print("document.location='" + request.getContextPath() + "'");
    }

    private boolean checkVcode(HttpServletRequest request) {
        String uid = request.getParameter("u");
        String vcode = request.getParameter("vc");
        User user = null;
        if (uid != null) {
            user = userMapper.findOne(uid);
        }
        return user != null && user.getVcode() != null
                && user.getVcode().equals(vcode);
    }

    protected String getBasePath(HttpServletRequest request) {
        int serverPort = request.getServerPort();
        int defaultPort = request.isSecure() ? 443 : 80;
        StringBuilder sb = new StringBuilder(80);

        sb.append(request.getScheme()).append("://").append(request.getServerName());
        if (serverPort != defaultPort) {
            sb.append(":").append(serverPort);
        }
        return sb.append(request.getContextPath()).toString();
    }
}
