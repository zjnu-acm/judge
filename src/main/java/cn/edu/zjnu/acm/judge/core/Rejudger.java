package cn.edu.zjnu.acm.judge.core;

import cn.edu.zjnu.acm.judge.mapper.SubmissionMapper;
import cn.edu.zjnu.acm.judge.mapper.UserProblemMapper;
import java.io.IOException;
import java.util.List;
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
        } catch (IOException | RuntimeException ex) {
            log.error("", ex);
        }
    }

    private void rejudgeProblem(long problemId) throws IOException {
        List<Long> submissions = submissionMapper.findAllByProblemId(problemId);
        for (long submission : submissions) {
            judger.reJudge(submission);
        }
        userProblemMapper.updateProblem(problemId);
        userProblemMapper.updateUsers();
    }

    public void rejudge(long submissionId) throws IOException {
        judger.reJudge(submissionId);
    }

}
