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
package com.github.zhanhb.download;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author zhanhb
 */
@Slf4j
public class IOUtilsTest {

    /**
     * Test of copyLarge method, of class IOUtils.
     *
     * @throws java.lang.Exception
     */
    @Test
    @SuppressWarnings("NestedAssignment")
    public void testCopyLarge() throws Exception {
        log.info("copyLarge");
        String str = "abcdefghijklmnopq";
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        for (int i = 1; i <= 4; ++i) {
            for (int j = 0; j < 40; ++j) {
                int inputOffset = ThreadLocalRandom.current().nextInt(str.length());
                int length = ThreadLocalRandom.current().nextInt(str.length());
                if (inputOffset > length) {
                    inputOffset ^= length ^= inputOffset ^= length;
                }
                length -= inputOffset;
                InputStream input = new ByteArrayInputStream(bytes);
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                long copied = IOUtils.copy(input, output, inputOffset, length, new byte[i]);
                String expResult = str.substring(inputOffset, inputOffset + length);
                assertArrayEquals(String.valueOf(i), expResult.getBytes(StandardCharsets.UTF_8), output.toByteArray());
                assertEquals(length, copied);
            }
        }
    }

}
