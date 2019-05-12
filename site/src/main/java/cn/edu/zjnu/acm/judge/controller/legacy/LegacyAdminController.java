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
package cn.edu.zjnu.acm.judge.controller.legacy;

import java.util.Map;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author zhanhb
 */
@Controller
@Secured("ROLE_ADMIN")
public class LegacyAdminController {

    @GetMapping("/admin.showproblem")
    public String showProblem(@RequestParam("problem_id") long problemId,
            RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("problemId", problemId);
        return "redirect:/admin/problems/{problemId}.html";
    }

    @GetMapping("/admin.showcontest")
    public String showContest(@RequestParam("contest_id") long contestId,
            RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("contestId", contestId);
        return "redirect:/admin/contests/{contestId}.html";
    }

    @GetMapping("/admin.rejudge")
    public String rejudge(RedirectAttributes attributes, @RequestParam Map<String, String> query) {
        attributes.addAllAttributes(query);
        return "redirect:/admin/rejudge";
    }

}
