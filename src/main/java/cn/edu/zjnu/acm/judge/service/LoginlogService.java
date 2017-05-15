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
package cn.edu.zjnu.acm.judge.service;

import cn.edu.zjnu.acm.judge.domain.LoginLog;
import cn.edu.zjnu.acm.judge.mapper.LoginlogMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhanhb
 */
@Service
@Slf4j
public class LoginlogService {

    @Autowired
    private LoginlogMapper loginlogMapper;
    private Thread thread;
    private final ArrayBlockingQueue<LoginLog> queue = new ArrayBlockingQueue<>(4096);

    @PostConstruct
    public void init() {
        thread = new Thread(this::savebatch);
        thread.start();
    }

    @PreDestroy
    public void destory() {
        log.info("interrupt");
        thread.interrupt();
    }

    @Transactional
    public void save(LoginLog loginlog) {
        queue.add(loginlog);
    }

    private void savebatch() {
        List<LoginLog> arrayList = new ArrayList<>(4);
        while (true) {
            try {
                arrayList.add(queue.take());
            } catch (InterruptedException ex) {
                if (!queue.isEmpty()) {
                    Thread.currentThread().interrupt();
                    break;
                }
                String msg = "finish save log";
                if (log.isDebugEnabled()) {
                    log.debug(msg, ex);
                } else {
                    log.info(msg);
                }
                return;
            }
            queue.drainTo(arrayList);
            LoginLog[] array = arrayList.toArray(new LoginLog[arrayList.size()]);
            arrayList.clear();
            try {
                loginlogMapper.save(array);
            } catch (RuntimeException ex) {
                log.error("save login log", ex);
            }
        }
    }

    void await() throws InterruptedException {
        destory();
        thread.join();
    }

}
