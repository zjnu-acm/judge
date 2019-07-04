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
package cn.edu.zjnu.acm.judge.controller;

import cn.edu.zjnu.acm.judge.config.JudgeHandlerInterceptor;
import java.util.Collections;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

/**
 *
 * @author zhanhb
 */
@Controller
@RequestMapping("unauthorized")
public class UnauthorizedEntryController {

    @GetMapping(produces = TEXT_HTML_VALUE)
    public String unauthorizedHtml(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String url = (String) request.getAttribute(JudgeHandlerInterceptor.BACK_URL_ATTRIBUTE_NAME);
        if (StringUtils.hasText(url)) {
            redirectAttributes.addAttribute("url", url);
        }
        return "redirect:/login";
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<?, ?>> unauthorized() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyMap());
    }

}
