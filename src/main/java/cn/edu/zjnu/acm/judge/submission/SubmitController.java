package cn.edu.zjnu.acm.judge.submission;

import cn.edu.zjnu.acm.judge.config.JudgeConfiguration;
import cn.edu.zjnu.acm.judge.config.LanguageFactory;
import cn.edu.zjnu.acm.judge.core.Judger;
import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.domain.Language;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.domain.RunRecord;
import cn.edu.zjnu.acm.judge.domain.Submission;
import cn.edu.zjnu.acm.judge.domain.UserModel;
import cn.edu.zjnu.acm.judge.exception.ForbiddenException;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.ContestMapper;
import cn.edu.zjnu.acm.judge.mapper.ProblemMapper;
import cn.edu.zjnu.acm.judge.mapper.SubmissionMapper;
import cn.edu.zjnu.acm.judge.mapper.UserPerferenceMapper;
import cn.edu.zjnu.acm.judge.service.UserDetailService;
import cn.edu.zjnu.acm.judge.util.ResultType;
import java.nio.file.Path;
import java.time.Instant;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SubmitController {

    @Autowired
    private Judger judger;
    @Autowired
    private SubmissionMapper submissionMapper;
    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private ContestMapper contestMapper;
    @Autowired
    private UserPerferenceMapper userPerferenceMapper;
    @Autowired
    private JudgeConfiguration judgeConfiguration;

    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public String submit(HttpServletRequest request,
            @RequestParam("language") int languageId,
            @RequestParam("problem_id") long problemId,
            @RequestParam("source") String source,
            RedirectAttributes redirectAttributes) {
        UserDetailService.requireLoginned(request);
        Language language;
        try {
            language = LanguageFactory.getLanguage(languageId);
        } catch (IllegalArgumentException ex) {
            throw new MessageException("Please choose a language");
        }
        if (source.length() > 32768) {
            throw new MessageException("Source code too long, submit FAILED;if you really need submit this source please contact administrator");
        }
        if (source.length() < 10) {
            throw new MessageException("Source code too short, submit FAILED;if you really need submit this source please contact administrator");
        }
        UserModel userModel = UserDetailService.getCurrentUser(request).orElseThrow(ForbiddenException::new);
        assert userModel != null;
        Instant nowTimestamp = Instant.now();
        long now = nowTimestamp.toEpochMilli();  //获取当前时间

        // 8秒交一次。。。
        // 使用绝对值，如果系统时间被改了依然可用
        if (Math.abs(userModel.getLastSubmitTime() - now) < 10000) {
            throw new MessageException("Sorry, please don't submit again within 10 seconds.");
        }
        userModel.setLastSubmitTime(now);
        String userId = userModel.getUserId();
        Problem problem = problemMapper.findOne(problemId);

        Path dataPath = judgeConfiguration.getDataDirectory(problemId);

        //检查该题是否被禁用
        if (problem == null) {
            throw new MessageException("No such problem");
        }
        Long contestId = problem.getContest();
        long memoryLimit = problem.getMemoryLimit();
        long timeLimit = problem.getTimeLimit();

        boolean started;
        boolean ended = true;
        long num = -1;
        if (contestId != null) { //竞赛是否存在
            Contest contest = contestMapper.findOne(contestId);
            if (contest != null) {

                //是否超过竞赛开始时间
                started = contest.isStarted();

                //是否超过竞赛结束时间
                ended = contest.isEnded();

                if (!started) {
                    throw new MessageException("No such problem");
                }
            }
            //题目中竞赛id置为空
            if (ended) {
                problemMapper.setContest(problemId, null);
                contestId = null;
            } else { //num为竞赛中的题目编号
                num = contestMapper.getProblemIdInContest(contestId, problemId);
            }
        }

        // 插入solution数据库表
        Submission submission = Submission.builder()
                .problem(problemId)
                .user(userId)
                .inDate(nowTimestamp)
                .sourceLength(source.length())
                .score(languageId)
                .language(languageId)
                .ip(request.getRemoteAddr())
                .contest(contestId)
                .num(num)
                .score(ResultType.QUEUING)
                .build();
        submissionMapper.save(submission);
        long submissionId = submission.getId();

        RunRecord runRecord = new RunRecord();
        runRecord.setSubmissionId(submissionId);
        runRecord.setProblemId(problemId);
        runRecord.setMemoryLimit(memoryLimit);
        runRecord.setTimeLimit(timeLimit);
        runRecord.setLanguage(language);

        runRecord.setDataPath(dataPath);
        runRecord.setSource(source);
        problemMapper.setInDate(problemId, nowTimestamp);
        runRecord.setUserId(userId);

        // 插入source_code表
        submissionMapper.saveSource(submissionId, source);
        judger.judge(runRecord);
        userPerferenceMapper.setLanguage(userId, languageId);

        //提交是否在竞赛中
        redirectAttributes.addAttribute("user_id", userId);
        if (contestId != null) {
            redirectAttributes.addAttribute("contest_id", contestId);
        }
        return "redirect:/status";
    }

}
