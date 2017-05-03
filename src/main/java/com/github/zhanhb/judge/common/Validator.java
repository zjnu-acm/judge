package com.github.zhanhb.judge.common;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public interface Validator {

    /**
     *
     * @param inputFile
     * @param standardOutput
     * @param outputFile
     * @return
     * @throws java.io.IOException
     * @throws com.github.zhanhb.judge.common.JudgeException
     * @see Status#ACCEPTED
     * @see Status#PRESENTATION_ERROR
     * @see Status#WRONG_ANSWER
     */
    Status validate(Path inputFile, Path standardOutput, Path outputFile)
            throws IOException, JudgeException;

    default ExecuteResult validate(Options options, ExecuteResult executeResult)
            throws IOException, JudgeException {
        Objects.requireNonNull(executeResult);
        Status validate = validate(options.getInputFile(),
                options.getStandardOutput(),
                options.getOutputFile());
        return executeResult.toBuilder().code(validate).build();
    }

}
