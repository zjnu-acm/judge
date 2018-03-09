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
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhanhb
 */
@Service
public class LoginlogService {

    @Autowired
    private LoginlogMapper loginlogMapper;
    private ExecutorService executorService;
    private final BlockingQueue<LoginLog> list = new ArrayBlockingQueue<>(200);

    @PostConstruct
    public void init() {
        executorService = Executors.newSingleThreadExecutor();
    }

    @PreDestroy
    public void destroy() {
        executorService.shutdown();
    }

    public void save(LoginLog loginlog) {
        Objects.requireNonNull(loginlog);
        try {
            list.put(loginlog);
            executorService.submit(this::saveBatch);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    private void saveBatch() {
        List<LoginLog> tmp = new ArrayList<>(list.size());
        if (list.drainTo(tmp) > 0) {
            loginlogMapper.save(tmp);
        }
    }

    void await() throws InterruptedException {
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
    }

}
