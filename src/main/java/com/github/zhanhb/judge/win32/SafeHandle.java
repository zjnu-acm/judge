package com.github.zhanhb.judge.win32;

import com.google.common.base.Preconditions;
import java.io.Closeable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import jnr.ffi.Pointer;

public class SafeHandle implements Closeable {

    static final jnr.ffi.Runtime runtime = jnr.ffi.Runtime.getSystemRuntime();
    private static final long INVALID_HANDLE_VALUE = runtime.addressMask();

    public static void validateHandle(Pointer /*HANDLE*/ handle) {
        Preconditions.checkArgument(handle != null && handle.address() != INVALID_HANDLE_VALUE, "invalid handle value");
    }

    public static void close(Pointer /*HANDLE*/ handle) {
        try {
            validateHandle(handle);
        } catch (IllegalArgumentException ex) {
            return;
        }
        Kernel32Util.assertTrue(Kernel32.INSTANCE.CloseHandle(handle));
    }

    private final Pointer /*HANDLE*/ handle;
    private final AtomicBoolean closed = new AtomicBoolean();

    public SafeHandle(Pointer /*HANDLE*/ handle) {
        Objects.requireNonNull(handle);
        validateHandle(handle);
        this.handle = handle;
    }

    public Pointer /*HANDLE*/ getValue() {
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
