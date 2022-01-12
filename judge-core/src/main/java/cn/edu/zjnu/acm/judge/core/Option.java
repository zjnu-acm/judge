package cn.edu.zjnu.acm.judge.core;

import java.nio.file.Path;
import lombok.Builder;
import lombok.Value;

/**
 * @author zhanhb
 */
@Builder(builderClassName = "Builder")
@Value
@SuppressWarnings("FinalClass")
public class Option {

    Path inputFile;
    Path errFile;
    Path outputFile; // 提交程序的输出文件
    Path standardOutput; // 标程输出的文件
    long timeLimit;
    long memoryLimit;
    long outputLimit;
    boolean redirectErrorStream;
    String command;
    Path workDirectory;

    @SuppressWarnings("PublicInnerClass")
    public static class Builder {

        Builder() {
            timeLimit = Long.MAX_VALUE;
            memoryLimit = Long.MAX_VALUE;
            outputLimit = Long.MAX_VALUE;
        }

    }

}
