/*
 * Copyright 2019 ZJNU ACM.
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
package jnc.platform.win32;

import java.io.Closeable;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import jnc.foreign.Platform;

/**
 *
 * @author zhanhb
 */
public class Handle implements Closeable {

    private static final long INVALID_HANDLE_VALUE = BigInteger.ONE.shiftLeft(Platform.getNativePlatform().getArch().sizeOfPointer() << 3).subtract(BigInteger.ONE).longValue();
    public static final Handle INVALID_HANDLE = new Handle();

    private static final AtomicIntegerFieldUpdater<Handle> CLOSED_FIELD_UPDATER
            = AtomicIntegerFieldUpdater.newUpdater(Handle.class, "closed");

    public static long validateHandle(long /*HANDLE*/ handle) {
        if (handle == 0) {
            throw new NullPointerException("handle");
        }
        long v = handle & INVALID_HANDLE_VALUE;
        if (v == INVALID_HANDLE_VALUE) {
            throw new IllegalArgumentException("invalid handle value");
        }
        return v;
    }

    public static Handle of(long value) {
        return new Handle(value);
    }

    public static void close(long value) {
        close0(validateHandle(value));
    }

    private static void close0(long value) {
        Kernel32Util.assertTrue(Kernel32.INSTANCE.CloseHandle(value));
    }

    private final long value;
    @SuppressWarnings("unused")
    private volatile int closed;

    private Handle() {
        this.value = INVALID_HANDLE_VALUE;
        this.closed = 1;
    }

    protected Handle(long value) {
        validateHandle(value);
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    @Override
    public void close() {
        if (CLOSED_FIELD_UPDATER.compareAndSet(this, 0, 1)) {
            // value will never be NULL or INVALID_HANDLE_VALUE
            close0(value);
        }
    }

    @Override
    public String toString() {
        return Long.toHexString(getValue());
    }

    @Override
    public int hashCode() {
        return Long.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Handle)) {
            return false;
        }
        final Handle other = (Handle) obj;
        return this.getValue() == other.getValue();
    }

}
