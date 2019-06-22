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
 * @see <a href="https://msdn.microsoft.com/en-us/library/windows/desktop/aa379560">SECURITY_ATTRIBUTES</a>
 */
@SuppressWarnings("unused")
public final class SECURITY_ATTRIBUTES extends jnc.foreign.Struct {

    private final DWORD nLength = new DWORD();
    private final uintptr_t lpSecurityDescriptor = new uintptr_t();
    private final WBOOL bInheritHandle = new WBOOL();

    public final int getLength() {
        return this.nLength.intValue();
    }

    public final void setLength(int value) {
        this.nLength.set(value);
    }

    public final long getSecurityDescriptor() {
        return this.lpSecurityDescriptor.get();
    }

    public final void setSecurityDescriptor(long value) {
        this.lpSecurityDescriptor.set(value);
    }

    public final boolean getInheritHandle() {
        return this.bInheritHandle.get();
    }

    public final void setInheritHandle(boolean value) {
        this.bInheritHandle.set(value);
    }
}
