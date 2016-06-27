package cn.edu.zjnu.acm.judge.admin;

import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.mapper.ProblemMapper;
import cn.edu.zjnu.acm.judge.service.UserDetailService;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AdminProblemListController {

    @Autowired
    private ProblemMapper problemMapper;

    @RequestMapping(value = "/admin/problems", method = {RequestMethod.GET, RequestMethod.HEAD})
    public String problemlist(HttpServletRequest request, @PageableDefault(100) Pageable pageable, Locale locale) {
        UserDetailService.requireAdminLoginned(request);

        long total = problemMapper.count();
        Page<Problem> page = new PageImpl<>(problemMapper.findAll(pageable, locale.getLanguage()), pageable, total);

        request.setAttribute("total", total);
        request.setAttribute("size", page.getSize());
        request.setAttribute("page", page);

        return "admin/problems/list";
    }

}
