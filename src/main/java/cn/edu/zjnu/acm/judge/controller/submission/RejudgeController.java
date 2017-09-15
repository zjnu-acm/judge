package cn.edu.zjnu.acm.judge.controller.submission;

import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.service.Rejudger;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Secured("ROLE_ADMIN")
public class RejudgeController {

    @Autowired
    private Rejudger rejudger;

    // TODO request method
    @GetMapping("/admin.rejudge")
    @ResponseBody
    public CompletableFuture<?> rejudge(
            @RequestParam(value = "problem_id", defaultValue = "0") long problemId,
            @RequestParam(value = "solution_id", defaultValue = "0") long submissionId)
            throws InterruptedException, ExecutionException {
        if (submissionId != 0) {
            return rejudger.bySubmissionId(submissionId);
        } else if (problemId != 0) {
            return rejudger.byProblemId(problemId);
        } else {
            throw new MessageException("please specified problem_id or solution_id", HttpStatus.BAD_REQUEST);
        }
    }

}
