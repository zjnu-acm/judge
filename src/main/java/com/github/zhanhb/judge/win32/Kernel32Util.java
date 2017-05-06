package com.github.zhanhb.judge.win32;

import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinNT;

import static com.sun.jna.platform.win32.WinBase.HANDLE_FLAG_INHERIT;

/**
 *
 * @author zhanhb
 */
public class Kernel32Util {

    public static void assertTrue(boolean test) {
        if (!test) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
    }

    public static WinNT.HANDLE[] createPipe(WinBase.SECURITY_ATTRIBUTES lpPipeAttributes, int nSize) {
        WinNT.HANDLEByReference hReadPipe = new WinNT.HANDLEByReference();
        WinNT.HANDLEByReference hWritePipe = new WinNT.HANDLEByReference();
        assertTrue(Kernel32.INSTANCE.CreatePipe(hReadPipe, hWritePipe, lpPipeAttributes, nSize));
        return new WinNT.HANDLE[]{hReadPipe.getValue(), hWritePipe.getValue()};
    }

    public static void setInheritable(WinNT.HANDLE handle) {
        assertTrue(Kernel32.INSTANCE.SetHandleInformation(handle, HANDLE_FLAG_INHERIT, HANDLE_FLAG_INHERIT));
    }

}
