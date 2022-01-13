package jnc.platform.win32;

@SuppressWarnings("unused")
public class JOBOBJECT_EXTENDED_LIMIT_INFORMATION extends JobObjectInformation {

    private final JOBOBJECT_BASIC_LIMIT_INFORMATION BasicLimitInformation = inner(new JOBOBJECT_BASIC_LIMIT_INFORMATION());
    private final IO_COUNTERS IoInfo = inner(new IO_COUNTERS());
    private final size_t ProcessMemoryLimit = new size_t();
    private final size_t JobMemoryLimit = new size_t();
    private final size_t PeakProcessMemoryUsed = new size_t();
    private final size_t PeakJobMemoryUsed = new size_t();

    public JOBOBJECT_BASIC_LIMIT_INFORMATION getBasicLimitInformation() {
        return BasicLimitInformation;
    }

    public IO_COUNTERS getIoInfo() {
        return IoInfo;
    }

    public long getProcessMemoryLimit() {
        return ProcessMemoryLimit.get();
    }

    public void setProcessMemoryLimit(long processMemoryLimit) {
        this.ProcessMemoryLimit.set(processMemoryLimit);
    }

    public long getJobMemoryLimit() {
        return JobMemoryLimit.get();
    }

    public void setJobMemoryLimit(long jobMemoryLimit) {
        this.JobMemoryLimit.set(jobMemoryLimit);
    }

    public long getPeakProcessMemoryUsed() {
        return PeakProcessMemoryUsed.get();
    }

    public void setPeakProcessMemoryUsed(long peakProcessMemoryUsed) {
        this.PeakProcessMemoryUsed.set(peakProcessMemoryUsed);
    }

    public long getPeakJobMemoryUsed() {
        return PeakJobMemoryUsed.get();
    }

    public void setPeakJobMemoryUsed(long peakJobMemoryUsed) {
        this.PeakJobMemoryUsed.set(peakJobMemoryUsed);
    }
}
