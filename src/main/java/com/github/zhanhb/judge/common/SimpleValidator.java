package com.github.zhanhb.judge.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Scanner;

public enum SimpleValidator implements Validator {
    NORMAL {
        @Override
        public Status validate(Path inputFile, Path outputFile, Path answerFile)
                throws IOException, JudgeException {
            try {
                if (isAccepted(outputFile, answerFile)) {
                    return Status.ACCEPTED;
                }
                if (isPresentationError(outputFile, answerFile)) {
                    return Status.PRESENTATION_ERROR;
                }
            } catch (OutOfMemoryError ignored) {
            }
            return Status.WRONG_ANSWER;
        }
    },
    PE_AS_ACCEPTED {
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
    };

    boolean isAccepted(Path standardFile, Path answerFile) throws IOException {
        try (BufferedReader outr = Files.newBufferedReader(standardFile, StandardCharsets.ISO_8859_1);
                BufferedReader ansr = Files.newBufferedReader(answerFile, StandardCharsets.ISO_8859_1)) {
            while (true) {
                String linea = outr.readLine();
                String lineb = ansr.readLine();
                if (linea == null && lineb == null) {
                    return true;
                } else if (!Objects.equals(linea, lineb)) {
                    return false;
                }
            }
        }
    }

    boolean isPresentationError(Path standardFile, Path answerFile) throws IOException {
        try (BufferedReader outr = Files.newBufferedReader(standardFile, StandardCharsets.ISO_8859_1);
                BufferedReader ansr = Files.newBufferedReader(answerFile, StandardCharsets.ISO_8859_1);
                Scanner out = new Scanner(outr);
                Scanner ans = new Scanner(ansr)) {
            while (out.hasNext()) {
                if (!ans.hasNext() || !out.next().equals(ans.next())) {
                    return false;
                }
            }
            return !ans.hasNext();
        }
    }

    @Override
    public abstract Status validate(Path inputFile, Path standardOutput, Path outputFile)
            throws IOException, JudgeException;

}
