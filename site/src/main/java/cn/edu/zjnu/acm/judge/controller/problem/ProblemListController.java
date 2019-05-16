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

import cn.edu.zjnu.acm.judge.data.form.ProblemForm;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.exception.BusinessCode;
import cn.edu.zjnu.acm.judge.exception.BusinessException;
import cn.edu.zjnu.acm.judge.service.ProblemService;
import cn.edu.zjnu.acm.judge.util.URIBuilder;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

/**
 * @author zhanhb
 */
@Controller
@RequestMapping(produces = TEXT_HTML_VALUE)
@RequiredArgsConstructor
public class ProblemListController {

    private final ProblemService problemService;

    @GetMapping({"problemlist", "problems"})
    public String problemList(ProblemForm form, Model model, Locale locale, Authentication authentication,
            @PageableDefault(100) Pageable pageable, HttpServletRequest request) {
        String currentUserId = authentication != null ? authentication.getName() : null;
        String url = URIBuilder.fromRequest(request).replaceQueryParam("page").toString();
        model.addAttribute("url", url);
        form.setDisabled(Boolean.FALSE);
        Page<Problem> page = problemService.findAll(form, currentUserId, pageable, locale);
        model.addAttribute("page", page);
        return "problems/list";
    }

    @GetMapping("searchproblem")
    public String searchProblem(ProblemForm form, Model model, Locale locale, Authentication authentication,
            @PageableDefault(1000) Pageable pageable, HttpServletRequest request) {
        if (!StringUtils.hasText(form.getSstr())) {
            throw new BusinessException(BusinessCode.PROBLEM_SEARCH_KEY_EMPTY);
        }
        String currentUserId = authentication != null ? authentication.getName() : null;
        String url = URIBuilder.fromRequest(request).replaceQueryParam("page").toString();
        model.addAttribute("url", url);
        model.addAttribute("query", form.getSstr());
        form.setDisabled(Boolean.FALSE);
        Page<Problem> page = problemService.findAll(form, currentUserId, pageable, locale);
        model.addAttribute("page", page);
        return "problems/search";
    }

}
