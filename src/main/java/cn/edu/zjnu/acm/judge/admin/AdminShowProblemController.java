package cn.edu.zjnu.acm.judge.admin;

import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.ProblemMapper;
import cn.edu.zjnu.acm.judge.service.UserDetailService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AdminShowProblemController {

    @Autowired
    private ProblemMapper problemMapper;

    @RequestMapping(value = "/admin/problems/{problemId}", method = {RequestMethod.GET, RequestMethod.HEAD})
    public String view(HttpServletRequest request,
            @PathVariable("problemId") long problemId) {
        UserDetailService.requireAdminLoginned(request);
        Problem problem = problemMapper.findOne(problemId);
        if (problem == null) {
            throw new MessageException("Can not find problem (ID:" + problemId + ")", HttpServletResponse.SC_NOT_FOUND);
        }
        request.setAttribute("problem", problem);
        return "admin/problems/show";
    }

}
