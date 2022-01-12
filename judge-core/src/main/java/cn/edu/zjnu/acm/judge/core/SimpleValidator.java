package cn.edu.zjnu.acm.judge.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Scanner;

/**
 * @author zhanhb
 */
public enum SimpleValidator implements Validator {
    NORMAL {
        @Override
        public Status validate(Path input, Path standard, Path output)
                throws IOException {
            try {
                if (isAccepted(standard, output)) {
                    return Status.ACCEPTED;
                }
                if (atLeastPe(standard, output)) {
                    return Status.PRESENTATION_ERROR;
                }
            } catch (OutOfMemoryError ignored) {
            }
            return Status.WRONG_ANSWER;
        }
    },
    PE_AS_AC {
        @Override
        public Status validate(Path input, Path standard, Path output)
                throws IOException {
            try {
                if (atLeastPe(standard, output)) {
                    return Status.ACCEPTED;
                }
            } catch (OutOfMemoryError ignored) {
            }
            return Status.WRONG_ANSWER;
        }
    };

    boolean isAccepted(Path standard, Path output) throws IOException {
        try (BufferedReader std = Files.newBufferedReader(standard, StandardCharsets.ISO_8859_1);
                BufferedReader out = Files.newBufferedReader(output, StandardCharsets.ISO_8859_1)) {
            while (true) {
                String cmp = std.readLine();
                String line = out.readLine();
                if (cmp == null && line == null) {
                    return true;
                } else if (!Objects.equals(cmp, line)) {
                    return false;
                }
            }
        }
    }

    boolean atLeastPe(Path standard, Path output) throws IOException {
        try (BufferedReader brStd = Files.newBufferedReader(standard, StandardCharsets.ISO_8859_1);
                BufferedReader brOut = Files.newBufferedReader(output, StandardCharsets.ISO_8859_1);
                Scanner std = new Scanner(brStd);
                Scanner out = new Scanner(brOut)) {
            while (std.hasNext()) {
                if (!out.hasNext() || !std.next().equals(out.next())) {
                    return false;
                }
            }
            return !out.hasNext();
        }
    }

    @Override
    public abstract Status validate(Path input, Path standard, Path output)
            throws IOException;

}
