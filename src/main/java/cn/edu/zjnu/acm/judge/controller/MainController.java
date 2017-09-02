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

import cn.edu.zjnu.acm.judge.exception.GlobalExceptionHandler;
import cn.edu.zjnu.acm.judge.service.ContestOnlyService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

/**
 *
 * @author zhanhb
 */
@Controller
public class MainController {

    @Autowired
    private ContestOnlyService contestOnlyService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/faq")
    public String faq() {
        return "faq";
    }

    @GetMapping("/findpassword")
    public String findPassword() {
        return "findpassword";
    }

    @GetMapping({"/registerpage", "/register"})
    public String registerPage() {
        contestOnlyService.checkRegister();
        return "registerpage";
    }

    @GetMapping(value = "/unauthorized", produces = {TEXT_HTML_VALUE, ALL_VALUE})
    public String unauthorizedHtml(HttpServletRequest request) {
        return GlobalExceptionHandler.unauthorized(request);
    }

    @GetMapping(value = "/unauthorized", produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public String unauthorized() {
        return "{}";
    }

}
