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

import com.google.code.kaptcha.servlet.KaptchaExtend;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author zhanhb
 */
@Controller
@RequiredArgsConstructor
public class KaptchaController {

    private final KaptchaExtend kaptchaExtend;

    @GetMapping(value = "images/rand", produces = "image/jpeg")
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        kaptchaExtend.captcha(request, response);
    }

    @GetMapping(value = "images/rand.jpg")
    public void doGetJpg(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        kaptchaExtend.captcha(request, response);
    }

}
