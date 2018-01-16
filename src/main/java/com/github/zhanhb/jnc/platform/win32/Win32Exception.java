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
package com.github.zhanhb.jnc.platform.win32;

/**
 *
 * @author zhanhb
 */
public class Win32Exception extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private static int hresultFromWin32(int x) {
        return x <= 0 ? x : (x & 0x0000FFFF) | 0x80070000;
    }

    private final int errorCode;

    public Win32Exception(int code) {
        this.errorCode = code;
    }

    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return Kernel32Util.formatMessage(hresultFromWin32(errorCode));
    }

}
