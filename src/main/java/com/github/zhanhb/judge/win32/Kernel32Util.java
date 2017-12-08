package com.github.zhanhb.judge.win32;

import com.github.zhanhb.judge.win32.struct.JOBOBJECTINFOCLASS;
import com.github.zhanhb.judge.win32.struct.JOBOBJECT_INFORMATION;
import com.google.common.annotations.VisibleForTesting;
import jnr.ffi.Pointer;
import jnr.ffi.byref.PointerByReference;

import static com.github.zhanhb.judge.win32.Kernel32.HANDLE_FLAG_INHERIT;
import static com.github.zhanhb.judge.win32.Native.sizeof;

/**
 *
 * @author zhanhb
 */
@SuppressWarnings("UtilityClassWithoutPrivateConstructor")
class Kernel32Util {

    private static final jnr.ffi.Runtime runtime = jnr.ffi.Runtime.getSystemRuntime();

    // convert int to boolean seems buggy
    static void assertTrue(int test) {
        if (test == 0) {
            throw new Win32Exception(runtime.getLastError());
        }
    }

    static void assertTrue(boolean test) {
        assertTrue(test ? 1 : 0);
    }

    static void setInheritable(long handle) {
        assertTrue(Kernel32.INSTANCE.SetHandleInformation(handle, HANDLE_FLAG_INHERIT, HANDLE_FLAG_INHERIT));
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
            throw new RuntimeException(Integer.toUnsignedString(runtime.getLastError()));
        }
        Pointer ptr = buffer.getValue();
        try {
            return WString.fromNative(ptr).trim();
        } finally {
            freeLocalMemory(ptr);
        }
    }

    @VisibleForTesting
    static void freeLocalMemory(Pointer ptr) {
        assertTrue(Kernel32.INSTANCE.LocalFree(ptr.address()) == 0);
    }

    static void setInformationJobObject(long hJob, JOBOBJECTINFOCLASS jobobjectinfoclass, JOBOBJECT_INFORMATION jobj) {
        assertTrue(Kernel32.INSTANCE.SetInformationJobObject(hJob, jobobjectinfoclass.value(), jobj, sizeof(jobj)));
    }

}
