/*
 * Copyright 2016 ZJNU ACM.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.zhanhb.judge.win32;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Union;
import com.sun.jna.win32.W32APIOptions;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author zhanhb
 */
@SuppressWarnings({"PublicField", "PublicInnerClass"})
public interface Debug extends com.sun.jna.platform.win32.Kernel32 {

    @SuppressWarnings("FieldNameHidesFieldInSuperclass")
    Debug INSTANCE = Native.loadLibrary("kernel32", Debug.class, W32APIOptions.UNICODE_OPTIONS);

    int DBG_CONTINUE = 0x10002;
    int DBG_TERMINATE_THREAD = 0x40010003;
    int DBG_TERMINATE_PROCESS = 0x40010004;
    int DBG_CONTROL_C = 0x40010005;
    int DBG_CONTROL_BREAK = 0x40010008;
    int DBG_EXCEPTION_NOT_HANDLED = 0x80010001;

    int EXCEPTION_DEBUG_EVENT = 1;
    int CREATE_THREAD_DEBUG_EVENT = 2;
    int CREATE_PROCESS_DEBUG_EVENT = 3;
    int EXIT_THREAD_DEBUG_EVENT = 4;
    int EXIT_PROCESS_DEBUG_EVENT = 5;
    int LOAD_DLL_DEBUG_EVENT = 6;
    int UNLOAD_DLL_DEBUG_EVENT = 7;
    int OUTPUT_DEBUG_STRING_EVENT = 8;
    int RIP_EVENT = 9;

    int STATUS_SEGMENT_NOTIFICATION = 0x40000005;
    int STATUS_GUARD_PAGE_VIOLATION = 0x80000001;
    int STATUS_DATATYPE_MISALIGNMENT = 0x80000002;
    int STATUS_BREAKPOINT = 0x80000003;
    int STATUS_SINGLE_STEP = 0x80000004;
    int STATUS_ACCESS_VIOLATION = 0xC0000005;
    int STATUS_IN_PAGE_ERROR = 0xC0000006;
    int STATUS_INVALID_HANDLE = 0xC0000008;
    int STATUS_NO_MEMORY = 0xC0000017;
    int STATUS_ILLEGAL_INSTRUCTION = 0xC000001D;
    int STATUS_NONCONTINUABLE_EXCEPTION = 0xC0000025;
    int STATUS_INVALID_DISPOSITION = 0xC0000026;
    int STATUS_ARRAY_BOUNDS_EXCEEDED = 0xC000008C;
    int STATUS_FLOAT_DENORMAL_OPERAND = 0xC000008D;
    int STATUS_FLOAT_DIVIDE_BY_ZERO = 0xC000008E;
    int STATUS_FLOAT_INEXACT_RESULT = 0xC000008F;
    int STATUS_FLOAT_INVALID_OPERATION = 0xC0000090;
    int STATUS_FLOAT_OVERFLOW = 0xC0000091;
    int STATUS_FLOAT_STACK_CHECK = 0xC0000092;
    int STATUS_FLOAT_UNDERFLOW = 0xC0000093;
    int STATUS_INTEGER_DIVIDE_BY_ZERO = 0xC0000094;
    int STATUS_INTEGER_OVERFLOW = 0xC0000095;
    int STATUS_PRIVILEGED_INSTRUCTION = 0xC0000096;
    int STATUS_STACK_OVERFLOW = 0xC00000FD;
    int STATUS_CONTROL_C_EXIT = 0xC000013A;
    int STATUS_DLL_INIT_FAILED = 0xC0000142;
    int STATUS_DLL_INIT_FAILED_LOGOFF = 0xC000026B;

    int EXCEPTION_ACCESS_VIOLATION = STATUS_ACCESS_VIOLATION;
    @SuppressWarnings("SuspiciousNameCombination")
    int EXCEPTION_DATATYPE_MISALIGNMENT = STATUS_DATATYPE_MISALIGNMENT;
    int EXCEPTION_BREAKPOINT = STATUS_BREAKPOINT;
    int EXCEPTION_SINGLE_STEP = STATUS_SINGLE_STEP;
    int EXCEPTION_ARRAY_BOUNDS_EXCEEDED = STATUS_ARRAY_BOUNDS_EXCEEDED;
    int EXCEPTION_FLT_DENORMAL_OPERAND = STATUS_FLOAT_DENORMAL_OPERAND;
    int EXCEPTION_FLT_DIVIDE_BY_ZERO = STATUS_FLOAT_DIVIDE_BY_ZERO;
    int EXCEPTION_FLT_INEXACT_RESULT = STATUS_FLOAT_INEXACT_RESULT;
    int EXCEPTION_FLT_INVALID_OPERATION = STATUS_FLOAT_INVALID_OPERATION;
    int EXCEPTION_FLT_OVERFLOW = STATUS_FLOAT_OVERFLOW;
    int EXCEPTION_FLT_STACK_CHECK = STATUS_FLOAT_STACK_CHECK;
    int EXCEPTION_FLT_UNDERFLOW = STATUS_FLOAT_UNDERFLOW;
    int EXCEPTION_INT_DIVIDE_BY_ZERO = STATUS_INTEGER_DIVIDE_BY_ZERO;
    int EXCEPTION_INT_OVERFLOW = STATUS_INTEGER_OVERFLOW;
    int EXCEPTION_PRIV_INSTRUCTION = STATUS_PRIVILEGED_INSTRUCTION;
    int EXCEPTION_IN_PAGE_ERROR = STATUS_IN_PAGE_ERROR;
    int EXCEPTION_ILLEGAL_INSTRUCTION = STATUS_ILLEGAL_INSTRUCTION;
    int EXCEPTION_NONCONTINUABLE_EXCEPTION = STATUS_NONCONTINUABLE_EXCEPTION;
    int EXCEPTION_STACK_OVERFLOW = STATUS_STACK_OVERFLOW;
    int EXCEPTION_INVALID_DISPOSITION = STATUS_INVALID_DISPOSITION;
    int EXCEPTION_GUARD_PAGE = STATUS_GUARD_PAGE_VIOLATION;
    int EXCEPTION_INVALID_HANDLE = STATUS_INVALID_HANDLE;

    boolean WaitForDebugEvent(DEBUG_EVENT lpDebugEvent, int dwMilliseconds);

    boolean ContinueDebugEvent(int dwProcessId, int dwThreadId, int dwContinueStatus);

    class DEBUG_EVENT extends Structure {

        public int dwDebugEventCode;
        public int dwProcessId;
        public int dwThreadId;
        public UNION u;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("dwDebugEventCode", "dwProcessId", "dwThreadId", "u");
        }

        public static class UNION extends Union {

            public EXCEPTION_DEBUG_INFO Exception;
            public CREATE_THREAD_DEBUG_INFO CreateThread;
            public CREATE_PROCESS_DEBUG_INFO CreateProcessInfo;
            public EXIT_THREAD_DEBUG_INFO ExitThread;
            public EXIT_PROCESS_DEBUG_INFO ExitProcess;
            public LOAD_DLL_DEBUG_INFO LoadDll;
            public UNLOAD_DLL_DEBUG_INFO UnloadDll;
            public OUTPUT_DEBUG_STRING_INFO DebugString;
            public RIP_INFO RipInfo;

        }

    }

    class EXCEPTION_DEBUG_INFO extends Structure {

        public EXCEPTION_RECORD ExceptionRecord;
        public int dwFirstChance;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("ExceptionRecord", "dwFirstChance");
        }

    }

    class EXCEPTION_RECORD extends Structure {

        static final int EXCEPTION_MAXIMUM_PARAMETERS = 15;
        public int ExceptionCode;
        public int ExceptionFlags;
        public EXCEPTION_RECORD.ByReference ExceptionRecord;
        public Pointer ExceptionAddress;
        public int NumberParameters;
        public int ExceptionInformation[] = new int[EXCEPTION_MAXIMUM_PARAMETERS];

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("ExceptionCode", "ExceptionFlags",
                    "ExceptionRecord", "ExceptionAddress", "NumberParameters",
                    "ExceptionInformation");
        }

        public class ByReference extends EXCEPTION_RECORD implements Structure.ByReference {

            public ByReference() {
            }

        }
    }

    class CREATE_THREAD_DEBUG_INFO extends Structure {

        public HANDLE hThread;
        public Pointer lpThreadLocalBase;
        public Pointer lpStartAddress;/*Callback*/

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("hThread", "lpThreadLocalBase", "lpStartAddress");
        }

    }

    class CREATE_PROCESS_DEBUG_INFO extends Structure {

        public HANDLE hFile;
        public HANDLE hProcess;
        public HANDLE hThread;
        public Pointer lpBaseOfImage;
        public int dwDebugInfoFileOffset;
        public int nDebugInfoSize;
        public Pointer lpThreadLocalBase;
        public Pointer lpStartAddress;
        public Pointer lpImageName;
        public short fUnicode;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("hFile", "hProcess", "hThread", "lpBaseOfImage",
                    "dwDebugInfoFileOffset", "nDebugInfoSize", "lpThreadLocalBase",
                    "lpStartAddress", "lpImageName", "fUnicode");
        }

    }

    class EXIT_THREAD_DEBUG_INFO extends Structure {

        public int dwExitCode;

        @Override
        protected List<String> getFieldOrder() {
            return Collections.singletonList("dwExitCode");
        }

    }

    class EXIT_PROCESS_DEBUG_INFO extends Structure {

        public int dwExitCode;

        @Override
        protected List<String> getFieldOrder() {
            return Collections.singletonList("dwExitCode");
        }

    }

    class LOAD_DLL_DEBUG_INFO extends Structure {

        public HANDLE hFile;
        public Pointer lpBaseOfDll;
        public int dwDebugInfoFileOffset;
        public int nDebugInfoSize;
        public Pointer lpImageName;
        public short fUnicode;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("hFile", "lpBaseOfDll", "dwDebugInfoFileOffset",
                    "nDebugInfoSize", "lpImageName", "fUnicode");
        }

    }

    class UNLOAD_DLL_DEBUG_INFO extends Structure {

        public Pointer lpBaseOfDll;

        @Override
        protected List<String> getFieldOrder() {
            return Collections.singletonList("lpBaseOfDll");
        }

    }

    class OUTPUT_DEBUG_STRING_INFO extends Structure {

        public String lpDebugStringData;
        public short fUnicode;
        public short nDebugStringLength;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("lpDebugStringData", "fUnicode", "nDebugStringLength");
        }

    }

    class RIP_INFO extends Structure {

        public int dwError;
        public int dwType;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("dwError", "dwType");
        }

    }

}
