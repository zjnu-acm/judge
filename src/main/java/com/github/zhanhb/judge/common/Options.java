package com.github.zhanhb.judge.common;

import java.nio.file.Path;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@Builder
@Getter
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

}
