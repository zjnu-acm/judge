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
package cn.edu.zjnu.acm.judge.config;

import cn.edu.zjnu.acm.judge.mapper.MailMapper;
import java.util.Optional;
import java.util.function.Function;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author zhanhb
 */
@ControllerAdvice
public class JudgeHandlerInterceptor {

    private static final String APPLIED_ONCE_KEY = JudgeHandlerInterceptor.class.getName().concat(".APPLIED_ONCE");
    public static final String BACK_URL_ATTRIBUTE_NAME = "backUrl";

    private static String getString(String attributeName, Function<HttpServletRequest, String> supplier, HttpServletRequest request) {
        return Optional.ofNullable((String) request.getAttribute(attributeName)).orElseGet(() -> supplier.apply(request));
    }

    @Autowired
    private MailMapper mailMapper;

    @ModelAttribute
    public void addAttributes(HttpServletRequest request,
            @RequestParam(value = "url", required = false) String url,
            Authentication authentication) {
        if (Boolean.TRUE.equals(request.getAttribute(APPLIED_ONCE_KEY))) {
            return;
        }
        request.setAttribute(APPLIED_ONCE_KEY, true);

        if (StringUtils.hasText(url)) {
            request.setAttribute(BACK_URL_ATTRIBUTE_NAME, url);
        } else {
            String uri = getString(RequestDispatcher.FORWARD_SERVLET_PATH, HttpServletRequest::getServletPath, request);
            String query = getString(RequestDispatcher.FORWARD_QUERY_STRING, HttpServletRequest::getQueryString, request);
            if (query != null) {
                uri = uri + '?' + query;
            }
            request.setAttribute(BACK_URL_ATTRIBUTE_NAME, uri);
        }
        Optional.ofNullable(authentication).map(Authentication::getName)
                .map(mailMapper::getMailInfo)
                .ifPresent(mailInfo -> request.setAttribute("mailInfo", mailInfo));
    }

}
