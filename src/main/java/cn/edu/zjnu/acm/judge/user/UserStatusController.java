package cn.edu.zjnu.acm.judge.user;

import cn.edu.zjnu.acm.judge.domain.User;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.UserMapper;
import cn.edu.zjnu.acm.judge.mapper.UserProblemMapper;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

@Slf4j
@Controller
public class UserStatusController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserProblemMapper userProblemMapper;

    @RequestMapping(value = "/userstatus", method = {RequestMethod.GET, RequestMethod.HEAD})
    public String userstatus(HttpServletRequest request,
            @RequestParam(value = "size", defaultValue = "3") int display,
            @RequestParam(value = "user_id", required = false) String userId) {
        if (StringUtils.isEmptyOrWhitespace(userId)) {
            throw new MessageException("Error -- no user found", HttpStatus.BAD_REQUEST);
        }
        User user = userMapper.findOne(userId);
        if (user == null) {
            throw new MessageException("Sorry, '" + userId + "' doesn't exist", HttpStatus.NOT_FOUND);
        }
        display = Math.max(Math.min(display, 9), 1);
        userId = user.getId();
        long rank = userMapper.rank(userId) + 1;
        long rankFirst = Math.max(rank - display, 1);
        List<User> neighbours = userMapper.neighbours(userId, display);
        List<Long> solvedProblems = userProblemMapper.findAllByUserIdAndAcceptedNot0(userId);
        request.setAttribute("neighbours", neighbours);
        request.setAttribute("solvedProblems", solvedProblems);
        request.setAttribute("rankFirst", rankFirst);
        request.setAttribute("user", user);
        request.setAttribute("rank", rank);

        log.debug("rankFirst = {}", rankFirst);
        return "users/status";
    }
}
