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

import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.exception.BusinessCode;
import cn.edu.zjnu.acm.judge.exception.BusinessException;
import cn.edu.zjnu.acm.judge.service.ContestService;
import com.google.common.collect.ImmutableMap;
import java.util.Locale;
import java.util.concurrent.Future;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
    private ContestService contestService;

    @GetMapping(value = "standing", produces = {TEXT_HTML_VALUE, ALL_VALUE})
    public Future<ModelAndView> standingHtml(@PathVariable("contestId") long contestId, Locale locale) {
        Contest contest = contestService.getContestAndProblemsNotDisabled(contestId, null, locale);
        if (!contest.isStarted()) {
            throw new BusinessException(BusinessCode.CONTEST_NOT_STARTED, contest.getId(), contest.getStartTime());
        }
        return contestService.standingAsync(contestId).thenApply(standing
                -> new ModelAndView("contests/standing", ImmutableMap.<String, Object>builder()
                        .put("contestId", contestId)
                        .put("contest", contest)
                        .put("id", contestId)
                        .put("problems", contest.getProblems())
                        .put("standing", standing)
                        .build()));
    }

    @GetMapping
    public String index(@PathVariable("contestId") long contestId, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("contestId", contestId);
        return "redirect:/contests/{contestId}/problems.html";
    }

}
