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
package cn.edu.zjnu.acm.judge.service;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author zhanhb
 */
@Service
@Slf4j
public class JudgePool {

    private ExecutorService executorService;
    @Autowired
    private JudgeService judger;

    @PostConstruct
    public void init() {
        final ThreadGroup group = new ThreadGroup("judge group");
        final AtomicInteger counter = new AtomicInteger();
        final ThreadFactory threadFactory = runnable -> new Thread(group, runnable, "judge thread " + counter.incrementAndGet());
        int nThreads = Runtime.getRuntime().availableProcessors();
        executorService = new ThreadPoolExecutor(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                threadFactory) {

            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                if (t != null) {
                    log.error("{}", r, t);
                }
            }

        };
    }

    @PreDestroy
    public void destory() {
        executorService.shutdownNow();
    }

    public CompletableFuture<?> add(long id) {
        return judger.toCompletableFuture(executorService, id);
    }

    public CompletableFuture<?> addAll(long... id) {
        return CompletableFuture.allOf(Arrays.stream(id).mapToObj(this::add).toArray(CompletableFuture[]::new));
    }

}
