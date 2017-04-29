package com.github.zhanhb.judge.common;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder(builderClassName = "Builder", toBuilder = true)
@Value
@SuppressWarnings("FinalClass")
public class ExecuteResult {

    private long time;
    private long memory;
    @NonNull
    private Status haltCode;
    private int exitCode;
    private String message;

    public boolean isSuccess() {
        return haltCode == Status.ACCEPTED;
    }

}
