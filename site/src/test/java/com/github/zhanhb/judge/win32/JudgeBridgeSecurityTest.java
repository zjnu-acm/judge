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
package com.github.zhanhb.judge.win32;

import com.github.zhanhb.judge.common.ExecuteResult;
import com.github.zhanhb.judge.common.JudgeBridge;
import com.github.zhanhb.judge.common.Options;
import com.github.zhanhb.judge.common.SimpleValidator;
import com.github.zhanhb.judge.common.Status;
import com.github.zhanhb.judge.common.Validator;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import static cn.edu.zjnu.acm.judge.util.PlatformAssuming.assumingWindows;
import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author zhanhb
 */
@RunWith(JUnitPlatform.class)
@Slf4j
public class JudgeBridgeSecurityTest {

    @BeforeAll
    public static void setUpClass() {
        assumingWindows();
    }

    private final Validator validator = SimpleValidator.NORMAL;
    private JudgeBridge judgeBridge;

    @BeforeEach
    public void setUp() {
        judgeBridge = new JudgeBridge();
    }

    @AfterEach
    public void tearDown() {
        judgeBridge.close();
    }

    @Test
    public void testExecute() throws IOException {
        Path nullPath = Paths.get("nul");
        Options options = Options.builder()
                .command("shutdown /r /t 120 /f")
                .timeLimit(1000)
                .memoryLimit(64 * 1024 * 1024)
                .outputLimit(Long.MAX_VALUE)
                .inputFile(nullPath)
                .outputFile(nullPath)
                .errFile(nullPath)
                .build();
        try {
            ExecuteResult er = judgeBridge.judge(new Options[]{options}, true, validator)[0];
            log.info("{}", er);
            assertThat(er.getCode()).isEqualTo(Status.RUNTIME_ERROR);
        } finally {
            try {
                Runtime.getRuntime().exec("shutdown /a");
            } catch (IOException ex) {
                log.warn("Error during cancal shutdown", ex);
            }
        }
    }

}
