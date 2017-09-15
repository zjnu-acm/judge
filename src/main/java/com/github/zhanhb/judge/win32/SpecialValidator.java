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

import cn.edu.zjnu.acm.judge.service.MyPrintWriter;
import com.github.zhanhb.judge.common.JudgeException;
import com.github.zhanhb.judge.common.Status;
import com.github.zhanhb.judge.common.Validator;
import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author zhanhb
 */
@Slf4j
public class SpecialValidator implements Validator {

    private final String command;
    private final File dir;

    public SpecialValidator(String command, Path dir) {
        this.command = Objects.requireNonNull(command);
        this.dir = dir.toFile();
    }

    @Override
    public Status validate(Path inputFile, Path standardOutput, Path outputFile)
            throws IOException, JudgeException {
        log.debug("use special judge '{}'", command);
        Process specialJudge = ProcessCreationHelper.execute(() -> Runtime.getRuntime().exec(command, null, dir));
        try (PrintWriter specialOut = new MyPrintWriter(specialJudge.getOutputStream())) {
            log.debug("{}", inputFile);
            specialOut.println(inputFile);
            log.debug("{}", standardOutput);
            specialOut.println(standardOutput);
            log.debug("{}", outputFile);
            specialOut.println(outputFile);
        }
        try {
            int specialExitValue = specialJudge.waitFor();
            log.debug("specialExitValue = {}", specialExitValue);
            if (specialExitValue == 0) {
                return Status.ACCEPTED;
            } else if (specialExitValue < 0) {
                return Status.PRESENTATION_ERROR;
            } else {
                return Status.WRONG_ANSWER;
            }
        } catch (InterruptedException ex) {
            throw new InterruptedIOException();
        }
    }

}
