/*
 * Copyright 2014 zhanhb.
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
package cn.edu.zjnu.acm.judge.exception;

import cn.edu.zjnu.acm.judge.config.JudgeHandlerInterceptor;
import java.io.IOException;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.LocaleResolver;

@ControllerAdvice
@Order(0)
public class GlobalExceptionHandler {

    public static String unauthorized(HttpServletRequest request) throws IOException {
        String url = (String) request.getAttribute(JudgeHandlerInterceptor.BACK_URL_ATTRIBUTE_NAME);
        return url == null ? "redirect:/login" : "redirect:/login?url=" + URLEncoder.encode(url, "UTF-8");
    }

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private LocaleResolver localeResolver;

    @ExceptionHandler(MessageException.class)
    public String messageExceptionHandler(HttpServletRequest request, HttpServletResponse response, MessageException ex) {
        String message = ex.getMessage();
        HttpStatus code = ex.getHttpStatus();
        try {
            message = messageSource.getMessage(message, null, localeResolver.resolveLocale(request));
        } catch (NoSuchMessageException ignore) {
        }
        request.setAttribute("message", message);
        if (code.is4xxClientError()) {
            response.setStatus(code.value());
        }
        return "message";
    }

    @ExceptionHandler(ForbiddenException.class)
    public String forbiddenExceptionHandler(HttpServletRequest request) throws IOException {
        return unauthorized(request);
    }

}
