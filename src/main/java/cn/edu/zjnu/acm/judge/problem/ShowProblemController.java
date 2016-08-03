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

    @GetMapping(value = "/showproblem", produces = TEXT_HTML_VALUE)
    public String showproblem(Model model,
            @RequestParam("problem_id") long problemId,
            Locale locale) {
        Problem problem = problemMapper.findOne(problemId, locale.getLanguage());
        if (problem == null) {
            throw new MessageException("Can not find problem (ID:" + problemId + ")", HttpStatus.NOT_FOUND);
        }
        Long contestId = problem.getContest();
        Path dataPath = judgeConfiguration.getDataDirectory(problemId);
        long contestNum = -1;
        boolean started = true;
        boolean ended = true;
        if (contestId != null) {
            Contest contest = contestMapper.findOne(contestId);
            if (contest != null) {
                started = contest.isStarted();
                ended = contest.isEnded();
            }
            if (ended) {
                problemMapper.setContest(problemId, null);
                contestId = null;
            }
            if (problem.isDisabled() && contestId == null) {
                started = false;
            }
        }
        if (contestId != null) {
            Long problemIdInContest = contestMapper.getProblemIdInContest(contestId, problemId);
            contestNum = problemIdInContest == null ? -1 : problemIdInContest;
        }
        if (!started) {
            throw new MessageException("Can not find problem (ID:" + problemId + ")", HttpStatus.NOT_FOUND);
        }
        model.addAttribute("problem", problem);
        model.addAttribute("isSpecial", Files.exists(dataPath.resolve(JudgeConfiguration.VALIDATE_FILE_NAME)));
        model.addAttribute("contestId", contestId);
        if (contestId == null) {
            model.addAttribute("title1", problemId + " -- " + problem.getTitle());
            model.addAttribute("title2", problem.getTitle());
        } else {
            List<Problem> problems = contestMapper.getProblems(contestId, null, locale.getLanguage());
            model.addAttribute("title1", (char) (contestNum + 'A') + ":" + problemId + " -- " + problem.getTitle());
            model.addAttribute("title2", (char) (contestNum + 'A') + ":" + problem.getTitle());
            model.addAttribute("problems", problems);
        }

        return "problems/view";
    }

}
