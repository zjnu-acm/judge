package cn.edu.zjnu.acm.judge.problem;

import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.ProblemMapper;
import cn.edu.zjnu.acm.judge.service.UserDetailService;
import com.google.common.base.Strings;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchProblemController {

    @Autowired
    private ProblemMapper problemMapper;

    @RequestMapping(value = "/searchproblem", method = {RequestMethod.GET, RequestMethod.HEAD})
    protected String searchproblem(HttpServletRequest request,
            @RequestParam(value = "sstr", required = false) String query) {
        if (Strings.isNullOrEmpty(query)) {
            throw new MessageException("Please input the keyword to the problem.", HttpServletResponse.SC_BAD_REQUEST);
        }
        String userId = UserDetailService.getCurrentUserId(request).orElse(null);
        List<Problem> problems = problemMapper.findAllBySearchTitleOrSourceAndDefunctN(query, userId);

        request.setAttribute("query", query);
        request.setAttribute("problems", problems);
        return "problems/search";
    }

}
