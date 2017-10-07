package cn.edu.zjnu.acm.judge.service;

import cn.edu.zjnu.acm.judge.mapper.SubmissionMapper;
import cn.edu.zjnu.acm.judge.mapper.UserProblemMapper;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Rejudger {

    @Autowired
    private JudgePool judgePool;
    @Autowired
    private SubmissionMapper submissionMapper;
    @Autowired
    private UserProblemMapper userProblemMapper;

    public CompletableFuture<?> byProblemId(long problemId) {
        List<Long> submissions = submissionMapper.findAllByProblemIdAndResultNotAccept(problemId);
        CompletableFuture<?> future = judgePool.addAll(submissions.stream().mapToLong(Long::longValue).toArray());
        return future.thenRun(() -> {
            userProblemMapper.updateProblem(problemId);
            userProblemMapper.updateUsers();
        });
    }

    public CompletableFuture<?> bySubmissionId(long submissionId) {
        return judgePool.add(submissionId).thenApply(__ -> submissionMapper.findOne(submissionId));
    }

}
