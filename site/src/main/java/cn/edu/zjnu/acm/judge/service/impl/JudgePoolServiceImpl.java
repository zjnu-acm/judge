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
package cn.edu.zjnu.acm.judge.service.impl;

import cn.edu.zjnu.acm.judge.service.JudgePoolService;
import cn.edu.zjnu.acm.judge.service.JudgeService;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author zhanhb
 */
@RequiredArgsConstructor
@Service("judgePoolService")
@Slf4j
public class JudgePoolServiceImpl implements JudgePoolService {

    private final JudgeService judgeService;
    private ExecutorService executorService;

    @PostConstruct
    public void init() {
        final ThreadGroup group = new ThreadGroup("judge group");
        final AtomicInteger counter = new AtomicInteger();
        final ThreadFactory threadFactory = runnable -> new Thread(group, runnable, "judge thread " + counter.incrementAndGet());
        int nThreads = Runtime.getRuntime().availableProcessors();
        executorService = new LogThreadPoolExecutor(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                threadFactory);
    }

    @PreDestroy
    public void destroy() {
        executorService.shutdownNow();
    }

    @Override
    public CompletableFuture<?> add(long id) {
        return CompletableFuture.runAsync(() -> judgeService.execute(id), executorService);
    }

    @Override
    public CompletableFuture<?> addAll(long... id) {
        return CompletableFuture.allOf(Arrays.stream(id).mapToObj(this::add).toArray(CompletableFuture[]::new));
    }

}
