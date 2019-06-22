package jnc.platform.win32;

import javax.annotation.Nonnull;

public final class Win32Exception extends RuntimeException {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("SpellCheckingInspection")
    private static int hresultFromWin32(int x) {
        return (x <= 0) ? x : (x & 0xFFFF | 0x80070000);
    }
    private final int errorCode;

    public Win32Exception(int errorCode) {
        this.errorCode = errorCode;
    }

    public final int getErrorCode() {
        return this.errorCode;
    }

    @Nonnull
    @Override
    public String getMessage() {
        return Kernel32Util.formatMessage(hresultFromWin32(this.errorCode));
    }

}
