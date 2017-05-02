package cn.edu.zjnu.acm.judge.problem;

import cn.edu.zjnu.acm.judge.config.JudgeConfiguration;
import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.ContestMapper;
import cn.edu.zjnu.acm.judge.mapper.ProblemMapper;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

@Controller
public class ShowProblemController {

    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private ContestMapper contestMapper;
    @Autowired
    private JudgeConfiguration judgeConfiguration;

    private Problem getProblem(long problemId, Locale locale) {
        Problem problem = problemMapper.findOne(problemId, locale.getLanguage());
        while (problem != null) {
            Long contestId = problem.getContest();
            if (contestId != null) {
                Contest contest = contestMapper.findOne(contestId);
                if (contest != null && !contest.isDisabled()) {
                    boolean started = contest.isStarted();
                    if (!contest.isEnded()) {
                        if (started) {
                            return problem;
                        }
                        break;
                    } else {
                        problemMapper.setContest(problemId, null);
                        problem.setContest(null);
                    }
                }
            }
            if (!problem.isDisabled()) {
                return problem;
            }
            break;
        }
        throw new MessageException("Can not find problem (ID:" + problemId + ")", HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/showproblem", produces = TEXT_HTML_VALUE)
    public String showproblem(Model model,
            @RequestParam("problem_id") long problemId,
            Locale locale) {
        Problem problem = getProblem(problemId, locale);
        Long contestId = problem.getContest();
        Path dataPath = judgeConfiguration.getDataDirectory(problemId);
        model.addAttribute("problem", problem);
        model.addAttribute("isSpecial", Files.exists(dataPath.resolve(JudgeConfiguration.VALIDATE_FILE_NAME)));
        model.addAttribute("contestId", contestId);
        String title1, title2;
        if (contestId == null) {
            title1 = problemId + " -- " + problem.getTitle();
            title2 = problem.getTitle();
        } else {
            Long problemIdInContest = contestMapper.getProblemIdInContest(contestId, problemId);
            long contestNum = problemIdInContest == null ? -1 : problemIdInContest;
            List<Problem> problems = contestMapper.getProblems(contestId, null, locale.getLanguage());
            model.addAttribute("problems", problems);
            title1 = (char) (contestNum + 'A') + ":" + problemId + " -- " + problem.getTitle();
            title2 = (char) (contestNum + 'A') + ":" + problem.getTitle();
        }
        model.addAttribute("title1", title1);
        model.addAttribute("title2", title2);

        return "problems/view";
    }

}
