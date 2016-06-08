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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.LocaleResolver;

@ControllerAdvice
public class GlobalExceptionHandler {

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
        } catch (NoSuchMessageException exception) {
        }
        request.setAttribute("message", message);
        if (code.is4xxClientError()) {
            response.setStatus(code.value());
        }
        return "message";
    }

    @ExceptionHandler(ForbiddenException.class)
    public String forbiddenExceptionHandler(HttpServletRequest request)
            throws UnsupportedEncodingException {
        return "redirect:/login?url=" + URLEncoder.encode((String) request.getAttribute("backUrl"), "UTF-8");
    }

}
