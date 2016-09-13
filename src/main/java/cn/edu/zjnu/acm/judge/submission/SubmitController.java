package cn.edu.zjnu.acm.judge.submission;

import cn.edu.zjnu.acm.judge.config.JudgeConfiguration;
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
import cn.edu.zjnu.acm.judge.service.LanguageService;
import cn.edu.zjnu.acm.judge.service.UserDetailService;
import cn.edu.zjnu.acm.judge.util.ResultType;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
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
    @Autowired
    private LanguageService languageService;

    @Secured("ROLE_USER")
    @PostMapping("/submit")
    public synchronized String submit(HttpServletRequest request,
            @RequestParam("language") int languageId,
            @RequestParam("problem_id") long problemId,
            @RequestParam("source") String source,
            RedirectAttributes redirectAttributes) {
        Language language;
        try {
            language = languageService.getLanguage(languageId);
        } catch (IllegalArgumentException ex) {
            throw new MessageException("Please choose a language", HttpStatus.BAD_REQUEST);
        }
        if (source.length() > 32768) {
            throw new MessageException("Source code too long, submit FAILED;if you really need submit this source please contact administrator", HttpStatus.BAD_REQUEST);
        }
        if (source.length() < 10) {
            throw new MessageException("Source code too short, submit FAILED;if you really need submit this source please contact administrator", HttpStatus.BAD_REQUEST);
        }
        UserModel userModel = UserDetailService.getCurrentUser(request).orElseThrow(ForbiddenException::new);
        Instant instant = Instant.now();
        long now = instant.toEpochMilli();  //获取当前时间

        // 8秒交一次。。。
        // 使用绝对值，如果系统时间被改了依然可用
        if (Math.abs(userModel.getLastSubmitTime() - now) < 10000) {
            throw new MessageException("Sorry, please don't submit again within 10 seconds.", HttpStatus.BAD_REQUEST);
        }
        userModel.setLastSubmitTime(now);
        String userId = userModel.getUserId();
        Problem problem = problemMapper.findOneNoI18n(problemId);

        //检查该题是否被禁用
        if (problem == null) {
            throw new MessageException("No such problem", HttpStatus.NOT_FOUND);
        }
        Path dataPath = judgeConfiguration.getDataDirectory(problemId);

        Long contestId = problem.getContest();
        long memoryLimit = problem.getMemoryLimit();
        long timeLimit = problem.getTimeLimit();

        long num = -1;
        if (contestId != null) { //竞赛是否存在
            Contest contest = contestMapper.findOne(contestId);

            // foreign key constraint, will never be null
            assert contest != null;

            // contest not started yet, can't view the problem.
            if (!contest.isStarted()) {
                throw new MessageException("No such problem", HttpStatus.NOT_FOUND);
            }

            // set problem id to null if the contest is ended.
            if (contest.isEnded()) {
                problemMapper.setContest(problemId, null);
                contestId = null;
            } else { //num为竞赛中的题目编号
                num = Optional.ofNullable(contestMapper.getProblemIdInContest(contestId, problemId)).orElse(-1L);
            }
        }

        // 插入solution数据库表
        Submission submission = Submission.builder()
                .problem(problemId)
                .user(userId)
                .inDate(instant)
                .sourceLength(source.length())
                .language(languageId)
                .ip(request.getRemoteAddr())
                .contest(contestId)
                .num(num)
                .score(ResultType.QUEUING)
                .build();
        submissionMapper.save(submission);
        long submissionId = submission.getId();

        RunRecord runRecord = RunRecord.builder()
                .submissionId(submissionId)
                .problemId(problemId)
                .memoryLimit(memoryLimit)
                .timeLimit(timeLimit)
                .language(language)
                .dataPath(dataPath)
                .source(source)
                .userId(userId)
                .build();
        problemMapper.setInDate(problemId, instant);

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
