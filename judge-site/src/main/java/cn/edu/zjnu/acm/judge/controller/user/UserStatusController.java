package cn.edu.zjnu.acm.judge.controller.user;

import cn.edu.zjnu.acm.judge.domain.User;
import cn.edu.zjnu.acm.judge.mapper.UserMapper;
import cn.edu.zjnu.acm.judge.mapper.UserProblemMapper;
import cn.edu.zjnu.acm.judge.service.AccountService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

/**
 * @author zhanhb
 */
@Slf4j
@Controller
@RequestMapping(produces = TEXT_HTML_VALUE)
@RequiredArgsConstructor
public class UserStatusController {

    private final UserMapper userMapper;
    private final UserProblemMapper userProblemMapper;
    private final AccountService accountService;

    @GetMapping("userstatus")
    @SuppressWarnings("AssignmentToMethodParameter")
    public String userStatus(Model model,
            @RequestParam(value = "size", defaultValue = "3") int display,
            @RequestParam(value = "user_id", required = false) String userId) {
        User user = accountService.findOne(userId);
        display = Math.max(Math.min(display, 9), 1);
        userId = user.getId();
        long rank = userMapper.rank(userId) + 1;
        long rankFirst = Math.max(rank - display, 1);
        List<User> neighbours = userMapper.neighbours(userId, display);
        List<Long> solvedProblems = userProblemMapper.findAllSolvedByUserId(userId);
        model.addAttribute("neighbours", neighbours);
        model.addAttribute("solvedProblems", solvedProblems);
        model.addAttribute("rankFirst", rankFirst);
        model.addAttribute("user", user);
        model.addAttribute("rank", rank);

        log.debug("rankFirst = {}", rankFirst);
        return "users/status";
    }

}
