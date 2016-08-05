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
package cn.edu.zjnu.acm.judge.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author zhanhb
 */
@Slf4j
public class UtilityTest {

    /**
     * Test of getRandomString method, of class Utility.
     */
    @Test
    public void testGetRandomString() {
        log.info("getRandomString");
        int length = 0;
        String result = Utility.getRandomString(length);
        assertEquals(length, result.length());
        for (int i = 0; i < 10; ++i) {
            length = 20;
            final String t = Utility.getRandomString(length);
            assertEquals(length, t.length());
            t.chars().forEach(ch -> Assert.assertTrue(t, Character.isLetterOrDigit(ch)));
        }
    }

}
