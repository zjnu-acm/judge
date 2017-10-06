package cn.edu.zjnu.acm.judge.controller.submission;

import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.domain.Submission;
import cn.edu.zjnu.acm.judge.exception.BusinessCode;
import cn.edu.zjnu.acm.judge.exception.BusinessException;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.ContestMapper;
import cn.edu.zjnu.acm.judge.mapper.ProblemMapper;
import cn.edu.zjnu.acm.judge.mapper.SubmissionMapper;
import cn.edu.zjnu.acm.judge.mapper.UserPreferenceMapper;
import cn.edu.zjnu.acm.judge.service.ContestOnlyService;
import cn.edu.zjnu.acm.judge.service.JudgePool;
import cn.edu.zjnu.acm.judge.service.LanguageService;
import cn.edu.zjnu.acm.judge.util.ResultType;
import com.google.common.cache.CacheBuilder;
import java.time.Instant;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SubmitController {

    @Autowired
    private JudgePool judgePool;
    @Autowired
    private SubmissionMapper submissionMapper;
    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private ContestMapper contestMapper;
    @Autowired
    private UserPreferenceMapper userPerferenceMapper;
    @Autowired
    private LanguageService languageService;
    @Autowired
    private ContestOnlyService contestOnlyService;
    private final Set<String> cache = Collections.newSetFromMap(CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.SECONDS).<String, Boolean>build().asMap());

    @Secured("ROLE_USER")
    @PostMapping("/submit")
    public synchronized String submit(HttpServletRequest request,
            @RequestParam("language") int languageId,
            @RequestParam("problem_id") long problemId,
            @RequestParam("source") String source,
            @RequestParam(value = "contest_id", required = false) Long contestId,
            RedirectAttributes redirectAttributes,
            Authentication authentication) {
        try {
            languageService.getAvailableLanguage(languageId);
        } catch (IllegalArgumentException ex) {
            throw new MessageException("Please choose a language", HttpStatus.BAD_REQUEST);
        }
        if (source.length() > 32768) {
            throw new MessageException("Source code too long, submit FAILED;if you really need submit this source please contact administrator", HttpStatus.BAD_REQUEST);
        }
        if (source.length() < 10) {
            throw new MessageException("Source code too short, submit FAILED;if you really need submit this source please contact administrator", HttpStatus.BAD_REQUEST);
        }
        String userId = authentication.getName();
        Instant instant = Instant.now();
        // 10秒交一次。。。
        // 使用绝对值，如果系统时间被改了依然可用
        if (cache.contains(userId) || !cache.add(userId)) {
            throw new MessageException("Sorry, please don't submit again within 10 seconds.", HttpStatus.BAD_REQUEST);
        }
        Problem problem = problemMapper.findOneNoI18n(problemId);

        //检查该题是否存在
        if (problemMapper.findOneNoI18n(problemId) == null) {
            throw new BusinessException(BusinessCode.PROBLEM_NOT_FOUND, problemId);
        }

        if (contestId != null) { //竞赛是否存在
            Contest contest = contestMapper.findOne(contestId);

            // foreign key constraint, will never be null
            assert contest != null;

            // contest not started yet, can't view the problem.
            if (!contest.isStarted()) {
                throw new BusinessException(BusinessCode.PROBLEM_NOT_FOUND, problemId);
            }

            // set contest id of the problem to null if the contest is ended.
            if (contest.isEnded()) {
                contestId = null;
            }
        }
        contestOnlyService.checkSubmit(request, contestId, problemId);
        // 插入solution数据库表
        Submission submission = Submission.builder()
                .problem(problemId)
                .user(userId)
                .inDate(instant)
                .sourceLength(source.length())
                .language(languageId)
                .ip(request.getRemoteAddr())
                .contest(contestId)
                .score(ResultType.QUEUING)
                .build();
        submissionMapper.save(submission);
        long submissionId = submission.getId();
        problemMapper.setInDate(problemId, instant);

        // 插入source_code表
        submissionMapper.saveSource(submissionId, source);
        judgePool.add(submissionId);
        userPerferenceMapper.setLanguage(userId, languageId);

        //提交是否在竞赛中
        redirectAttributes.addAttribute("user_id", userId);
        if (contestId != null) {
            redirectAttributes.addAttribute("contest_id", contestId);
        }
        return "redirect:/status";
    }

}
