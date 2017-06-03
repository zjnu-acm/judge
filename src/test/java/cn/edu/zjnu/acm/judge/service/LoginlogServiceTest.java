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

import cn.edu.zjnu.acm.judge.Application;
import cn.edu.zjnu.acm.judge.domain.LoginLog;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 *
 * @author zhanhb
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@Slf4j
public class LoginlogServiceTest {

    @Autowired
    private LoginlogService loginlogService;
    private ExecutorService executorService;

    @Before
    public void setUp() {
        executorService = Executors.newFixedThreadPool(100);
    }

    @After
    public void tearDown() throws InterruptedException {
        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        loginlogService.destory();
        loginlogService.getExecutor().awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
    }

    /**
     * Test of save method, of class LoginlogService.
     *
     * @throws java.lang.InterruptedException
     */
    @Test
    public void testSave() throws InterruptedException {
        log.info("save");
        for (int i = 0; i < 10000; ++i) {
            LoginLog loginlog = LoginLog.builder().user(uuid()).password(uuid()).ip("127.0.0.1").build();
            executorService.submit(() -> loginlogService.save(loginlog));
        }
        log.info("finish");
    }

    private String uuid() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

}
