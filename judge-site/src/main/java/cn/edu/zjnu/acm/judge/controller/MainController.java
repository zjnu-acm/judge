/*
 * Copyright 2016-2019 ZJNU ACM.
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

import cn.edu.zjnu.acm.judge.service.ContestOnlyService;
import cn.edu.zjnu.acm.judge.service.SystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

/**
 *
 * @author zhanhb
 */
@Controller
@RequestMapping(produces = TEXT_HTML_VALUE)
@RequiredArgsConstructor
public class MainController {

    private final ContestOnlyService contestOnlyService;
    private final SystemService systemService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("content", systemService.getIndex());
        return "index";
    }

    @GetMapping("faq")
    public String faq() {
        return "faq";
    }

    @GetMapping("findpassword")
    public String findPassword() {
        return "users/findPassword";
    }

    @GetMapping({"registerpage", "register"})
    public String registerPage() {
        contestOnlyService.checkRegister();
        return "users/registerPage";
    }

}
