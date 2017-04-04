package com.github.zhanhb.judge.impl;

import com.github.zhanhb.judge.common.JudgeException;
import com.github.zhanhb.judge.common.Status;
import com.github.zhanhb.judge.common.Validator;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class SimpleValidator implements Validator {

    private boolean isPresentationError(Path standardFile, Path answerFile) throws IOException {
        try (BufferedReader outr = Files.newBufferedReader(standardFile, StandardCharsets.ISO_8859_1);
                BufferedReader ansr = Files.newBufferedReader(answerFile, StandardCharsets.ISO_8859_1);
                Scanner out = new Scanner(outr);
                Scanner ans = new Scanner(ansr)) {
            while (out.hasNext()) {
                if (!ans.hasNext()) {
                    return false;
                }
                if (!out.next().equals(ans.next())) {
                    return false;
                }
            }
            return !ans.hasNext();
        }
    }

    @Override
    public Status validate(Path inputFile, Path outputFile, Path answerFile)
            throws IOException, JudgeException {
        try {
            if (isPresentationError(outputFile, answerFile)) {
                return Status.ACCEPTED;
            }
        } catch (OutOfMemoryError ignored) {
        }
        return Status.WRONG_ANSWER;
    }

}
