package jnc.platform.win32

import jnc.foreign.Foreign
import jnc.foreign.byref.PointerByReference

/**
 * @author zhanhb
 */
interface Kernel32Util {

    companion object {
        val lastError: Int
            @JvmStatic
            get() = Foreign.getDefault().lastError

        @JvmStatic
        fun assertTrue(test: Boolean) {
            if (!test) {
                throw Win32Exception(lastError)
            }
        }

        @JvmStatic
        fun formatMessage(code: Int): String {
            val buffer = PointerByReference()
            val nLen = Kernel32.INSTANCE.FormatMessageW(
                    WinBase.FORMAT_MESSAGE_ALLOCATE_BUFFER
                            or WinBase.FORMAT_MESSAGE_FROM_SYSTEM
                            or WinBase.FORMAT_MESSAGE_IGNORE_INSERTS,
                    0 /*NULL*/,
                    code,
                    0, // TODO: // MAKELANGID(LANG_NEUTRAL,SUBLANG_DEFAULT)
                    buffer, 0, 0)
            if (nLen == 0) {
                throw RuntimeException(Integer.toUnsignedString(lastError))
            }
            val ptr = buffer.value
            try {
                return WString.fromNative(ptr)!!.trim()
            } finally {
                freeLocalMemory(ptr.address())
            }
        }

        @JvmStatic
        fun freeLocalMemory(ptr: Long) {
            @Suppress("DEPRECATED_IDENTITY_EQUALS")
            assertTrue(Kernel32.INSTANCE.LocalFree(ptr) === 0L)
        }
    }
}
