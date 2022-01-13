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
package cn.edu.zjnu.acm.judge.sandbox.win32;

import java.util.Objects;
import jnc.platform.win32.Kernel32;

import static jnc.platform.win32.WinBase.SEM_FAILCRITICALERRORS;
import static jnc.platform.win32.WinBase.SEM_NOGPFAULTERRORBOX;
import static jnc.platform.win32.WinBase.SEM_NOOPENFILEERRORBOX;

/**
 *
 * @author zhanhb
 */
@SuppressWarnings("PublicInnerClass")
public interface ProcessCreationHelper {

    static <V, E extends Throwable> V execute(ExceptionalCallable<V, E> supplier) throws E {
        Objects.requireNonNull(supplier);
        synchronized (java.lang.Runtime.getRuntime()) {
            int oldErrorMode = Kernel32.INSTANCE.SetErrorMode(SEM_FAILCRITICALERRORS | SEM_NOGPFAULTERRORBOX | SEM_NOOPENFILEERRORBOX);
            try {
                return supplier.call();
            } finally {
                Kernel32.INSTANCE.SetErrorMode(oldErrorMode);
            }
        }
    }

    static <E extends Throwable> void execute(ExceptionalRunnable<E> runnable) throws E {
        execute(ExceptionalCallable.fromRunnable(runnable));
    }

    @FunctionalInterface
    interface ExceptionalCallable<V, E extends Throwable> {

        V call() throws E;

        static <V, E extends Throwable> ExceptionalCallable<V, E> fromRunnable(ExceptionalRunnable<E> runnable) {
            Objects.requireNonNull(runnable);
            return () -> {
                runnable.run();
                return null;
            };
        }
    }

    @FunctionalInterface
    interface ExceptionalRunnable<E extends Throwable> {

        void run() throws E;

    }

}
