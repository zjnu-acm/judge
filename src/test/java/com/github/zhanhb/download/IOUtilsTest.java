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
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
 *
 * @author zhanhb
 */
@Slf4j
public class IOUtilsTest {

    /**
     * Test of copyLarge method, of class IOUtils.
     * @throws java.lang.Exception
     */
    @Test
    public void testCopyLarge_5args() throws Exception {
        log.info("copyLarge");
        InputStream input = new ByteArrayInputStream("abcdefgh".getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        long inputOffset = 3;
        long length = 2;
        byte[] buffer = new byte[4096];
        IOUtils.copyLarge(input, output, inputOffset, length, buffer);
        assertArrayEquals("de".getBytes(StandardCharsets.UTF_8), output.toByteArray());
    }

}
