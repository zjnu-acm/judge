package cn.edu.zjnu.acm.judge.admin;

import cn.edu.zjnu.acm.judge.config.JudgeConfiguration;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.mapper.ProblemMapper;
import cn.edu.zjnu.acm.judge.service.ContestService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Secured("ROLE_ADMIN")
public class AddProblemController {

    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private ContestService contestService;
    @Autowired
    private JudgeConfiguration judgeConfiguration;

    @RequestMapping(value = "/admin/problems", method = RequestMethod.POST)
    public String addProblem(HttpServletRequest request, Problem problem) {
        problemMapper.save(problem);
        long id = problem.getId();
        Path problemDir = judgeConfiguration.getDataDirectory(id);
        Long contest = problem.getContest();
        if (contest != null) {
            contestService.addProblem(contest, id);
        }
        try {
            Files.createDirectories(problemDir);
        } catch (IOException ex) {
        }
        request.setAttribute("id", id);
        request.setAttribute("dir", problemDir);

        return "admin/problems/add";
    }

}
