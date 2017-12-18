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
import com.github.zhanhb.judge.common.JudgeException;
import com.github.zhanhb.judge.common.Options;
import com.github.zhanhb.judge.common.SimpleValidator;
import com.github.zhanhb.judge.common.Status;
import com.github.zhanhb.judge.common.Validator;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import jnc.foreign.Platform;
import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

/**
 *
 * @author zhanhb
 */
@Slf4j
public class JudgeBridgeSecurityTest {

    @BeforeClass
    public static void setUpClass() {
        assumeTrue("not windows", Platform.getNativePlatform().getOS().isWindows());
    }

    private final Validator validator = SimpleValidator.NORMAL;

    @Test
    public void testExecute() throws IOException, JudgeException {
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
            ExecuteResult er = JudgeBridge.INSTANCE.judge(new Options[]{options}, true, validator)[0];
            log.info("{}", er);
            assertEquals(Status.RUNTIME_ERROR, er.getCode());
        } finally {
            try {
                Runtime.getRuntime().exec("shutdown /a");
            } catch (IOException ex) {
                log.warn("Error during cancal shutdown", ex);
            }
        }
    }

}
