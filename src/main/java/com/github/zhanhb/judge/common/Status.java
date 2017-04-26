package com.github.zhanhb.judge.common;

import cn.edu.zjnu.acm.judge.util.MatcherWrapper;
import lombok.Getter;

public enum Status {

    ACCEPTED(0), PRESENTATION_ERROR(1), TIME_LIMIT_EXCEED(2), MEMORY_LIMIT_EXCEED(3),
    WRONG_ANSWER(4), RUNTIME_ERROR(5), OUTPUT_LIMIT_EXCEED(6), COMPILATION_ERROR(7),
    NON_ZERO_EXIT_CODE(8, "Non-zero Exit Code"), FLOATING_POINT_ERROR(9), SEGMENTATION_FAULT(10),
    OUT_OF_CONTEST_TIME(11, "Out of Contest Time"),
    JUDGE_INTERNAL_ERROR(12), RESTRICTED_FUNCTION(13), NO_SUCH_PROBLEM(14),
    SUBMISSION_LIMIT_EXCEED(15),
    QUEUING(1000), COMPILING(1001), RUNNING(1002), VALIDATING(1003), JUDGING(1004), PENDING_REJUDGE(1005);

    @Getter
    private final boolean finalResult;
    private final String toString;
    @Getter
    private final int result;

    Status(int result) {
        this.result = result;
        this.finalResult = ordinal() < 1000;
        this.toString = MatcherWrapper.matcher("(^|_)(?i)([A-Z]+)", name()).replaceAll(matcher -> {
            String prefix = matcher.group(1);
            String partial = matcher.group(2);
            return (prefix.isEmpty() ? "" : " ") + partial.substring(0, 1) + partial.substring(1).toLowerCase();
        });
    }

    Status(int result, String toString) {
        this.result = result;
        this.finalResult = ordinal() < 1000;
        this.toString = toString;
    }

    @Override
    public String toString() {
        return toString;
    }

}
