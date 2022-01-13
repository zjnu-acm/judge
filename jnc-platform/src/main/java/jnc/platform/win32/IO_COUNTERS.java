package jnc.platform.win32;

import jnc.foreign.Struct;

@SuppressWarnings("unused")
public final class IO_COUNTERS extends Struct {

    private final uint64_t ReadOperationCount = new uint64_t();
    private final uint64_t WriteOperationCount = new uint64_t();
    private final uint64_t OtherOperationCount = new uint64_t();
    private final uint64_t ReadTransferCount = new uint64_t();
    private final uint64_t WriteTransferCount = new uint64_t();
    private final uint64_t OtherTransferCount = new uint64_t();

    public long getReadOperationCount() {
        return ReadOperationCount.get();
    }

    public void setReadOperationCount(long readOperationCount) {
        this.ReadOperationCount.set(readOperationCount);
    }

    public long getWriteOperationCount() {
        return WriteOperationCount.get();
    }

    public void setWriteOperationCount(long writeOperationCount) {
        this.WriteOperationCount.set(writeOperationCount);
    }

    public long getOtherOperationCount() {
        return OtherOperationCount.get();
    }

    public void setOtherOperationCount(long otherOperationCount) {
        this.OtherOperationCount.set(otherOperationCount);
    }

    public long getReadTransferCount() {
        return ReadTransferCount.get();
    }

    public void setReadTransferCount(long readTransferCount) {
        this.ReadTransferCount.set(readTransferCount);
    }

    public long getWriteTransferCount() {
        return WriteTransferCount.get();
    }

    public void setWriteTransferCount(long writeTransferCount) {
        this.WriteTransferCount.set(writeTransferCount);
    }

    public long getOtherTransferCount() {
        return OtherTransferCount.get();
    }

    public void setOtherTransferCount(long otherTransferCount) {
        this.OtherTransferCount.set(otherTransferCount);
    }
}
