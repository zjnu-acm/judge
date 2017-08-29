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

import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 *
 * @author zhanhb
 */
@ControllerAdvice
@Order
public class BusinessExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handler(BusinessException businessException, Locale locale) {
        ResponseEntity.BodyBuilder builder;
        BusinessCode code = businessException.getCode();
        switch (code) {
            case NOT_FOUND:
                builder = ResponseEntity.status(HttpStatus.NOT_FOUND);
                break;
            case UNAUTHORIZED:
                builder = ResponseEntity.status(HttpStatus.UNAUTHORIZED);
                break;
            case FORBIDDEN:
                builder = ResponseEntity.status(HttpStatus.FORBIDDEN);
                break;
            default:
                builder = ResponseEntity.badRequest();
                break;
        }
        String message = code.getMessage();
        return builder.body(new GenericExceptionBean(messageSource.getMessage(message, null, message, locale)));
    }

}
