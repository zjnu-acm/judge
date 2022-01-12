package cn.edu.zjnu.acm.judge.core;

import java.io.IOException;

/**
 * @author zhanhb
 */
public class UnsupportedExecutor implements Executor {

    @Override
    public ExecuteResult execute(Option option) throws IOException {
        throw new UnsupportedOperationException();
    }

}
