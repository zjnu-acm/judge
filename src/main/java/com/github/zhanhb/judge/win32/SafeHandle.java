package com.github.zhanhb.judge.win32;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import java.io.Closeable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class SafeHandle implements Closeable {

    public static void close(HANDLE handle) {
        if (handle != null && !WinBase.INVALID_HANDLE_VALUE.equals(handle)) {
            Kernel32Util.assertTrue(Kernel32.INSTANCE.CloseHandle(handle));
        }
    }

    private final HANDLE handle;
    private final AtomicBoolean closed = new AtomicBoolean();

    public SafeHandle(HANDLE handle) {
        if (WinBase.INVALID_HANDLE_VALUE.equals(Objects.requireNonNull(handle))) {
            throw new IllegalArgumentException("invalid handle value");
        }
        this.handle = handle;
    }

    public HANDLE getValue() {
        return handle;
    }

    @Override
    public void close() {
        if (closed.compareAndSet(false, true)) {
            close(handle);
        }
    }

    @Override
    public String toString() {
        return String.valueOf(getValue());
    }

}
