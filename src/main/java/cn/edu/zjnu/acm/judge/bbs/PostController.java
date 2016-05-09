package cn.edu.zjnu.acm.judge.bbs;

import cn.edu.zjnu.acm.judge.domain.Message;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.MessageMapper;
import cn.edu.zjnu.acm.judge.service.UserDetailService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PostController {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private MessageMapper messageMapper;

    @RequestMapping(value = "/post", method = RequestMethod.POST)
    @Transactional
    public String post(HttpServletRequest request,
            @RequestParam(value = "problem_id", required = false) Long problemId,
            @RequestParam(value = "parent_id", defaultValue = "0") long parentId,
            @RequestParam(value = "content", defaultValue = "") String content,
            @RequestParam(value = "title", defaultValue = "") String title,
            RedirectAttributes redirectAttributes)
            throws SQLException {
        UserDetailService.requireAdminLoginned(request);
        long depth = 0;
        long orderNum = 0;
        final String userId = UserDetailService.getCurrentUserId(request).orElse(null);
        if (StringUtils.isBlank(title)) {
            throw new MessageException("Title can't be blank");
        }
        final long nextId = messageMapper.nextId();
        if (parentId != 0) {
            Message message = messageMapper.findOne(parentId);

            if (message == null) {
                throw new MessageException("No such parent message");
            }
            orderNum = message.getOrder();
            final long depth1 = message.getDepth();
            long parentThreadId = message.getThread();

            List<Message> messages = messageMapper.findAllByThreadIdAndOrderNumGreaterThanOrderByOrderNum(parentThreadId, orderNum);
            for (Message m : messages) {
                depth = m.getDepth();
                if (depth <= depth1) {
                    break;
                }
                orderNum = m.getOrder();
            }
            depth = depth1 + 1;
            try (Connection conn = dataSource.getConnection();
                    PreparedStatement ps = conn.prepareStatement("update message set orderNum=orderNum+1 where thread_id=? and orderNum>?")) {
                ps.setLong(1, parentThreadId);
                ps.setLong(2, orderNum);
                ps.executeUpdate();
            }
            try (Connection conn = dataSource.getConnection();
                    PreparedStatement ps = conn.prepareStatement("update message set thread_id=? where thread_id=?")) {
                ps.setLong(1, nextId);
                ps.setLong(2, parentThreadId);
                ps.executeUpdate();
            }
            ++orderNum;
        }
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement("insert into message (thread_id,message_id,parent_id,orderNum,problem_id,depth,user_id,title,content,in_date) values(?,?,?,?,?,?,?,?,?,now())")) {
            ps.setLong(1, nextId);
            ps.setLong(2, nextId);
            ps.setLong(3, parentId);
            ps.setLong(4, orderNum);
            ps.setLong(5, problemId != null ? problemId : 0);
            ps.setLong(6, depth);
            ps.setString(7, userId);
            ps.setString(8, title);
            ps.setString(9, content);
            ps.executeUpdate();
        }
        if (problemId != null) {
            redirectAttributes.addAttribute("problem_id", problemId);
        }
        return "redirect:/bbs";
    }
}
