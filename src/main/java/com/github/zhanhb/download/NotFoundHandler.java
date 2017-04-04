/*
 * Copyright 2017 zhanhb.
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
package com.github.zhanhb.download;

import java.io.FileNotFoundException;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author zhanhb
 */
public interface NotFoundHandler {

    public static NotFoundHandler defaultHandler() {
        return context -> {
            HttpServletRequest request = context.get(HttpServletRequest.class);
            HttpServletResponse response = context.get(HttpServletResponse.class);
            // Check if we're included so we can return the appropriate
            // missing resource name in the error
            String requestUri = (String) request.getAttribute(RequestDispatcher.INCLUDE_REQUEST_URI);
            if (requestUri != null) {
                // We're included
                // SRV.9.3 says we must throw a FNFE
                throw new FileNotFoundException("The requested resource (" + requestUri + ") is not available");
            }
            response.sendError(HttpServletResponse.SC_NOT_FOUND, request.getRequestURI());
        };
    }

    void handle(ActionContext context) throws IOException, ServletException;

}
