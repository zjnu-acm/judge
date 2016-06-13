/*
 * Copyright 2016 ZJNU ACM.
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
package cn.edu.zjnu.acm.judge.controller;

import cn.edu.zjnu.acm.judge.domain.Mail;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.MailMapper;
import cn.edu.zjnu.acm.judge.mapper.UserMapper;
import cn.edu.zjnu.acm.judge.service.UserDetailService;
import cn.edu.zjnu.acm.judge.util.JudgeUtils;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

/**
 *
 * @author zhanhb
 */
@Controller
public class MailController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MailMapper mailMapper;

    @RequestMapping(value = "/deletemail", method = {RequestMethod.GET, RequestMethod.HEAD})
    public String delete(HttpServletRequest request,
            @RequestParam("mail_id") long mailId) {
        UserDetailService.requireLoginned(request);
        Mail mail = mailMapper.findOne(mailId);
        if (mail == null) {
            throw new MessageException("No such mail", HttpStatus.NOT_FOUND);
        }
        if (!UserDetailService.isUser(request, mail.getTo())) {
            throw new MessageException("Sorry, invalid access", HttpStatus.FORBIDDEN);
        }
        mailMapper.delete(mailId);
        return "redirect:/mail";
    }

    @RequestMapping(value = "/mail", method = {RequestMethod.GET, RequestMethod.HEAD})
    public String mail(HttpServletRequest request,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "start", defaultValue = "1") long start) {
        UserDetailService.requireLoginned(request);
        if (start <= 0) {
            start = 1;
        }
        String currentUserId = UserDetailService.getCurrentUserId(request).orElse(null);

        List<Mail> mails = mailMapper.findAllByTo(currentUserId, start - 1, size);

        request.setAttribute("userId", currentUserId);
        request.setAttribute("mails", mails);
        request.setAttribute("size", size);
        request.setAttribute("start", start);
        return "mails/list";
    }

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public String send(HttpServletRequest request,
            @RequestParam("title") String title,
            @RequestParam("to") String to,
            @RequestParam("content") String content) {
        UserDetailService.requireLoginned(request);
        String userId = UserDetailService.getCurrentUserId(request).orElse(null);
        if (StringUtils.isEmptyOrWhitespace(title)) {
            title = "No Topic";
        }
        if (content.length() > 40000) {
            throw new MessageException("Sorry, content too long", HttpStatus.PAYLOAD_TOO_LARGE);
        }
        if (userMapper.findOne(to) == null) {
            throw new MessageException("Sorry, no such user:" + to, HttpStatus.NOT_FOUND);
        }

        mailMapper.save(Mail.builder()
                .from(userId)
                .to(to)
                .title(title)
                .content(content)
                .build());
        return "mails/sendsuccess";
    }

    @RequestMapping(value = {"/sendpage", "/send"}, method = {RequestMethod.GET, RequestMethod.HEAD})
    public String sendpage(HttpServletRequest request,
            @RequestParam(value = "reply", defaultValue = "-1") long reply,
            @RequestParam(value = "to", defaultValue = "") String userId) {
        UserDetailService.requireLoginned(request);
        String title = "";
        String content = "";

        if (reply != -1) {
            Mail parent = mailMapper.findOne(reply);
            if (parent == null) {
                throw new MessageException("No such mail", HttpStatus.NOT_FOUND);
            }
            String toUser = parent.getTo();
            if (!UserDetailService.isUser(request, toUser)) {
                throw new MessageException("invalid access", HttpStatus.FORBIDDEN);
            }
            userId = parent.getFrom();
            title = parent.getTitle();
            content = parent.getContent();
            if (!title.regionMatches(true, 0, "re:", 0, 3)) {
                title = "Re:" + title;
            }
            mailMapper.setReply(reply);
        }
        request.setAttribute("to", userId);
        request.setAttribute("title", title);
        request.setAttribute("content", JudgeUtils.getReplyString(content));
        return "mails/sendpage";
    }

    @RequestMapping(value = "/showmail", method = {RequestMethod.GET, RequestMethod.HEAD})
    public String showmail(HttpServletRequest request, @RequestParam("mail_id") long mailId) {
        UserDetailService.requireLoginned(request);
        Mail mail = mailMapper.findOne(mailId);
        if (mail == null) {
            throw new MessageException("No such mail", HttpStatus.NOT_FOUND);
        }
        if (!UserDetailService.isUser(request, mail.getTo())) {
            throw new MessageException("Sorry, invalid access", HttpStatus.FORBIDDEN);
        }
        mailMapper.readed(mailId);
        request.setAttribute("mail", mail);
        return "mails/view";
    }

}
