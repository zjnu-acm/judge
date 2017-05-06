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

import com.sun.jna.Platform;
import com.sun.jna.platform.win32.WinNT;
import lombok.extern.slf4j.Slf4j;
import org.junit.AssumptionViolatedException;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.github.zhanhb.judge.win32.Advapi32.SECURITY_MANDATORY_LOW_RID;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author zhanhb
 */
@Slf4j
public class Advapi32UtilTest {

    @BeforeClass
    public static void setUpClass() {
        if (!Platform.isWindows()) {
            throw new AssumptionViolatedException("not windows");
        }
    }

    /**
     * Test of newPSID method, of class Advapi32Util.
     */
    @Test
    public void testNewPSID() {
        log.info("newPSID");
        Advapi32.SID_IDENTIFIER_AUTHORITY pIdentifierAuthority = new Advapi32.SID_IDENTIFIER_AUTHORITY(new byte[]{0, 0, 0, 0, 0, 16});

        WinNT.PSID result = Advapi32Util.newPSID(pIdentifierAuthority, SECURITY_MANDATORY_LOW_RID);
        String sidString = result.getSidString();
        assertEquals("S-1-16-4096", sidString);
    }

}
