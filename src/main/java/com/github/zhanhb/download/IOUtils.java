/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
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

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@SuppressWarnings("UtilityClassWithoutPrivateConstructor")
class IOUtils {

    public static long copy(InputStream input, OutputStream output,
            final long inputOffset, final long length,
            final byte[] buffer) throws IOException {
        if (inputOffset > 0) {
            for (long remain = inputOffset, n; remain > 0; remain -= n) {
                n = input.skip(remain);
                if (n <= 0) {
                    throw new EOFException("Bytes to skip: " + inputOffset + " actual: " + (inputOffset - remain));
                }
            }
        }
        if (length <= 0) {
            if (length != 0) {
                for (long count = 0;;) {
                    int n = input.read(buffer);
                    if (n == -1) {
                        return count == 0 ? -1 : count;
                    }
                    output.write(buffer, 0, n);
                    count += n;
                }
            }
            return 0;
        }
        int bytesToRead = buffer.length;
        long count = 0;
        for (long d = length / bytesToRead; d > 0; d--) {
            int n = input.read(buffer);
            if (n == -1) {
                return count == 0 ? -1 : count;
            }
            output.write(buffer, 0, n);
            count += n;
        }
        long rem = length - count;
        while (rem > 0) {
            int n = input.read(buffer, 0, (int) Math.min(rem, bytesToRead));
            if (n == -1) {
                break;
            }
            output.write(buffer, 0, n);
            count += n;
            rem -= n;
        }
        return count == 0 ? -1 : count;
    }

}
