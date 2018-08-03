package com.github.zhanhb.judge.common;

import com.github.zhanhb.judge.win32.WindowsExecutor;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author zhanhb
 */
@Slf4j
public class JudgeBridge implements Closeable {

    private final Executor executor = new WindowsExecutor();

    public ExecuteResult[] judge(Options[] optionses, boolean stopOnError, Validator validator)
            throws IOException, JudgeException {
        // the first case takes much more time than other cases.
        executor.execute(optionses[0]);
        List<ExecuteResult> list = new ArrayList<>(optionses.length);
        for (Options options : optionses) {
            log.debug("prepare execute {}", options);
            ExecuteResult result = executor.execute(options);
            boolean success = result.isSuccess();
            if (success) {
                result = validator.validate(options, result);
                success = result.isSuccess();
            }
            log.info("result:{}", result);
            list.add(result);
            if (stopOnError && !success) {
                break;
            }
        }
        return list.toArray(new ExecuteResult[list.size()]);
    }

    @Override
    public void close() {
        executor.close();
    }

}
