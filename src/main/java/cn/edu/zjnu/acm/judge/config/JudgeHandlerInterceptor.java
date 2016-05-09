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
import cn.edu.zjnu.acm.judge.service.UserDetailService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 *
 * @author zhanhb
 */
@RequiredArgsConstructor
@Slf4j
public class JudgeHandlerInterceptor extends HandlerInterceptorAdapter {

    private final MailMapper mailMapper;

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) {
        String url = request.getParameter("url");
        if (StringUtils.isBlank(url)) {
            url = request.getRequestURI();
            String query = request.getQueryString();
            if (query != null) {
                url = url + '?' + query;
            }
        }
        request.setAttribute("backUrl", url);
        UserDetailService.getCurrentUserId(request)
                .map(mailMapper::getMailInfo)
                .ifPresent(mailInfo -> request.setAttribute("mailInfo", mailInfo));
        return true;
    }

}
