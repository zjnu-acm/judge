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

import java.util.Arrays;

/**
 * @see
 * <a href="https://msdn.microsoft.com/en-us/library/windows/desktop/aa379598(v=vs.85).aspx">SID_IDENTIFIER_AUTHORITY</a>
 */
public class SID_IDENTIFIER_AUTHORITY extends jnc.foreign.Struct {

    public SID_IDENTIFIER_AUTHORITY() {
        padding(6); // actual type BYTE[6]
    }

    public byte[] toByteArray() {
        byte[] bytes = new byte[6];
        getMemory().getBytes(0, bytes, 0, 6);
        return bytes;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(toByteArray());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SID_IDENTIFIER_AUTHORITY other = (SID_IDENTIFIER_AUTHORITY) obj;
        return Arrays.equals(toByteArray(), other.toByteArray());
    }

}
