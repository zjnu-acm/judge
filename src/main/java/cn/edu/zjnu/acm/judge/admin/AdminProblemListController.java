package cn.edu.zjnu.acm.judge.admin;

import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.mapper.ProblemMapper;
import cn.edu.zjnu.acm.judge.service.UserDetailService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminProblemListController {

    @Autowired
    private ProblemMapper problemMapper;

    @RequestMapping(value = "/admin/problems", method = {RequestMethod.GET, RequestMethod.HEAD})
    public String problemlist(HttpServletRequest request,
            @RequestParam(value = "start", defaultValue = "0") long start,
            @RequestParam(value = "size", defaultValue = "100") int size) {
        UserDetailService.requireAdminLoginned(request);
        if (size <= 0) {
            throw new IllegalArgumentException();
        }

        long total = problemMapper.count();
        List<Problem> problems = problemMapper.findAll(start, size);

        request.setAttribute("total", total);
        request.setAttribute("size", size);
        request.setAttribute("list", problems);

        return "admin/problems/list";
    }

}
