package cn.edu.zjnu.acm.judge.admin;

import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.mapper.ProblemMapper;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Secured("ROLE_ADMIN")
public class AdminProblemListController {

    @Autowired
    private ProblemMapper problemMapper;

    @GetMapping("/admin/problems")
    public String problemList(Model model, @PageableDefault(100) Pageable pageable, Locale locale) {

        long total = problemMapper.count();
        Page<Problem> page = new PageImpl<>(problemMapper.findAll(pageable, locale.getLanguage()), pageable, total);

        model.addAttribute("total", total);
        model.addAttribute("page", page);

        return "admin/problems/list";
    }

}
