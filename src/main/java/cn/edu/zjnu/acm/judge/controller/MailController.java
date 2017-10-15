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
import cn.edu.zjnu.acm.judge.service.AccountService;
import cn.edu.zjnu.acm.judge.service.UserDetailsServiceImpl;
import cn.edu.zjnu.acm.judge.util.JudgeUtils;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author zhanhb
 */
@Controller
@Secured("ROLE_USER")
public class MailController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private MailMapper mailMapper;

    @GetMapping("/deletemail")
    public String delete(@RequestParam("mail_id") long mailId, Authentication authentication) {
        Mail mail = mailMapper.findOne(mailId);
        if (mail == null) {
            throw new MessageException("No such mail", HttpStatus.NOT_FOUND);
        }
        if (!UserDetailsServiceImpl.isUser(authentication, mail.getTo())) {
            throw new MessageException("Sorry, invalid access", HttpStatus.FORBIDDEN);
        }
        mailMapper.delete(mailId);
        return "redirect:/mail";
    }

    @GetMapping("/mail")
    @SuppressWarnings("AssignmentToMethodParameter")
    public String mail(Model model,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "start", defaultValue = "1") long start,
            Authentication authentication) {
        if (start <= 0) {
            start = 1;
        }
        String currentUserId = authentication != null ? authentication.getName() : null;

        List<Mail> mails = mailMapper.findAllByTo(currentUserId, start - 1, size);

        model.addAttribute("userId", currentUserId);
        model.addAttribute("mails", mails);
        model.addAttribute("size", size);
        model.addAttribute("start", start);
        return "mails/list";
    }

    @PostMapping("/send")
    @SuppressWarnings("AssignmentToMethodParameter")
    public String send(@RequestParam("title") String title,
            @RequestParam("to") String to,
            @RequestParam("content") String content,
            Authentication authentication) {
        String userId = authentication != null ? authentication.getName() : null;
        if (!StringUtils.hasText(title)) {
            title = "No Topic";
        }
        if (content.length() > 40000) {
            throw new MessageException("Sorry, content too long", HttpStatus.PAYLOAD_TOO_LARGE);
        }
        accountService.findOne(to);

        mailMapper.save(Mail.builder()
                .from(userId)
                .to(to)
                .title(title)
                .content(content)
                .build());
        return "mails/sendsuccess";
    }

    @GetMapping({"/sendpage", "/send"})
    @SuppressWarnings("AssignmentToMethodParameter")
    public String sendPage(Model model,
            @RequestParam(value = "reply", defaultValue = "-1") long reply,
            @RequestParam(value = "to", defaultValue = "") String userId,
            Authentication authentication) {
        String title = "";
        String content = "";

        if (reply != -1) {
            Mail parent = mailMapper.findOne(reply);
            if (parent == null) {
                throw new MessageException("No such mail", HttpStatus.NOT_FOUND);
            }
            String toUser = parent.getTo();
            if (!UserDetailsServiceImpl.isUser(authentication, toUser)) {
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
        model.addAttribute("to", userId);
        model.addAttribute("title", title);
        model.addAttribute("content", JudgeUtils.getReplyString(content));
        return "mails/sendpage";
    }

    @GetMapping("/showmail")
    public String showMail(Model model,
            @RequestParam("mail_id") long mailId,
            Authentication authentication) {
        Mail mail = mailMapper.findOne(mailId);
        if (mail == null) {
            throw new MessageException("No such mail", HttpStatus.NOT_FOUND);
        }
        if (!UserDetailsServiceImpl.isUser(authentication, mail.getTo())) {
            throw new MessageException("Sorry, invalid access", HttpStatus.FORBIDDEN);
        }
        mailMapper.readed(mailId);
        model.addAttribute("mail", mail);
        return "mails/view";
    }

}
