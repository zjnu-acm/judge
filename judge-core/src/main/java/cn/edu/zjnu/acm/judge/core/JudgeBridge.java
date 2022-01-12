package cn.edu.zjnu.acm.judge.core;

import cn.edu.zjnu.acm.judge.sandbox.win32.WindowsExecutor;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jnc.foreign.Platform;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author zhanhb
 */
@Slf4j
public class JudgeBridge implements Closeable {

    private static final ExecuteResult[] EMPTY = {};
    private final Executor executor;

    public JudgeBridge() {
        if (Platform.getNativePlatform().getOS().isWindows()) {
            executor = new WindowsExecutor();
        } else {
            executor = new UnsupportedExecutor();
        }
    }

    public ExecuteResult[] judge(Option[] options, boolean stopOnError, Validator validator)
            throws IOException {
        // the first case takes much more time than other cases.
        executor.execute(options[0]);
        List<ExecuteResult> list = new ArrayList<>(options.length);
        for (Option option : options) {
            log.debug("prepare execute {}", option);
            ExecuteResult result = executor.execute(option);
            boolean success = result.isSuccess();
            if (success) {
                result = validator.validate(option, result);
                success = result.isSuccess();
            }
            log.info("result:{}", result);
            list.add(result);
            if (stopOnError && !success) {
                break;
            }
        }
        return list.toArray(EMPTY);
    }

    @Override
    public void close() {
        executor.close();
    }

}
