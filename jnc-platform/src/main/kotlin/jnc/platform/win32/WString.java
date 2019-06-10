package jnc.platform.win32;

import java.nio.charset.StandardCharsets;
import javax.annotation.Nullable;
import jnc.foreign.Foreign;
import jnc.foreign.Pointer;

public interface WString {
    @Nullable
    static Pointer toNative(@Nullable String string) {
        return (string == null) ? null : Foreign.getDefault().getMemoryManager().allocateString(string, StandardCharsets.UTF_16LE);
    }

    @Nullable
    static String fromNative(@Nullable Pointer ptr) {
        return (ptr != null) ? ptr.getString(0, StandardCharsets.UTF_16LE) : null;
    }

}
