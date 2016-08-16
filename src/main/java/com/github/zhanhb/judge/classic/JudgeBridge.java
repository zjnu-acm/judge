package com.github.zhanhb.judge.classic;

import com.github.zhanhb.judge.common.ExecuteResult;
import com.github.zhanhb.judge.common.JudgeException;
import com.github.zhanhb.judge.common.Options;
import com.github.zhanhb.judge.common.Validator;
import com.github.zhanhb.judge.win32.Executor;
import java.io.IOException;
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
        ExecuteResult[] ers = new ExecuteResult[optionses.length];
        for (int i = 0; i < optionses.length; ++i) {
            log.debug("prepare execute {}", optionses[i]);
            ers[i] = Executor.INSTANCE.execute(optionses[i]);
            boolean success = ers[i].isSuccess();
            if (stopOnError && !success) {
                break;
            }
            if (!success) {
                continue;
            }
            ers[i] = validator.validate(optionses[i], ers[i]);
            success = ers[i].isSuccess();
            if (stopOnError && !success) {
                break;
            }
        }
        return ers;
    }

}
