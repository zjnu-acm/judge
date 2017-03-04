package com.github.zhanhb.judge.common;

import java.util.Optional;
import lombok.Getter;

public enum Status {

    accepted(0), presentationError(1), timeLimitExceed(2), memoryLimitExceed(3),
    wrongAnswer(4), runtimeError(5), outputLimitExceed(6), compilationError(7),
    nonZeroExitCode(8, "Non-zero Exit Code"), floatPointError(9), segmentationFault(10),
    outOfContestTime(11, "Out of Contest Time"),
    judgeInternalError(12), restrictedFunction(13), noSuchProblem(14),
    submissionLimitExceeded(15),
    queuing(1000), compiling(1001), running(1002), validating(1003), judging(1004), pendingRejudge(1005);

    @Getter
    private final boolean finalResult;
    private final String toString;
    @Getter
    private final int result;

    Status(int result) {
        this(result, null);
    }

    Status(int result, String toString) {
        this.result = result;
        this.finalResult = ordinal() < 1000;
        this.toString = Optional.ofNullable(toString).orElseGet(()
                -> name().substring(0, 1).toUpperCase().concat(
                        name().substring(1).replaceAll("[A-Z]", " $0").trim() //NOI18N
                ));
    }

    @Override
    public String toString() {
        return toString;
    }

}
