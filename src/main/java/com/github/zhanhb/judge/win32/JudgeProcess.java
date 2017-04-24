package com.github.zhanhb.judge.win32;

import com.github.zhanhb.judge.common.Status;
import com.github.zhanhb.judge.jna.win32.Kernel32;
import com.github.zhanhb.judge.jna.win32.Kernel32Util;
import com.github.zhanhb.judge.jna.win32.Psapi;
import com.github.zhanhb.judge.jna.win32.Psapi.PROCESS_MEMORY_COUNTERS;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import java.util.concurrent.atomic.AtomicReference;

import static com.sun.jna.platform.win32.WinBase.WAIT_ABANDONED;
import static com.sun.jna.platform.win32.WinBase.WAIT_FAILED;
import static com.sun.jna.platform.win32.WinError.WAIT_TIMEOUT;

public class JudgeProcess {

    private final WinNT.HANDLE hProcess;
    private final AtomicReference<Status> status = new AtomicReference<>();

    public JudgeProcess(WinNT.HANDLE hProcess) {
        this.hProcess = hProcess;
    }

    public void terminate(Status errorCode) {
        if (status.compareAndSet(null, errorCode)) {
            if (hProcess != null && !WinBase.INVALID_HANDLE_VALUE.equals(hProcess)) {
                // don't check the return value, maybe the process has already exited.
                Kernel32.INSTANCE.TerminateProcess(hProcess, 1);
            }
        }
    }

    public long getPeakMemory() {
        PROCESS_MEMORY_COUNTERS ppsmemCounters = new PROCESS_MEMORY_COUNTERS();
        Kernel32Util.assertTrue(Psapi.INSTANCE.GetProcessMemoryInfo(hProcess, ppsmemCounters, ppsmemCounters.cb));
        return ppsmemCounters.PeakWorkingSetSize.longValue();
    }

    private boolean join0(int millis) {
        int dwWait = Kernel32.INSTANCE.WaitForSingleObject(hProcess, millis);
        switch (dwWait) {
            case WAIT_ABANDONED:
                throw new IllegalStateException();
            case WAIT_FAILED:
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return dwWait != WAIT_TIMEOUT;
    }

    public boolean join(long millis) {
        return join0(millis > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) millis);
    }

    public long getStartTime() {
        WinBase.FILETIME ftCreateTime = new WinBase.FILETIME();
        WinBase.FILETIME temp = new WinBase.FILETIME();
        Kernel32Util.assertTrue(Kernel32.INSTANCE.GetProcessTimes(hProcess, ftCreateTime, temp, temp, temp));
        return ftCreateTime.toTime();
    }

    private long getLong(WinBase.FILETIME ft) {
        return (long) ft.dwHighDateTime << 32 | ft.dwLowDateTime & 0xFFFFFFFFL;
    }

    public long getTime() {
        WinBase.FILETIME tmp = new WinBase.FILETIME();
        WinBase.FILETIME ftKernelTime = new WinBase.FILETIME();
        WinBase.FILETIME ftUserTime = new WinBase.FILETIME();
        Kernel32Util.assertTrue(Kernel32.INSTANCE.GetProcessTimes(hProcess, tmp, tmp, ftKernelTime, ftUserTime));
        return (getLong(ftUserTime) + getLong(ftKernelTime)) / 10000;
    }

    public Status getStatus() {
        return status.get();
    }

    public int getExitCode() {
        IntByReference dwExitCode = new IntByReference();
        Kernel32Util.assertTrue(Kernel32.INSTANCE.GetExitCodeProcess(hProcess, dwExitCode));
        return dwExitCode.getValue();
    }

    public long getActiveTime() {
        return System.currentTimeMillis() - getStartTime();
    }

}
