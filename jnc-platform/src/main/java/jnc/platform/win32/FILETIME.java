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

import java.time.Instant;
import javax.annotation.Nonnull;

/**
 * @see <a href="https://msdn.microsoft.com/zh-tw/library/windows/desktop/ms724284">FILETIME</a>
 */
@SuppressWarnings({"unused", "SpellCheckingInspection", "WeakerAccess"})
public final class FILETIME extends jnc.foreign.Struct {

    private static Instant toInstant(long filetime) {
        long t = filetime - 116444736000000000L;
        return Instant.ofEpochSecond(t / 10000000, t % 10000000 * 100);
    }

    private final DWORD dwLowDateTime = new DWORD();
    private final DWORD dwHighDateTime = new DWORD();

    public final int getLowDateTime() {
        return this.dwLowDateTime.intValue();
    }

    public final void setLowDateTime(int value) {
        this.dwLowDateTime.set(value);
    }

    public final int getHighDateTime() {
        return this.dwHighDateTime.intValue();
    }

    public final void setHighDateTime(int value) {
        this.dwHighDateTime.set(value);
    }

    public final long longValue() {
        return (long) getHighDateTime() << 32 | getLowDateTime() & 0xFFFFFFFFL;
    }

    @Nonnull
    public final Instant toInstant() {
        return toInstant(longValue());
    }

}
