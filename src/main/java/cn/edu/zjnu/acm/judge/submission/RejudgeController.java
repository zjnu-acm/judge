package cn.edu.zjnu.acm.judge.submission;

import cn.edu.zjnu.acm.judge.core.Rejudger;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.service.UserDetailService;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RejudgeController {

    @Autowired
    private Rejudger rejudger;

    // TODO request method
    @RequestMapping(value = "/admin.rejudge", method = {RequestMethod.GET, RequestMethod.HEAD})
    public String rejudge(HttpServletRequest request,
            @RequestParam(value = "problem_id", defaultValue = "0") long problemId,
            @RequestParam(value = "solution_id", defaultValue = "0") long submissionId)
            throws IOException {
        UserDetailService.requireAdminLoginned(request);
        if (submissionId != 0) {
            rejudger.rejudge(submissionId);
        } else if (problemId != 0) {
            new Thread(() -> rejudger.run(problemId)).start();
        } else {
            throw new MessageException("please specified problem_id or solution_id", HttpStatus.BAD_REQUEST);
        }
        return "admin/recieved";
    }

}
