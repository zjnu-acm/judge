package cn.edu.zjnu.acm.judge.bbs;

import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

@Controller
@Secured("ROLE_USER")
public class PostController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/post")
    @Transactional
    public String post(@RequestParam(value = "problem_id", required = false) Long problemId,
            @RequestParam(value = "parent_id", required = false) Long parentId,
            @RequestParam(value = "content", defaultValue = "") String content,
            @RequestParam(value = "title", defaultValue = "") String title,
            RedirectAttributes redirectAttributes,
            Authentication authentication) {
        final String userId = authentication != null ? authentication.getName() : null;
        if (StringUtils.isEmptyOrWhitespace(title)) {
            throw new MessageException("Title can't be blank", HttpStatus.BAD_REQUEST);
        }

        messageService.save(parentId, problemId, userId, title, content);

        if (problemId != null) {
            redirectAttributes.addAttribute("problem_id", problemId);
        }
        return "redirect:/bbs";
    }

}
