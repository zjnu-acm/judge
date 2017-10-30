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
public class FILETIME extends jnr.ffi.Struct {

    private static long toMillis(final int high, final int low) {
        final long filetime = (long) high << 32 | low & 0xffffffffL;
        final long ms_since_16010101 = filetime / (1000 * 10);
        final long ms_since_19700101 = ms_since_16010101 - 11644473600000L;
        return ms_since_19700101;
    }

    private final DWORD dwLowDateTime = new DWORD();
    private final DWORD dwHighDateTime = new DWORD();

    public FILETIME(jnr.ffi.Runtime runtime) {
        super(runtime);
    }

    public int getLowDateTime() {
        return dwLowDateTime.intValue();
    }

    public int getHighDateTime() {
        return dwHighDateTime.intValue();
    }

    public long longValue() {
        return getHighDateTime() << 32 + getLowDateTime();
    }

    public long toMillis() {
        return toMillis(getHighDateTime(), getLowDateTime());
    }

}
