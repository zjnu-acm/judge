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
 * @see
 * <a href="https://msdn.microsoft.com/en-us/library/windows/desktop/aa379598(v=vs.85).aspx">SID_IDENTIFIER_AUTHORITY</a>
 */
public class SID_IDENTIFIER_AUTHORITY extends jnc.foreign.Struct {

    private final uint8_t[] Value = array(new uint8_t[6]); // actual type BYTE[6]

    public SID_IDENTIFIER_AUTHORITY() {
    }

    public SID_IDENTIFIER_AUTHORITY(int a, int b, int c, int d, int e, int f) {
        Value[0].set((short) a);
        Value[1].set((short) b);
        Value[2].set((short) c);
        Value[3].set((short) d);
        Value[4].set((short) e);
        Value[5].set((short) f);
    }

}
