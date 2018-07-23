/*
 * Copyright 2018 ZJNU ACM.
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

import cn.edu.zjnu.acm.judge.util.DeleteHelper;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.stereotype.Service;

/**
 *
 * @author zhanhb
 */
@Service
public class DeleteService {

    private ScheduledExecutorService pool;

    @PostConstruct
    public void init() {
        pool = Executors.newScheduledThreadPool(1);
    }

    @PreDestroy
    public void shutdown() {
        pool.shutdown();
    }

    private void delete0(Path path) {
        try {
            DeleteHelper.delete(path);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public ScheduledFuture<?> delete(Path path) {
        ScheduledFuture<?>[] array = {null};
        Runnable r = () -> {
            try {
                delete0(path);
            } catch (UncheckedIOException ex) {
                return;
            }
            array[0].cancel(false);
        };
        array[0] = pool.scheduleAtFixedRate(r, 0, 5, TimeUnit.SECONDS);
        return array[0];
    }

}
