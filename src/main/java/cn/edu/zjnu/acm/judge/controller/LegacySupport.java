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
package cn.edu.zjnu.acm.judge.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author zhanhb
 */
@Controller
public class LegacySupport {

    @RequestMapping(value = "/admin.showproblem", method = {RequestMethod.GET, RequestMethod.HEAD})
    public String showproblem(HttpServletRequest request,
            @RequestParam("problem_id") long problemId,
            RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("problemId", problemId);
        return "redirect:/admin/problems/{problemId}";
    }

}
