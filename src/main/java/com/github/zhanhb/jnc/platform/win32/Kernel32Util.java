package com.github.zhanhb.jnc.platform.win32;

import jnc.foreign.Pointer;
import jnc.foreign.byref.PointerByReference;

public interface Kernel32Util {

    public static void assertTrue(boolean test) {
        if (!test) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
    }

    static String formatMessage(int code) {
        PointerByReference buffer = new PointerByReference();
        int nLen = Kernel32.INSTANCE.FormatMessageW(
                WinBase.FORMAT_MESSAGE_ALLOCATE_BUFFER
                | WinBase.FORMAT_MESSAGE_FROM_SYSTEM
                | WinBase.FORMAT_MESSAGE_IGNORE_INSERTS,
                0 /*NULL*/,
                code,
                0, // TODO: // MAKELANGID(LANG_NEUTRAL,SUBLANG_DEFAULT)
                buffer, 0, 0);
        if (nLen == 0) {
            throw new RuntimeException(Integer.toUnsignedString(Kernel32.INSTANCE.GetLastError()));
        }
        Pointer ptr = buffer.getValue();
        try {
            return WString.fromNative(ptr).trim();
        } finally {
            freeLocalMemory(ptr.address());
        }
    }

    public static void freeLocalMemory(long ptr) {
        assertTrue(Kernel32.INSTANCE.LocalFree(ptr) == 0);
    }

}
