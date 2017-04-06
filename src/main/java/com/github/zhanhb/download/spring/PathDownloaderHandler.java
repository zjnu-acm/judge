/*
 * Copyright 2016 zhanhb.
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

import com.github.zhanhb.ckfinder.download.ContentDisposition;
import com.github.zhanhb.ckfinder.download.PathPartial;
import java.nio.file.Path;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 *
 * @author zhanhb
 */
public class PathDownloaderHandler implements HandlerMethodReturnValueHandler {

    private final PathPartial viewer = PathPartial.builder()
            .contentDisposition(ContentDisposition.inline())
            .build();
    private final PathPartial downloader = PathPartial.builder()
            .contentDisposition(ContentDisposition.attachment())
            .build();

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return returnType.getMethodAnnotation(ToDownload.class) != null
                && Path.class.isAssignableFrom(
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

        PathPartial d = toDownload.attachment() ? downloader : viewer;
        d.service(request, response, Path.class.cast(returnValue));
        mavContainer.setRequestHandled(true);
    }

}
