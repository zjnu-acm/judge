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

import cn.edu.zjnu.acm.judge.util.Platform;
import com.github.zhanhb.jnc.platform.win32.Advapi32;
import com.github.zhanhb.jnc.platform.win32.Kernel32Util;
import com.github.zhanhb.jnc.platform.win32.SID;
import com.github.zhanhb.jnc.platform.win32.WString;
import jnc.foreign.byref.AddressByReference;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 *
 * @author zhanhb
 */
@RunWith(JUnitPlatform.class)
@Slf4j
public class IntegrityLevelTest {

    @BeforeAll
    public static void setUpClass() {
        assumeTrue(Platform.isWindows(), "not windows");
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
            SID sid;
            try {
                sid = SID.copyOf(pSid.getValue());
            } finally {
                Kernel32Util.freeLocalMemory(pSid.getValue());
            }
            assertTrue(sid.isValid());
            String sidString = sid.toString();
            assertThat(sidString).isEqualTo(integrityLevelStr);
        }
    }

}
