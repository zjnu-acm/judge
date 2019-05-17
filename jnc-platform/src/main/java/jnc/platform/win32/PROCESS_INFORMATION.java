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
 * @see
 * <a href="https://msdn.microsoft.com/en-us/library/windows/desktop/ms684873(v=vs.85).aspx">PROCESS_INFORMATION</a>
 * @author zhanhb
 */
@SuppressWarnings("unused")
public class PROCESS_INFORMATION extends jnc.foreign.Struct {

    private final uintptr_t /*HANDLE*/ hProcess = new uintptr_t();
    private final uintptr_t /*HANDLE*/ hThread = new uintptr_t();
    private final DWORD dwProcessId = new DWORD();
    private final DWORD dwThreadId = new DWORD();

    public long getProcess() {
        return hProcess.get();
    }

    public long getThread() {
        return hThread.get();
    }

}