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
package com.github.zhanhb.judge.win32.struct;

/**
 *
 * @author zhanhb
 */
public class PROCESS_INFORMATION extends jnr.ffi.Struct {

    private final Address hProcess = new Address();
    private final Address hThread = new Address();
    private final DWORD dwProcessId = new DWORD();
    private final DWORD dwThreadId = new DWORD();

    public PROCESS_INFORMATION(jnr.ffi.Runtime runtime) {
        super(runtime);
    }

    public long getProcess() {
        return hProcess.get().address();
    }

    public long getThread() {
        return hThread.get().address();
    }

}
