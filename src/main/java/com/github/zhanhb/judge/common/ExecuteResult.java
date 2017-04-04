package com.github.zhanhb.judge.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@Builder(builderClassName = "Builder", toBuilder = true)
@Getter
@Value
@SuppressWarnings("FinalClass")
public class ExecuteResult {

    private long time;
    private long memory;
    private Status haltCode;
    private int exitCode;
    private String message;

    public boolean isSuccess() {
        return haltCode == null || haltCode == Status.ACCEPTED;
    }

}
