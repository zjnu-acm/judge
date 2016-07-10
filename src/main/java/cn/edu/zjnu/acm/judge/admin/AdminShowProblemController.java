package cn.edu.zjnu.acm.judge.admin;

import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.ProblemMapper;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Secured("ROLE_ADMIN")
public class AdminShowProblemController {

    @Autowired
    private ProblemMapper problemMapper;

    // TODO
    @RequestMapping(value = "/admin/problems/{problemId}", method = {RequestMethod.GET, RequestMethod.HEAD})
    public String view(HttpServletRequest request,
            @PathVariable("problemId") long problemId,
            Locale locale) {
        Problem problem = problemMapper.findOne(problemId, locale.getLanguage());
        if (problem == null) {
            throw new MessageException("Can not find problem (ID:" + problemId + ")", HttpStatus.NOT_FOUND);
        }
        request.setAttribute("problem", problem);
        return "admin/problems/show";
    }

}
