package cn.edu.zjnu.acm.judge.user;

import cn.edu.zjnu.acm.judge.domain.User;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.UserMapper;
import cn.edu.zjnu.acm.judge.service.UserDetailService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ModifyUserPageController {

    @Autowired
    private UserMapper userMapper;

    @RequestMapping(value = "/modifyuserpage", method = {RequestMethod.GET, RequestMethod.HEAD})
    protected String modifyuserpage(HttpServletRequest request) {
        UserDetailService.requireLoginned(request);
        String userId = UserDetailService.getCurrentUserId(request).orElse(null);
        User user = userMapper.findOne(userId);

        if (user == null) {
            throw new MessageException("user not exists.", HttpStatus.NOT_FOUND);
        }
        request.setAttribute("user", user);
        return "users/edit";
    }
}
