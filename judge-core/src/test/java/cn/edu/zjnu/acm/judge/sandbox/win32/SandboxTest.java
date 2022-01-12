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
package cn.edu.zjnu.acm.judge.sandbox.win32;

import java.util.ArrayList;
import java.util.List;
import jnc.platform.win32.Handle;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import static cn.edu.zjnu.acm.judge.sandbox.win32.IntegrityLevel.INTEGRITY_LEVEL_LAST;
import static cn.edu.zjnu.acm.judge.sandbox.win32.IntegrityLevel.INTEGRITY_LEVEL_LOW;
import static cn.edu.zjnu.acm.judge.sandbox.win32.TokenLevel.USER_LAST;
import static cn.edu.zjnu.acm.judge.test.PlatformAssuming.assumingWindows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * @author zhanhb
 */
@RunWith(JUnitPlatform.class)
@Slf4j
public class SandboxTest {

    @BeforeAll
    public static void setUpClass() {
        assumingWindows();
    }

    public static List<Arguments> data() {
        ArrayList<Arguments> list = new ArrayList<>(56);
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
                        list.add(arguments(securityLevel, integrityLevel, tokenType, lockdownDefaultDacl));
                    }
                }
            }
        }
        return list;
    }

    /**
     * Test of createRestrictedToken method, of class Sandbox.
     */
    @ParameterizedTest
    @MethodSource("data")
    public void testCreateRestrictedToken(TokenLevel securityLevel, IntegrityLevel integrityLevel, TokenType tokenType, boolean lockdownDefaultDacl) {
        log.info("CreateRestrictedToken");
        Handle.close(Sandbox.INSTANCE.createRestrictedToken(securityLevel, integrityLevel, tokenType, lockdownDefaultDacl, null));
    }

}
