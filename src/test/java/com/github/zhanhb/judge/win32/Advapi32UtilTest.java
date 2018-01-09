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

import jnc.foreign.Platform;
import jnc.foreign.Pointer;
import jnc.foreign.byref.AddressByReference;
import jnc.foreign.byref.PointerByReference;
import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Test;

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
        assumeTrue("not windows", Platform.getNativePlatform().getOS().isWindows());
    }

    /**
     * Test of newPSID method, of class Advapi32Util.
     */
    @Test
    public void testNewPSID() {
        log.info("newPSID");
        for (IntegrityLevel integrityLevel : IntegrityLevel.values()) {
            String integrityLevelStr = integrityLevel.getString();
            if (integrityLevelStr == null) {
                continue;
            }
            AddressByReference pSid = new AddressByReference();
            Kernel32Util.assertTrue(Advapi32.INSTANCE.ConvertStringSidToSidW(
                    WString.toNative(integrityLevelStr), pSid));
            String sidString;
            try {
                PointerByReference stringSid = new PointerByReference();
                Kernel32Util.assertTrue(Advapi32.INSTANCE.ConvertSidToStringSidW(
                        pSid.getValue(), stringSid));

                Pointer ptr = stringSid.getValue();
                try {
                    sidString = WString.fromNative(ptr);
                } finally {
                    Kernel32Util.freeLocalMemory(ptr);
                }
                assertEquals(integrityLevelStr, sidString);
            } finally {
                Kernel32.INSTANCE.LocalFree(pSid.getValue());
            }
        }
    }

}
