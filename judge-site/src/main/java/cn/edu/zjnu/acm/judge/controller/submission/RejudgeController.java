package cn.edu.zjnu.acm.judge.controller.submission;

import cn.edu.zjnu.acm.judge.service.RejudgeService;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author zhanhb
 */
@RequestMapping(produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@RestController
@Secured("ROLE_ADMIN")
public class RejudgeController {

    private final RejudgeService rejudgeService;

    // TODO request method
    @GetMapping(value = "admin/rejudge", params = "solution_id")
    public CompletableFuture<?> rejudgeSolution(
            @RequestParam("solution_id") long submissionId) {
        return rejudgeService.bySubmissionId(submissionId);
    }

    @GetMapping(value = "admin/rejudge", params = "problem_id")
    public ResponseEntity<?> rejudgeProblem(@RequestParam("problem_id") long problemId) {
        rejudgeService.byProblemId(problemId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Collections.singletonMap("message", "重新评测请求已经受理"));
    }

}
