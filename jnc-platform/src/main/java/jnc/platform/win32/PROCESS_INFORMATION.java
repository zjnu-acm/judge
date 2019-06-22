/*
 * Copyright 2017 ZJNU ACM.
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
package jnc.platform.win32;

/**
 * @see <a href="https://msdn.microsoft.com/en-us/library/windows/desktop/ms684873">PROCESS_INFORMATION</a>
 */
@SuppressWarnings("unused")
public final class PROCESS_INFORMATION extends jnc.foreign.Struct {

    private final uintptr_t hProcess = new uintptr_t();
    private final uintptr_t hThread = new uintptr_t();
    private final DWORD dwProcessId = new DWORD();
    private final DWORD dwThreadId = new DWORD();

    public final long getProcess() {
        return this.hProcess.get();
    }

    public final void setProcess(long value) {
        this.hProcess.set(value);
    }

    public final long getThread() {
        return this.hThread.get();
    }

    public final void setThread(long value) {
        this.hThread.set(value);
    }

    public final int getProcessId() {
        return this.dwProcessId.intValue();
    }

    public final void setProcessId(int value) {
        this.dwProcessId.set(value);
    }

    public final int getThreadId() {
        return this.dwThreadId.intValue();
    }

    public final void setThreadId(int value) {
        this.dwThreadId.set(value);
    }
}
