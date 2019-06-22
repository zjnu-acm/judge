package jnc.platform.win32;

import javax.annotation.Nonnull;
import jnc.foreign.Foreign;
import jnc.foreign.Pointer;
import jnc.foreign.byref.PointerByReference;

public interface Kernel32Util {

    static int getLastError() {
        return Foreign.getDefault().getLastError();
    }

    static void assertTrue(boolean test) {
        if (!test) {
            throw new Win32Exception(getLastError());
        }
    }

    @Nonnull
    @SuppressWarnings("null")
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
            throw new RuntimeException(Integer.toUnsignedString(getLastError()));
        }
        Pointer ptr = buffer.getValue();
        try {
            return WString.fromNative(ptr).trim();
        } finally {
            freeLocalMemory(ptr.address());
        }
    }

    static void freeLocalMemory(long ptr) {
        assertTrue((Kernel32.INSTANCE.LocalFree(ptr) == 0));
    }

}
