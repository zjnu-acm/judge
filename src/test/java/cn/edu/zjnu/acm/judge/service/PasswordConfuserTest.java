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
package cn.edu.zjnu.acm.judge.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 *
 * @author zhanhb
 */
@Slf4j
public class PasswordConfuserTest {

    @Test
    public void testConfuse() {
        log.info("confuse");
        PasswordConfuser instance = new PasswordConfuser();
        assertNull(instance.confuse(null));
        assertEquals("", instance.confuse(""));
        assertEquals("*", instance.confuse("a"));
        assertEquals("****", instance.confuse("abcd"));
        assertEquals("a***e", instance.confuse("abcde"));
        assertEquals("a****f", instance.confuse("abcdef"));
        assertEquals("a******h", instance.confuse("abcdefgh"));
        assertEquals("a**************p", instance.confuse("abcdefghijklmnop"));
        assertEquals("a......q(total 17 characters)", instance.confuse("abcdefghijklmnopq"));
        assertEquals("a......z(total 26 characters)", instance.confuse("abcdefghijklmnopqrstuvwxyz"));
    }

}
