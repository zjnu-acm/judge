package cn.edu.zjnu.acm.judge.service;

import cn.edu.zjnu.acm.judge.mapper.SubmissionMapper;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Rejudger {

    @Autowired
    private JudgePool judgePool;
    @Autowired
    private SubmissionMapper submissionMapper;

    public CompletableFuture<?> byProblemId(long problemId) {
        List<Long> submissions = submissionMapper.findAllByProblemIdAndResultNotAccept(problemId);
        return judgePool.addAll(submissions.stream().mapToLong(Long::longValue).toArray());
    }

    public CompletableFuture<?> bySubmissionId(long submissionId) {
        return judgePool.add(submissionId).thenApply(__ -> submissionMapper.findOne(submissionId));
    }

}
