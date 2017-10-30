/*
 * Copyright 2016 ZJNU ACM.
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
package com.github.zhanhb.judge.win32;

import com.github.zhanhb.judge.win32.struct.SID_IDENTIFIER_AUTHORITY;
import jnr.ffi.Platform;
import jnr.ffi.Pointer;
import jnr.ffi.byref.PointerByReference;
import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.github.zhanhb.judge.win32.Advapi32.SECURITY_MANDATORY_LOW_RID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

/**
 *
 * @author zhanhb
 */
@Slf4j
public class Advapi32UtilTest {

    @BeforeClass
    public static void setUpClass() {
        assumeTrue("not windows", Platform.getNativePlatform().getOS() == Platform.OS.WINDOWS);
    }

    private static String convertSidToStringSid(Pointer sid) {
        PointerByReference stringSid = new PointerByReference();
        Kernel32Util.assertTrue(Advapi32.INSTANCE.ConvertSidToStringSidW(sid, stringSid));

        Pointer ptr = stringSid.getValue();
        try {
            return WString.fromNative(ptr);
        } finally {
            Kernel32Util.freeLocalMemory(ptr);
        }
    }

    /**
     * Test of newPSID method, of class Advapi32Util.
     */
    @Test
    public void testNewPSID() {
        log.info("newPSID");
        jnr.ffi.Runtime runtime = jnr.ffi.Runtime.getSystemRuntime();
        SID_IDENTIFIER_AUTHORITY pIdentifierAuthority = new SID_IDENTIFIER_AUTHORITY(runtime, 0, 0, 0, 0, 0, 16);

        Pointer pSid = Advapi32Util.newPSID(pIdentifierAuthority, SECURITY_MANDATORY_LOW_RID);
        try {
            String sidString = convertSidToStringSid(pSid);
            assertEquals("S-1-16-4096", sidString);
        } finally {
            Kernel32Util.assertTrue(Advapi32.INSTANCE.FreeSid(pSid) == null);
        }
    }

}
