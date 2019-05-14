/*
 * Copyright 2019 ZJNU ACM.
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
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author zhanhb
 */
@Slf4j
public class URLEncoderTest {

    /**
     * Test of encode method, of class URLEncoder.
     */
    @Test
    public void testEncode() {
        log.info("encode");
        String str = "hello@ \u4f60\u597d";
        URLEncoder encoder = URLEncoder.QUERY;
        String expect = "hello@%20%E4%BD%A0%E5%A5%BD";

        assertEquals(expect, encoder.encode(str));
        assertEquals(expect + expect, encoder.encode(str + str));
        assertEquals(expect + expect + 1, encoder.encode(str + str + 1));

        str = com.google.common.base.Strings.repeat(str, 13);
        assertNotEquals(URLEncoder.URI_COMPONENT.encode(str), encoder.encode(str));

        str = "abc@@@123456";
        assertSame(str, encoder.encode(str));
    }

}