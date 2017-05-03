package com.github.zhanhb.judge.classic;

import com.github.zhanhb.judge.common.ExecuteResult;
import com.github.zhanhb.judge.common.JudgeException;
import com.github.zhanhb.judge.common.Options;
import com.github.zhanhb.judge.common.Validator;
import com.github.zhanhb.judge.win32.Executor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author zhanhb
 */
@Slf4j
public enum JudgeBridge {
    INSTANCE;

    public ExecuteResult[] execute(Options[] optionses, boolean stopOnError, Validator validator)
            throws IOException, JudgeException {
        // the first case takes much more time than other cases.
        Executor.INSTANCE.execute(optionses[0]);
        List<ExecuteResult> list = new ArrayList<>(optionses.length);
        for (Options options : optionses) {
            log.debug("prepare execute {}", options);
            ExecuteResult result = Executor.INSTANCE.execute(options);
            boolean success = result.isSuccess();
            if (success) {
                result = validator.validate(options, result);
                success = result.isSuccess();
            }
            list.add(result);
            if (stopOnError && !success) {
                break;
            }
        }
        return list.toArray(new ExecuteResult[list.size()]);
    }

}
