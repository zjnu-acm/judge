/*
 * Copyright 2016 ZJNU ACM.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.zhanhb.judge.win32;

import java.util.Objects;

import static com.github.zhanhb.judge.win32.Kernel32.SEM_NOGPFAULTERRORBOX;

/**
 *
 * @author zhanhb
 */
@SuppressWarnings("PublicInnerClass")
public class ProcessCreationHelper {

    private static Object getLock() {
        return Runtime.getRuntime();
    }

    public static <V, E extends Throwable> V execute(ExceptionCallable<V, E> supplier) throws E {
        Objects.requireNonNull(supplier);
        synchronized (getLock()) {
            int oldErrorMode = Kernel32.INSTANCE.SetErrorMode(SEM_NOGPFAULTERRORBOX);
            try {
                return supplier.call();
            } finally {
                Kernel32.INSTANCE.SetErrorMode(oldErrorMode);
            }
        }
    }

    public static <E extends Throwable> void execute(ExceptionRunnable<E> supplier) throws E {
        execute(ExceptionCallable.wrapper(supplier));
    }

    private ProcessCreationHelper() {
        throw new AssertionError();
    }

    public static interface ExceptionCallable<V, E extends Throwable> {

        V call() throws E;

        static <V, E extends Throwable> ExceptionCallable<V, E>
                wrapper(ExceptionRunnable<E> runnable) {
            Objects.requireNonNull(runnable);
            return () -> {
                runnable.run();
                return null;
            };
        }

    }

    public static interface ExceptionRunnable<E extends Throwable> {

        void run() throws E;

    }

}
