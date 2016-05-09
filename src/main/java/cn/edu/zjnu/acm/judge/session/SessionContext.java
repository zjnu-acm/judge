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
package cn.edu.zjnu.acm.judge.session;

import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author zhanhb
 */
@Service
@Slf4j
public class SessionContext {

    private final ConcurrentHashMap<String, HttpSession> map = new ConcurrentHashMap<>(40);

    public void addSession(HttpSession session) {
        map.put(session.getId(), session);
    }

    public void removeSession(HttpSession session) {
        map.remove(session.getId());
    }

    public void check(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && request.isRequestedSessionIdValid()) {
            addSession(session);
        }
    }

}
