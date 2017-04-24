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
package com.github.zhanhb.judge.jna.win32;

import com.github.zhanhb.judge.jna.win32.Debug.DEBUG_EVENT;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinBase.FILETIME;
import com.sun.jna.platform.win32.WinBase.PROCESS_INFORMATION;
import com.sun.jna.platform.win32.WinDef;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;

import static com.github.zhanhb.judge.jna.win32.Debug.CREATE_PROCESS_DEBUG_EVENT;
import static com.github.zhanhb.judge.jna.win32.Debug.CREATE_THREAD_DEBUG_EVENT;
import static com.github.zhanhb.judge.jna.win32.Debug.DBG_CONTINUE;
import static com.github.zhanhb.judge.jna.win32.Debug.DBG_CONTROL_C;
import static com.github.zhanhb.judge.jna.win32.Debug.DBG_EXCEPTION_NOT_HANDLED;
import static com.github.zhanhb.judge.jna.win32.Debug.EXCEPTION_ACCESS_VIOLATION;
import static com.github.zhanhb.judge.jna.win32.Debug.EXCEPTION_BREAKPOINT;
import static com.github.zhanhb.judge.jna.win32.Debug.EXCEPTION_DATATYPE_MISALIGNMENT;
import static com.github.zhanhb.judge.jna.win32.Debug.EXCEPTION_DEBUG_EVENT;
import static com.github.zhanhb.judge.jna.win32.Debug.EXCEPTION_SINGLE_STEP;
import static com.github.zhanhb.judge.jna.win32.Debug.EXIT_PROCESS_DEBUG_EVENT;
import static com.github.zhanhb.judge.jna.win32.Debug.EXIT_THREAD_DEBUG_EVENT;
import static com.github.zhanhb.judge.jna.win32.Debug.LOAD_DLL_DEBUG_EVENT;
import static com.github.zhanhb.judge.jna.win32.Debug.OUTPUT_DEBUG_STRING_EVENT;
import static com.github.zhanhb.judge.jna.win32.Debug.RIP_EVENT;
import static com.github.zhanhb.judge.jna.win32.Debug.UNLOAD_DLL_DEBUG_EVENT;
import static com.github.zhanhb.judge.jna.win32.Kernel32.HIGH_PRIORITY_CLASS;
import static com.github.zhanhb.judge.jna.win32.Kernel32.SEM_NOGPFAULTERRORBOX;
import static com.sun.jna.platform.win32.WinBase.CREATE_BREAKAWAY_FROM_JOB;
import static com.sun.jna.platform.win32.WinBase.CREATE_NEW_PROCESS_GROUP;
import static com.sun.jna.platform.win32.WinBase.CREATE_NO_WINDOW;
import static com.sun.jna.platform.win32.WinBase.CREATE_UNICODE_ENVIRONMENT;
import static com.sun.jna.platform.win32.WinBase.DEBUG_PROCESS;
import static com.sun.jna.platform.win32.WinBase.INFINITE;

/**
 *
 * @author zhanhb
 */
@Ignore
@Slf4j
public class DebugTest {

    void puts(String s) {
        log.info(s);
    }

    int input(DEBUG_EVENT ev) {
        log.info(ev.dwProcessId + " " + ev.dwThreadId);
        return 0;
        // return getch();
    }

    int OnCreateThreadDebugEvent(DEBUG_EVENT ev) {
        puts("OnCreateThreadDebugEvent");
        input(ev);
        return DBG_CONTINUE;
    }

    int OnCreateProcessDebugEvent(DEBUG_EVENT ev) {
        puts("OnCreateProcessDebugEvent");
        input(ev);
        return DBG_CONTINUE;
    }

    int OnExitThreadDebugEvent(DEBUG_EVENT ev) {
        puts("OnExitThreadDebugEvent");
        input(ev);
        return DBG_CONTINUE;
    }

    int OnExitProcessDebugEvent(DEBUG_EVENT ev) {
        puts("OnExitProcessDebugEvent");
        input(ev);
        return DBG_CONTINUE;
    }

    int OnLoadDllDebugEvent(DEBUG_EVENT ev) {
        puts("OnLoadDllDebugEvent");
        input(ev);
        return DBG_CONTINUE;
    }

    int OnUnloadDllDebugEvent(DEBUG_EVENT ev) {
        puts("OnUnloadDllDebugEvent");
        input(ev);
        return DBG_CONTINUE;
    }

    int OnOutputDebugStringEvent(DEBUG_EVENT ev) {
        puts("OnOutputDebugStringEvent");
        input(ev);
        return DBG_CONTINUE;
    }

    int OnRipEvent(DEBUG_EVENT ev) {
        puts("OnRipEvent");
        input(ev);
        return DBG_CONTINUE;
    }

    void EnterDebugLoop(DEBUG_EVENT DebugEv) {
        int dwContinueStatus = DBG_CONTINUE; // exception continuation
        int processCount = 0;

        for (;;) {
            // Wait for a debugging event to occur. The second parameter indicates
            // that the function does not return until a debugging event occurs.
            Debug.INSTANCE.WaitForDebugEvent(DebugEv, INFINITE);
            // Process the debugging event code.
            switch (DebugEv.dwDebugEventCode) {
                case EXCEPTION_DEBUG_EVENT:
                    // Process the exception code. When handling
                    // exceptions, remember to set the continuation
                    // status parameter (dwContinueStatus). This value
                    // is used by the ContinueDebugEvent function.

                    switch (DebugEv.u.Exception.ExceptionRecord.ExceptionCode) {
                        case EXCEPTION_ACCESS_VIOLATION:
                            puts("EXCEPTION_ACCESS_VIOLATION");
                            // First chance: Pass this on to the system.
                            // Last chance: Display an appropriate error.
                            break;

                        case EXCEPTION_BREAKPOINT:
                            puts("EXCEPTION_BREAKPOINT");
                            // First chance: Display the current
                            // instruction and register values.
                            break;

                        case EXCEPTION_DATATYPE_MISALIGNMENT:
                            puts("EXCEPTION_DATATYPE_MISALIGNMENT");
                            // First chance: Pass this on to the system.
                            // Last chance: Display an appropriate error.
                            break;

                        case EXCEPTION_SINGLE_STEP:
                            puts("EXCEPTION_SINGLE_STEP");
                            // First chance: Update the display of the
                            // current instruction and register values.
                            break;

                        case DBG_CONTROL_C:
                            puts("DBG_CONTROL_C");
                            // First chance: Pass this on to the system.
                            // Last chance: Display an appropriate error.
                            break;

                        default:
                            log.info("{}", DebugEv.u.Exception.ExceptionRecord.ExceptionCode);
                            puts("default");
                            // Handle other exceptions.
                            break;
                    }
                    dwContinueStatus = DBG_EXCEPTION_NOT_HANDLED;
                    break;

                case CREATE_THREAD_DEBUG_EVENT:
                    // As needed, examine or change the thread's registers
                    // with the GetThreadContext and SetThreadContext functions;
                    // and suspend and resume thread execution with the
                    // SuspendThread and ResumeThread functions.
                    puts("CREATE_THREAD_DEBUG_EVENT");

                    dwContinueStatus = OnCreateThreadDebugEvent(DebugEv);
                    break;

                case CREATE_PROCESS_DEBUG_EVENT:
                    // As needed, examine or change the registers of the
                    // process's initial thread with the GetThreadContext and
                    // SetThreadContext functions; read from and write to the
                    // process's virtual memory with the ReadProcessMemory and
                    // WriteProcessMemory functions; and suspend and resume
                    // thread execution with the SuspendThread and ResumeThread
                    // functions. Be sure to close the handle to the process image
                    // file with CloseHandle.
                    ++processCount;
                    dwContinueStatus = OnCreateProcessDebugEvent(DebugEv);
                    break;

                case EXIT_THREAD_DEBUG_EVENT:
                    // Display the thread's exit code.

                    dwContinueStatus = OnExitThreadDebugEvent(DebugEv);
                    break;

                case EXIT_PROCESS_DEBUG_EVENT:
                    // Display the process's exit code.
                    --processCount;
                    if (processCount == 0) {
                        Debug.INSTANCE.ContinueDebugEvent(DebugEv.dwProcessId, DebugEv.dwThreadId, DBG_CONTINUE);
                        return;
                    }
                    dwContinueStatus = OnExitProcessDebugEvent(DebugEv);
                    break;

                case LOAD_DLL_DEBUG_EVENT:
                    // Read the debugging information included in the newly
                    // loaded DLL. Be sure to close the handle to the loaded DLL
                    // with CloseHandle.

                    dwContinueStatus = OnLoadDllDebugEvent(DebugEv);
                    break;

                case UNLOAD_DLL_DEBUG_EVENT:
                    // Display a message that the DLL has been unloaded.

                    dwContinueStatus = OnUnloadDllDebugEvent(DebugEv);
                    break;

                case OUTPUT_DEBUG_STRING_EVENT:
                    // Display the output debugging string.

                    dwContinueStatus = OnOutputDebugStringEvent(DebugEv);
                    break;

                case RIP_EVENT:
                    dwContinueStatus = OnRipEvent(DebugEv);
                    break;
            }

            // Resume executing the thread that reported the debugging event.
            Debug.INSTANCE.ContinueDebugEvent(DebugEv.dwProcessId,
                    DebugEv.dwThreadId,
                    dwContinueStatus);
        }
    }

    /**
     * Test of WaitForDebugEvent method, of class Debug.
     */
    @Test
    public void testDebug() {
        int old = Kernel32.INSTANCE.SetErrorMode(SEM_NOGPFAULTERRORBOX);
        try {
            WinBase.STARTUPINFO si = new WinBase.STARTUPINFO();
            PROCESS_INFORMATION pi = new PROCESS_INFORMATION();
            String command = "C:\\Users\\Administrator\\Documents\\C-Free\\Temp\\6.exe";
            if (Kernel32.INSTANCE.CreateProcess(null, command, null, null, true,
                    new WinDef.DWORD(HIGH_PRIORITY_CLASS
                            | CREATE_NEW_PROCESS_GROUP
                            | CREATE_UNICODE_ENVIRONMENT
                            | CREATE_BREAKAWAY_FROM_JOB
                            | CREATE_NO_WINDOW
                            | DEBUG_PROCESS),
                    Pointer.NULL, null, si, pi)) {
                log.info(pi.dwProcessId + " " + pi.dwThreadId);
                DEBUG_EVENT de = new DEBUG_EVENT();
                EnterDebugLoop(de);
                FILETIME a = new FILETIME();
                FILETIME b = new FILETIME();
                FILETIME tmp = new FILETIME();
                Kernel32.INSTANCE.WaitForSingleObject(pi.hProcess, 100000);
                Kernel32.INSTANCE.GetProcessTimes(pi.hProcess, a, b, tmp, tmp);
                log.info("{}", b.toTime() - a.toTime());
            } else {
                Kernel32Util.assertTrue(false);
            }
        } finally {
            Kernel32.INSTANCE.SetErrorMode(old);
        }
    }

}
