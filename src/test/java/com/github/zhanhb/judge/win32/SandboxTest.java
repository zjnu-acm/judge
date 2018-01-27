/*
 * Copyright 2018 ZJNU ACM.
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

import java.util.ArrayList;
import java.util.List;
import jnc.foreign.Platform;
import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static com.github.zhanhb.judge.win32.IntegrityLevel.INTEGRITY_LEVEL_LAST;
import static com.github.zhanhb.judge.win32.IntegrityLevel.INTEGRITY_LEVEL_LOW;
import static com.github.zhanhb.judge.win32.TokenLevel.USER_LAST;
import static org.junit.Assume.assumeTrue;

/**
 *
 * @author zhanhb
 */
@Slf4j
@RunWith(Parameterized.class)
public class SandboxTest {

    @BeforeClass
    public static void setUpClass() {
        assumeTrue("not windows", Platform.getNativePlatform().getOS().isWindows());
    }

    @Parameterized.Parameters(name = "{index} {0} {1} {2} {3}")
    public static List<Object[]> data() {
        ArrayList<Object[]> list = new ArrayList<>(56);
        TokenLevel[] tokenLevels = TokenLevel.values();
        IntegrityLevel[] integrityLevels = new IntegrityLevel[]{INTEGRITY_LEVEL_LOW, INTEGRITY_LEVEL_LAST};
        TokenType[] tokenTypes = TokenType.values();
        boolean[] lockDowns = new boolean[]{true, false};
        for (TokenLevel securityLevel : tokenLevels) {
            if (USER_LAST == securityLevel) {
                continue;
            }
            for (IntegrityLevel integrityLevel : integrityLevels) {
                for (TokenType tokenType : tokenTypes) {
                    for (boolean lockdownDefaultDacl : lockDowns) {
                        list.add(new Object[]{securityLevel, integrityLevel, tokenType, lockdownDefaultDacl});
                    }
                }
            }
        }
        return list;
    }

    private final TokenLevel securityLevel;
    private final IntegrityLevel integrityLevel;
    private final TokenType tokenType;
    private final boolean lockdownDefaultDacl;

    public SandboxTest(TokenLevel securityLevel, IntegrityLevel integrityLevel, TokenType tokenType, boolean lockdownDefaultDacl) {
        this.securityLevel = securityLevel;
        this.integrityLevel = integrityLevel;
        this.tokenType = tokenType;
        this.lockdownDefaultDacl = lockdownDefaultDacl;
    }

    /**
     * Test of createRestrictedToken method, of class Sandbox.
     */
    @Test
    public void testCreateRestrictedToken() {
        log.info("CreateRestrictedToken");
        Sandbox instance = new Sandbox();
        Handle.close(instance.createRestrictedToken(securityLevel, integrityLevel, tokenType, lockdownDefaultDacl));
    }

}
