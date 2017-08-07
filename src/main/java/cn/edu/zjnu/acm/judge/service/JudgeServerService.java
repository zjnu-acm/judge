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

import cn.edu.zjnu.acm.judge.config.JudgeConfiguration;
import cn.edu.zjnu.acm.judge.util.DeleteHelper;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author zhanhb
 */
@Service
public class JudgeServerService {

    @Autowired
    private JudgeConfiguration judgeConfiguration;

    @SuppressWarnings("CallToThreadYield")
    public void delete(Path path) {
        if (!judgeConfiguration.isDeleteTempFile()) {
            return;
        }
        try {
            DeleteHelper.delete(Objects.requireNonNull(path));
        } catch (IOException ignore) {
            // delete again
            System.gc();
            Thread.yield();
            try {
                DeleteHelper.delete(path);
            } catch (IOException ignore2) {
            }
        }
    }

}
