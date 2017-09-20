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
package cn.edu.zjnu.acm.judge.exception;

import java.util.Collections;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author zhanhb
 */
@ControllerAdvice(annotations = Controller.class)
public class GlobalExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(BusinessException.class)
    public ModelAndView handler(BusinessException businessException, Locale locale) {
        BusinessCode code = businessException.getCode();
        HttpStatus status = code.getStatus();
        String message = code.getMessage();
        message = messageSource.getMessage(message, businessException.getParams(), message, locale);
        return new ModelAndView("message", Collections.singletonMap("message", message), status);
    }

    @ExceptionHandler(MessageException.class)
    public ModelAndView messageExceptionHandler(MessageException ex, Locale locale) {
        String message = ex.getMessage();
        HttpStatus status = ex.getHttpStatus();
        message = messageSource.getMessage(message, null, message, locale);
        return new ModelAndView("message", Collections.singletonMap("message", message), status);
    }

}
