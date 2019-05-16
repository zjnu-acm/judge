/*
 * Copyright 2019 ZJNU ACM.
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

import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.exception.BusinessCode;
import cn.edu.zjnu.acm.judge.exception.BusinessException;
import cn.edu.zjnu.acm.judge.mapper.ProblemMapper;
import cn.edu.zjnu.acm.judge.service.LocaleService;
import cn.edu.zjnu.acm.judge.service.SystemService;
import java.util.Locale;
import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

/**
 * @author zhanhb
 */
@Controller
@RequestMapping(produces = TEXT_HTML_VALUE)
@RequiredArgsConstructor
public class ShowProblemController {

    private final ProblemMapper problemMapper;
    private final LocaleService localeService;
    private final SystemService systemService;

    private Problem getProblem(long problemId, @Nullable Locale locale) {
        Problem problem = problemMapper.findOne(problemId, localeService.resolve(locale));
        if (problem != null) {
            Boolean disabled = problem.getDisabled();
            if (disabled == null || !disabled) {
                return problem;
            }
        }
        throw new BusinessException(BusinessCode.PROBLEM_NOT_FOUND, problemId);
    }

    @GetMapping("showproblem")
    public String showProblem(Model model, @RequestParam("problem_id") long problemId, @Nullable Locale locale) {
        Problem problem = getProblem(problemId, locale);
        model.addAttribute("problem", problem);
        model.addAttribute("isSpecial", systemService.isSpecialJudge(problemId));
        String title1 = problemId + " -- " + problem.getTitle();
        String title2 = problem.getTitle();
        model.addAttribute("title1", title1);
        model.addAttribute("title2", title2);
        return "problems/view";
    }

}
