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
package cn.edu.zjnu.acm.judge.controller.contest;

import cn.edu.zjnu.acm.judge.config.JudgeConfiguration;
import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.exception.BusinessCode;
import cn.edu.zjnu.acm.judge.exception.BusinessException;
import cn.edu.zjnu.acm.judge.service.ContestService;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

/**
 * @author zhanhb
 */
@Controller("contest")
@RequestMapping("/contests/{contestId}")
public class ContestController {

    @Autowired
    private JudgeConfiguration judgeConfiguration;
    @Autowired
    private ContestService contestService;

    @GetMapping(value = "standing", produces = {TEXT_HTML_VALUE, ALL_VALUE})
    public Future<ModelAndView> standingHtml(@PathVariable("contestId") long contestId, Locale locale) {
        return contestService.standingAsync(contestId).thenCombineAsync(CompletableFuture.supplyAsync(() -> contestService.getContestAndProblems(contestId, null, locale)), (standing, contest) -> {
            ModelMap model = new ModelMap();
            model.addAttribute("contestId", contestId);
            model.addAttribute("contest", contest);
            model.addAttribute("id", contestId);
            model.addAttribute("problems", contest.getProblems());
            model.addAttribute("standing", standing);
            return new ModelAndView("contests/standing", model);
        });
    }

    @GetMapping
    public String index(@PathVariable("contestId") long contestId, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("contestId", contestId);
        return "redirect:/contests/{contestId}/problems";
    }

    @GetMapping("problems")
    public String problems(@PathVariable("contestId") long contestId, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("contest_id", contestId);
        return "redirect:/showcontest";
    }

    @GetMapping("problems/{pid}")
    public String showProblem(@PathVariable("contestId") long contestId, @PathVariable("pid") long problemNum, Model model, Locale locale) {
        Contest contest = contestService.getContestAndProblems(contestId, null, locale);
        if (contest == null) {
            throw new BusinessException(BusinessCode.CONTEST_NOT_FOUND, contest);
        }
        Problem problem = contestService.getProblem(contestId, problemNum, locale);
        if (problem == null) {
            throw new BusinessException(BusinessCode.PROBLEM_NOT_FOUND, problemNum);
        }
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

}
