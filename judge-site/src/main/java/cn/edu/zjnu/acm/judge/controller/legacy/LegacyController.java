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
package cn.edu.zjnu.acm.judge.controller.legacy;

import java.net.URI;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

/**
 *
 * @author zhanhb
 */
@Controller
@Deprecated
@RequestMapping(produces = TEXT_HTML_VALUE)
public class LegacyController {

    @Deprecated
    @GetMapping("conteststanding")
    public String contestStanding(@RequestParam("contest_id") long contestId,
            RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("contestId", contestId);
        return "redirect:/contests/{contestId}/standing.html";
    }

    @Deprecated
    @GetMapping("showcontest")
    public String showContest(@RequestParam("contest_id") long contestId,
            RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("contestId", contestId);
        return "redirect:/contests/{contestId}/problems.html";
    }

    @Deprecated
    @GetMapping("support/ckfinder.action")
    public ResponseEntity<?> ckfinder(HttpServletRequest request,
            @RequestParam("path") String path) {
        try {
            URI uri = URI.create(request.getContextPath() + "/userfiles/").resolve(path.replaceAll("^/+", ""));
            if (!uri.isAbsolute()) {
                return ResponseEntity.status(HttpStatus.FOUND).location(uri).build();
            }
        } catch (IllegalArgumentException ignored) {
        }
        return ResponseEntity.notFound().build();
    }

}
