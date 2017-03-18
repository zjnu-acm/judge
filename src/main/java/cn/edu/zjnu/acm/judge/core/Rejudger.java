package cn.edu.zjnu.acm.judge.core;

import cn.edu.zjnu.acm.judge.mapper.SubmissionMapper;
import cn.edu.zjnu.acm.judge.mapper.UserProblemMapper;
import java.util.List;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Rejudger {

    @Autowired
    private Judger judger;
    @Autowired
    private SubmissionMapper submissionMapper;
    @Autowired
    private UserProblemMapper userProblemMapper;

    public void run(long pid) {
        try {
            rejudgeProblem(pid);
        } catch (InterruptedException | ExecutionException | RuntimeException ex) {
            log.error("", ex);
        }
    }

    private void rejudgeProblem(long problemId) throws InterruptedException, ExecutionException {
        List<Long> submissions = submissionMapper.findAllByProblemIdAndREsultNotAccept(problemId);
        for (long submission : submissions) {
            judger.reJudge(submission);
        }
        userProblemMapper.updateProblem(problemId);
        userProblemMapper.updateUsers();
    }

    public void rejudge(long submissionId) throws InterruptedException, ExecutionException {
        judger.reJudge(submissionId);
    }

}
