/*
 * Copyright 2017 ZJNU ACM.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.edu.zjnu.acm.judge.controller.contest;

import cn.edu.zjnu.acm.judge.config.JudgeConfiguration;
import cn.edu.zjnu.acm.judge.data.dto.ScoreCount;
import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.domain.Submission;
import cn.edu.zjnu.acm.judge.exception.BusinessCode;
import cn.edu.zjnu.acm.judge.exception.BusinessException;
import cn.edu.zjnu.acm.judge.mapper.SubmissionMapper;
import cn.edu.zjnu.acm.judge.service.ContestService;
import cn.edu.zjnu.acm.judge.service.SubmissionService;
import cn.edu.zjnu.acm.judge.service.UserDetailsServiceImpl;
import cn.edu.zjnu.acm.judge.util.ResultType;
import cn.edu.zjnu.acm.judge.util.URLBuilder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

/**
 *
 * @author zhanhb
 */
@Controller
@RequestMapping("/contests/{contestId}/problems")
public class ContestProblemController {

    @Autowired
    private JudgeConfiguration judgeConfiguration;
    @Autowired
    private ContestService contestService;
    @Autowired
    private SubmissionMapper submissionMapper;
    @Autowired
    private SubmissionService submissionService;

    @GetMapping
    public String problems(Model model, Locale locale,
            @PathVariable("contestId") long contestId,
            Authentication authentication) {
        Contest contest = contestService.getContestAndProblemsNotDisabled(contestId, authentication != null ? authentication.getName() : null, locale);
        model.addAttribute("contestId", contestId);
        model.addAttribute("contest", contest);
        if (contest.isStarted()) {
            model.addAttribute("problems", contest.getProblems());
        } else {
            contest.setProblems(null);
        }
        return "contests/problems";
    }

    @GetMapping("{pid}")
    public String showProblem(@PathVariable("contestId") long contestId, @PathVariable("pid") long problemNum, Model model, Locale locale) {
        Contest contest = contestService.getContestAndProblemsNotDisabled(contestId, null, locale);
        if (!contest.isStarted()) {
            throw new BusinessException(BusinessCode.CONTEST_NOT_STARTED, contest.getId(), contest.getStartTime());
        }
        Problem problem = contestService.getProblem(contestId, problemNum, locale);
        model.addAttribute("problem", problem);
        model.addAttribute("problems", contest.getProblems());
        Path dataPath = judgeConfiguration.getDataDirectory(problem.getOrigin());
        model.addAttribute("isSpecial", Files.exists(dataPath.resolve(JudgeConfiguration.VALIDATE_FILE_NAME)));
        List<Long> problemsId = contest.getProblems().stream().map(Problem::getOrigin).collect(Collectors.toList());
        String index = contestService.toProblemIndex(problemsId.indexOf(problem.getOrigin()));
        String title1 = index + ":" + problem.getOrigin() + " -- " + problem.getTitle();
        String title2 = index + ":" + problem.getTitle();
        model.addAttribute("title1", title1);
        model.addAttribute("title2", title2);
        return "contests/problem";
    }

    @GetMapping(value = "{pid}/status", produces = TEXT_HTML_VALUE)
    public String status(
            @PathVariable("contestId") long contestId,
            @PathVariable("pid") int problemNum,
            @PageableDefault(size = 20, sort = {"time", "memory", "code_length"}) Pageable pageable,
            Model model, Authentication authentication, HttpServletRequest request) {
        contestService.findOneByIdAndNotDisabled(contestId); // check if problem exists and not disabled
        Problem problem = contestService.getProblem(contestId, problemNum, null);
        Page<Submission> page = submissionService.bestSubmission(contestId, problem.getOrigin(), pageable, problem.getSubmitUser());
        model.addAttribute("page", page);
        List<ScoreCount> list = submissionMapper.groupByScore(contestId, problem.getOrigin());
        ArrayList<String> scores = new ArrayList<>(list.size());
        ArrayList<Long> counts = new ArrayList<>(list.size());
        ArrayList<String> urls = new ArrayList<>(list.size());
        for (ScoreCount scoreCount : list) {
            int score = scoreCount.getScore();
            scores.add(ResultType.getShowsourceString(score));
            counts.add(scoreCount.getCount());
            urls.add(request.getContextPath() + "/status?contest_id=" + contestId + "&problem_id=" + problem.getOrigin() + "&score=" + score);
        }
        boolean isAdmin = UserDetailsServiceImpl.isAdminLoginned(request);
        boolean isSourceBrowser = UserDetailsServiceImpl.isSourceBrowser(request);
        boolean canView = isAdmin || isSourceBrowser;

        request.setAttribute("page", page);
        request.setAttribute("sa", Arrays.asList(counts, scores, urls));
        request.setAttribute("problem", problem);
        request.setAttribute("url", URLBuilder.fromRequest(request).replaceQueryParam("page").toString());
        request.setAttribute("contestId", contestId);
        request.setAttribute("canView", canView);
        request.setAttribute("authentication", authentication);
        return "contests/problems-status";
    }

}
