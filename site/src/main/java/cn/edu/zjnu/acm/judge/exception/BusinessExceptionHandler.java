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
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author zhanhb
 */
@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class BusinessExceptionHandler {

    private final MessageSource messageSource;
    private final ContentNegotiationStrategy contentNegotiationStrategy;

    @ExceptionHandler
    public Object handler(BusinessException businessException, Locale locale, NativeWebRequest nativeWebRequest)
            throws HttpMediaTypeNotAcceptableException {
        BusinessCode code = businessException.getCode();
        String message = code.getMessage();
        String formatted = messageSource.getMessage(message, businessException.getParams(), message, locale);

        List<MediaType> mediaTypes = contentNegotiationStrategy.resolveMediaTypes(nativeWebRequest);
        log.debug("mediaTypes: {}", mediaTypes);

        for (MediaType mediaType : mediaTypes) {
            if (mediaType.isCompatibleWith(MediaType.APPLICATION_JSON)) {
                ResponseEntity.BodyBuilder builder = ResponseEntity.status(code.getStatus());
                return builder.body(Collections.singletonMap("message", formatted));
            }
            if (mediaType.isCompatibleWith(MediaType.TEXT_HTML)) {
                break;
            }
        }
        return new ModelAndView("message", Collections.singletonMap("message", formatted), code.getStatus());
    }

}
