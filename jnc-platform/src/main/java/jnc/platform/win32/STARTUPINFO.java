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

import javax.annotation.Nullable;

/**
 * @see
 * <a href="https://msdn.microsoft.com/zh-tw/library/windows/desktop/ms686331">STARTUPINFO</a>
 */
@SuppressWarnings({"unused", "SpellCheckingInspection"})
public final class STARTUPINFO extends jnc.foreign.Struct {

    private final DWORD cb = new DWORD();
    private final Pointer lpReserved = new Pointer();
    private final Pointer lpDesktop = new Pointer();
    private final Pointer lpTitle = new Pointer();
    private final DWORD dwX = new DWORD();
    private final DWORD dwY = new DWORD();
    private final DWORD dwXSize = new DWORD();
    private final DWORD dwYSize = new DWORD();
    private final DWORD dwXCountChars = new DWORD();
    private final DWORD dwYCountChars = new DWORD();
    private final DWORD dwFillAttribute = new DWORD();
    private final DWORD dwFlags = new DWORD();
    private final WORD wShowWindow = new WORD();
    private final WORD cbReserved2 = new WORD();
    private final uintptr_t lpReserved2 = new uintptr_t();
    private final uintptr_t hStdInput = new uintptr_t();
    private final uintptr_t hStdOutput = new uintptr_t();
    private final uintptr_t hStdError = new uintptr_t();

    public final int getCb() {
        return this.cb.intValue();
    }

    public final void setCb(int value) {
        this.cb.set(value);
    }

    @Nullable
    public final jnc.foreign.Pointer getDesktop() {
        return this.lpDesktop.get();
    }

    public final void setDesktop(@Nullable jnc.foreign.Pointer value) {
        this.lpDesktop.set(value);
    }

    @Nullable
    public final jnc.foreign.Pointer getTitle() {
        return this.lpTitle.get();
    }

    public final void setTitle(@Nullable jnc.foreign.Pointer value) {
        this.lpTitle.set(value);
    }

    public final int getX() {
        return this.dwX.intValue();
    }

    public final void setX(int value) {
        this.dwX.set(value);
    }

    public final int getY() {
        return this.dwY.intValue();
    }

    public final void setY(int value) {
        this.dwY.set(value);
    }

    public final int getXSize() {
        return this.dwXSize.intValue();
    }

    public final void setXSize(int value) {
        this.dwXSize.set(value);
    }

    public final int getYSize() {
        return this.dwYSize.intValue();
    }

    public final void setYSize(int value) {
        this.dwYSize.set(value);
    }

    public final int getXCountChars() {
        return this.dwXCountChars.intValue();
    }

    public final void setXCountChars(int value) {
        this.dwXCountChars.set(value);
    }

    public final int getYCountChars() {
        return this.dwYCountChars.intValue();
    }

    public final void setYCountChars(int value) {
        this.dwYCountChars.set(value);
    }

    public final int getFillAttribute() {
        return this.dwFillAttribute.intValue();
    }

    public final void setFillAttribute(int value) {
        this.dwFillAttribute.set(value);
    }

    public final int getFlags() {
        return this.dwFlags.intValue();
    }

    public final void setFlags(int value) {
        this.dwFlags.set(value);
    }

    public final char getShowWindow() {
        return this.wShowWindow.get();
    }

    public final void setShowWindow(char value) {
        this.wShowWindow.set(value);
    }

    public final long getStdInput() {
        return this.hStdInput.get();
    }

    public final void setStdInput(long value) {
        this.hStdInput.set(value);
    }

    public final long getStdOutput() {
        return this.hStdOutput.get();
    }

    public final void setStdOutput(long value) {
        this.hStdOutput.set(value);
    }

    public final long getStdError() {
        return this.hStdError.get();
    }

    public final void setStdError(long value) {
        this.hStdError.set(value);
    }
}
