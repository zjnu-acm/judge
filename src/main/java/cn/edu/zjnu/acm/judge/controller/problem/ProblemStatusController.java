/*
 * Copyright 2016 ZJNU ACM.
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
package cn.edu.zjnu.acm.judge.controller.problem;

import cn.edu.zjnu.acm.judge.data.dto.ScoreCount;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.domain.Submission;
import cn.edu.zjnu.acm.judge.mapper.SubmissionMapper;
import cn.edu.zjnu.acm.judge.service.ProblemService;
import cn.edu.zjnu.acm.judge.service.SubmissionService;
import cn.edu.zjnu.acm.judge.service.UserDetailsServiceImpl;
import cn.edu.zjnu.acm.judge.util.ResultType;
import cn.edu.zjnu.acm.judge.util.URLBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author zhanhb
 */
@Controller
@Slf4j
public class ProblemStatusController {

    @Autowired
    private ProblemService problemService;
    @Autowired
    private SubmissionService submissionService;
    @Autowired
    private SubmissionMapper submissionMapper;

    @GetMapping("/gotoproblem")
    public String gotoProblem(@RequestParam(value = "pid", required = false) String pid,
            RedirectAttributes redirectAttributes) {
        long problemId;
        try {
            problemId = Long.parseLong(pid);
        } catch (NumberFormatException ex) {
            redirectAttributes.addAttribute("sstr", pid);
            return "redirect:/searchproblem";
        }
        redirectAttributes.addAttribute("problem_id", problemId);
        return "redirect:/showproblem";
    }

    @GetMapping("/problemstatus")
    @SuppressWarnings("AssignmentToMethodParameter")
    public String status(HttpServletRequest request,
            @RequestParam("problem_id") long id,
            @PageableDefault(size = 20, sort = {"time", "memory", "code_length"}) Pageable pageable,
            Authentication authentication) {
        log.debug("{}", pageable);
        if (pageable.getPageSize() > 500) {
            pageable = PageRequest.of(pageable.getPageNumber(), 500, pageable.getSort());
        }
        Problem problem = problemService.findOneNoI18n(id);
        List<ScoreCount> list = submissionMapper.groupByScore(null, id);
        ArrayList<String> scores = new ArrayList<>(list.size());
        ArrayList<Long> counts = new ArrayList<>(list.size());
        ArrayList<String> urls = new ArrayList<>(list.size());
        for (ScoreCount scoreCount : list) {
            int score = scoreCount.getScore();
            scores.add(ResultType.getShowsourceString(score));
            counts.add(scoreCount.getCount());
            urls.add(request.getContextPath() + "/status?problem_id=" + id + "&score=" + score);
        }
        Page<Submission> page = submissionService.bestSubmission(null, id, pageable, problem.getSubmitUser());
        boolean isAdmin = UserDetailsServiceImpl.isAdminLoginned(request);
        boolean isSourceBrowser = UserDetailsServiceImpl.isSourceBrowser(request);
        boolean canView = isAdmin || isSourceBrowser;

        request.setAttribute("page", page);
        request.setAttribute("sa", Arrays.asList(counts, scores, urls));
        request.setAttribute("problem", problem);
        request.setAttribute("url", URLBuilder.fromRequest(request).replaceQueryParam("page").toString());
        request.setAttribute("canView", canView);
        request.setAttribute("authentication", authentication);

        return "problems/status";
    }

}
