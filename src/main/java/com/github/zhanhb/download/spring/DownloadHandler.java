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
package com.github.zhanhb.download.spring;

import com.github.zhanhb.download.Downloader;
import com.github.zhanhb.download.SimpleContentDisposition;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 *
 * @author zhanhb
 */
@Slf4j
public class DownloadHandler implements HandlerMethodReturnValueHandler {

    private final Downloader viewer = Downloader.newInstance()
            .contentDisposition(SimpleContentDisposition.INLINE);
    private final Downloader downloader = Downloader.newInstance()
            .contentDisposition(SimpleContentDisposition.ATTACHMENT);

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return returnType.getMethodAnnotation(ToDownload.class) != null
                && Resource.class.isAssignableFrom(
                        returnType.getMethod().getReturnType());
    }

    @Override
    public void handleReturnValue(Object returnValue,
            MethodParameter returnType,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        ToDownload toDownload = returnType.getMethodAnnotation(ToDownload.class);

        Downloader d = toDownload.attachment() ? downloader : viewer;
        d.service(request, response, Resource.class.cast(returnValue));
        mavContainer.setRequestHandled(true);
    }

}
