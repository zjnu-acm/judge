package cn.edu.zjnu.acm.judge.bbs;

import cn.edu.zjnu.acm.judge.domain.Message;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.MessageMapper;
import cn.edu.zjnu.acm.judge.service.UserDetailService;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

@Controller
@Secured("ROLE_USER")
public class PostController {

    @Autowired
    private MessageMapper messageMapper;

    @RequestMapping(value = "/post", method = RequestMethod.POST)
    @Transactional
    public String post(HttpServletRequest request,
            @RequestParam(value = "problem_id", required = false) Long problemId,
            @RequestParam(value = "parent_id", required = false) Long parentId,
            @RequestParam(value = "content", defaultValue = "") String content,
            @RequestParam(value = "title", defaultValue = "") String title,
            RedirectAttributes redirectAttributes) {
        long depth = 0;
        long orderNum = 0;
        final String userId = UserDetailService.getCurrentUserId(request).orElse(null);
        if (StringUtils.isEmptyOrWhitespace(title)) {
            throw new MessageException("Title can't be blank", HttpStatus.BAD_REQUEST);
        }
        final long nextId = messageMapper.nextId();
        final Message parent = parentId != null
                ? Optional.ofNullable(messageMapper.findOne(parentId))
                .orElseThrow(() -> new MessageException("No such parent message", HttpStatus.NOT_FOUND))
                : null;
        if (parent != null) {
            orderNum = parent.getOrder();
            final long depth1 = parent.getDepth();

            List<Message> messages = messageMapper.findAllByThreadIdAndOrderNumGreaterThanOrderByOrderNum(parent.getThread(), parent.getOrder());
            for (Message m : messages) {
                depth = m.getDepth();
                if (depth <= depth1) {
                    break;
                }
                orderNum = m.getOrder();
            }
            depth = depth1 + 1;
            messageMapper.updateOrderNumByThreadIdAndOrderNumGreaterThan(parent.getThread(), orderNum);
            ++orderNum;
        }
        messageMapper.save(nextId, parentId, orderNum, problemId, depth, userId, title, content);
        if (parent != null) {
            messageMapper.updateThreadIdByThreadId(nextId, parent.getThread());
        }
        if (problemId != null) {
            redirectAttributes.addAttribute("problem_id", problemId);
        }
        return "redirect:/bbs";
    }

}
