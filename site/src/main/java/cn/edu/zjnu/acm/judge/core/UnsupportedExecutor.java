package cn.edu.zjnu.acm.judge.core;

import java.io.IOException;

/**
 * @author zhanhb
 */
public enum UnsupportedExecutor implements Executor {

    INSTANCE;

    @Override
    public ExecuteResult execute(Options options) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() {
    }

}
