package cn.edu.zjnu.acm.judge.sandbox.win32;

import cn.edu.zjnu.acm.judge.core.Status;
import com.google.common.base.Preconditions;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;
import jnc.foreign.byref.IntByReference;
import jnc.platform.win32.FILETIME;
import jnc.platform.win32.Handle;
import jnc.platform.win32.Kernel32;
import jnc.platform.win32.Kernel32Util;
import jnc.platform.win32.PROCESS_MEMORY_COUNTERS;
import jnc.platform.win32.Psapi;
import jnc.platform.win32.Win32Exception;

import static jnc.platform.win32.WinBase.WAIT_ABANDONED;
import static jnc.platform.win32.WinBase.WAIT_FAILED;
import static jnc.platform.win32.WinError.WAIT_TIMEOUT;

/**
 * @author zhanhb
 */
public class JudgeProcess {

    private final long /*HANDLE*/ hProcess;
    private final AtomicReference<Status> status = new AtomicReference<>();

    public JudgeProcess(long /*HANDLE*/ hProcess) {
        this.hProcess = Handle.validateHandle(hProcess);
    }

    public void terminate(Status errorCode) {
        if (status.compareAndSet(null, errorCode)) {
            // don't check the return value, maybe the process has already exited.
            Kernel32.INSTANCE.TerminateProcess(hProcess, 1);
        }
    }

    public long getPeakMemory() {
        PROCESS_MEMORY_COUNTERS ppsmemCounters = new PROCESS_MEMORY_COUNTERS();
        Kernel32Util.assertTrue(Psapi.INSTANCE.GetProcessMemoryInfo(hProcess, ppsmemCounters, ppsmemCounters.size()));
        return ppsmemCounters.getPeakWorkingSetSize();
    }

    private boolean join0(int millis) {
        int dwWait = Kernel32.INSTANCE.WaitForSingleObject(hProcess, millis);
        switch (dwWait) {
            case WAIT_ABANDONED:
                throw new IllegalStateException();
            case WAIT_FAILED:
                throw new Win32Exception(Kernel32Util.getLastError());
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

    public Instant getStartTime() {
        FILETIME ftCreateTime = new FILETIME();
        FILETIME temp = new FILETIME();
        Kernel32Util.assertTrue(Kernel32.INSTANCE.GetProcessTimes(hProcess, ftCreateTime, temp, temp, temp));
        return ftCreateTime.toInstant();
    }

    public long getTime() {
        FILETIME tmp = new FILETIME();
        FILETIME ftKernelTime = new FILETIME();
        FILETIME ftUserTime = new FILETIME();
        Kernel32Util.assertTrue(Kernel32.INSTANCE.GetProcessTimes(hProcess, tmp, tmp, ftKernelTime, ftUserTime));
        return Long.divideUnsigned(ftUserTime.longValue() + ftKernelTime.longValue(), 10000);
    }

    public Status getStatus() {
        return status.get();
    }

    public int getExitCode() {
        IntByReference dwExitCode = new IntByReference();
        Kernel32Util.assertTrue(Kernel32.INSTANCE.GetExitCodeProcess(hProcess, dwExitCode));
        return dwExitCode.getValue();
    }

}
