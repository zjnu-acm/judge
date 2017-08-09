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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
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
    private ExecutorService executorService;
    private final List<LoginLog> list = new ArrayList<>(4);
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    @PostConstruct
    public void init() {
        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(this::saveBatch);
        executorService.shutdown();
    }

    @PreDestroy
    public void destroy() {
        executorService.shutdownNow();
    }

    @Transactional
    public void save(LoginLog loginlog) {
        Objects.requireNonNull(loginlog);
        lock.lock();
        try {
            list.add(loginlog);
            condition.signal();
        } finally {
            lock.unlock();
        }
    }

    private void saveBatch() {
        while (true) {
            LoginLog[] array;
            try {
                lock.lock();
                try {
                    while (list.isEmpty()) {
                        condition.await();
                    }
                    array = list.toArray(new LoginLog[list.size()]);
                    list.clear();
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException ex) {
                log.info("finish save log");
                return;
            }
            try {
                loginlogMapper.save(array);
            } catch (RuntimeException ex) {
                log.error("save login log", ex);
            }
        }
    }

    ExecutorService getExecutor()  {
        return executorService;
    }

}
