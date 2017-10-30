package com.github.zhanhb.judge.win32;

import com.github.zhanhb.judge.common.Status;
import com.github.zhanhb.judge.win32.struct.FILETIME;
import com.github.zhanhb.judge.win32.struct.PROCESS_MEMORY_COUNTERS;
import com.google.common.base.Preconditions;
import java.util.concurrent.atomic.AtomicReference;
import jnr.ffi.Pointer;
import jnr.ffi.byref.IntByReference;

import static com.github.zhanhb.judge.win32.Native.sizeof;
import static com.github.zhanhb.judge.win32.WinBase.WAIT_ABANDONED;
import static com.github.zhanhb.judge.win32.WinBase.WAIT_FAILED;
import static com.github.zhanhb.judge.win32.WinBase.WAIT_TIMEOUT;

public class JudgeProcess {

    private static final jnr.ffi.Runtime runtime = jnr.ffi.Runtime.getSystemRuntime();

    private final Pointer /*HANDLE*/ hProcess;
    private final AtomicReference<Status> status = new AtomicReference<>();

    public JudgeProcess(Pointer /*HANDLE*/ hProcess) {
        this.hProcess = hProcess;
    }

    public void terminate(Status errorCode) {
        if (status.compareAndSet(null, errorCode)) {
            try {
                SafeHandle.validateHandle(hProcess);
            } catch (IllegalArgumentException ex) {
                return;
            }
            // don't check the return value, maybe the process has already exited.
            Kernel32.INSTANCE.TerminateProcess(hProcess, 1);
        }
    }

    public long getPeakMemory() {
        PROCESS_MEMORY_COUNTERS ppsmemCounters = new PROCESS_MEMORY_COUNTERS(runtime);
        Kernel32Util.assertTrue(Psapi.INSTANCE.GetProcessMemoryInfo(hProcess, ppsmemCounters, sizeof(ppsmemCounters)));
        return ppsmemCounters.getPeakWorkingSetSize();
    }

    private boolean join0(int millis) {
        int dwWait = Kernel32.INSTANCE.WaitForSingleObject(hProcess, millis);
        switch (dwWait) {
            case WAIT_ABANDONED:
                throw new IllegalStateException();
            case WAIT_FAILED:
                throw new Win32Exception(runtime.getLastError());
            case WAIT_TIMEOUT:
                return false;
            default:
                return true;
        }
    }

    public boolean join(long millis) {
        Preconditions.checkArgument(millis >= 0);
        return join0((int) Math.min(millis, 0xFFFFFFFEL));
    }

    public long getStartTime() {
        FILETIME ftCreateTime = new FILETIME(runtime);
        FILETIME temp = new FILETIME(runtime);
        Kernel32Util.assertTrue(Kernel32.INSTANCE.GetProcessTimes(hProcess, ftCreateTime, temp, temp, temp));
        return ftCreateTime.toMillis();
    }

    public long getTime() {
        FILETIME tmp = new FILETIME(runtime);
        FILETIME ftKernelTime = new FILETIME(runtime);
        FILETIME ftUserTime = new FILETIME(runtime);
        Kernel32Util.assertTrue(Kernel32.INSTANCE.GetProcessTimes(hProcess, tmp, tmp, ftKernelTime, ftUserTime));
        return (ftUserTime.longValue() + ftKernelTime.longValue()) / 10000;
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
