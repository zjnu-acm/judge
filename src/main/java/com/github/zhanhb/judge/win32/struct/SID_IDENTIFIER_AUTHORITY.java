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
public class SID_IDENTIFIER_AUTHORITY extends jnr.ffi.Struct {

    private final Unsigned8[] Value = array(new Unsigned8[6]); // actual type BYTE[6]

    public SID_IDENTIFIER_AUTHORITY(jnr.ffi.Runtime runtime) {
        super(runtime);
    }

    public SID_IDENTIFIER_AUTHORITY(jnr.ffi.Runtime runtime, int a, int b, int c, int d, int e, int f) {
        this(runtime);
        Value[0].set(a);
        Value[1].set(b);
        Value[2].set(c);
        Value[3].set(d);
        Value[4].set(e);
        Value[5].set(f);
    }

}
