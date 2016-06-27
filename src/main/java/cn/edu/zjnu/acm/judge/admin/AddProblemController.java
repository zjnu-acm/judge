package cn.edu.zjnu.acm.judge.admin;

import cn.edu.zjnu.acm.judge.config.JudgeConfiguration;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.mapper.ContestMapper;
import cn.edu.zjnu.acm.judge.mapper.ProblemMapper;
import cn.edu.zjnu.acm.judge.service.ContestService;
import cn.edu.zjnu.acm.judge.service.UserDetailService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AddProblemController {

    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private ContestService contestService;
    @Autowired
    private JudgeConfiguration judgeConfiguration;

    @RequestMapping(value = "/admin/problems", method = RequestMethod.POST)
    public String addProblem(HttpServletRequest request, Problem problem) {
        UserDetailService.requireAdminLoginned(request);

        problemMapper.save(problem);
        long id = problem.getId();
        Path problemDir = judgeConfiguration.getDataDirectory(id);
        try {
            Files.createDirectories(problemDir);
        } catch (IOException ex) {
        }
        Long contest = problem.getContest();
        if (contest != null) {
            contestService.addProblem(contest, id);
        }
        request.setAttribute("id", id);
        request.setAttribute("dir", problemDir);

        return "admin/problems/add";
    }

}
