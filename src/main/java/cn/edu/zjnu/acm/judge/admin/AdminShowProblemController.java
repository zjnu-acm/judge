package cn.edu.zjnu.acm.judge.admin;

import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.ProblemMapper;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@Secured("ROLE_ADMIN")
public class AdminShowProblemController {

    @Autowired
    private ProblemMapper problemMapper;

    // TODO for admin only
    @GetMapping("/admin/problems/{problemId}")
    public String view(Model model,
            @PathVariable("problemId") long problemId,
            Locale locale) {
        Problem problem = problemMapper.findOne(problemId, locale.getLanguage());
        if (problem == null) {
            throw new MessageException("Can not find problem (ID:" + problemId + ")", HttpStatus.NOT_FOUND);
        }
        model.addAttribute("problem", problem);
        return "admin/problems/show";
    }

}
