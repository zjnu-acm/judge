package cn.edu.zjnu.acm.judge.core;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

/**
 * @author zhanhb
 */
public interface Validator {

    /**
     *
     * @param inputFile
     * @param standardOutput
     * @param outputFile
     * @return
     * @throws java.io.IOException
     * @see Status#ACCEPTED
     * @see Status#PRESENTATION_ERROR
     * @see Status#WRONG_ANSWER
     */
    Status validate(Path inputFile, Path standardOutput, Path outputFile)
            throws IOException;

    default ExecuteResult validate(Option option, ExecuteResult executeResult)
            throws IOException {
        Objects.requireNonNull(executeResult);
        Status validate = validate(option.getInputFile(),
                option.getStandardOutput(),
                option.getOutputFile());
        return executeResult.toBuilder().code(validate).build();
    }

}
