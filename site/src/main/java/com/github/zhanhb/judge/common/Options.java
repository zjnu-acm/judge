package com.github.zhanhb.judge.common;

import java.nio.file.Path;
import lombok.Builder;
import lombok.Value;

@Builder(builderClassName = "Builder")
@Value
@SuppressWarnings("FinalClass")
public class Options {

    private Path inputFile;
    private Path errFile;
    private Path outputFile; // 提交程序的输出文件
    private Path standardOutput; // 标程输出的文件
    private long timeLimit;
    private long memoryLimit;
    private long outputLimit;
    private boolean redirectErrorStream;
    private String command;
    private Path workDirectory;

    @SuppressWarnings("PublicInnerClass")
    public static class Builder {

        Builder() {
            timeLimit = Long.MAX_VALUE;
            memoryLimit = Long.MAX_VALUE;
            outputLimit = Long.MAX_VALUE;
        }

    }

}
