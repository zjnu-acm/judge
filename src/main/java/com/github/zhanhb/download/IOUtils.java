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

    public static long copyLarge(InputStream input, OutputStream output, byte[] buffer)
            throws IOException {
        for (long count = 0;;) {
            int n = input.read(buffer);
            if (n == -1) {
                return count;
            }
            output.write(buffer, 0, n);
            count += n;
        }
    }

    public static long copyLarge(InputStream input, OutputStream output,
            final long inputOffset, final long length,
            final byte[] buffer) throws IOException {
        if (inputOffset > 0) {
            for (long remain = inputOffset, skipped; remain > 0; remain -= skipped) {
                skipped = input.skip(remain);
                if (skipped <= 0) {
                    throw new EOFException("Bytes to skip: " + inputOffset + " actual: " + (inputOffset - remain));
                }
            }
        }
        if (length <= 0) {
            if (length < 0) {
                return copyLarge(input, output, buffer);
            }
            return 0;
        }
        int bytesToRead = buffer.length;
        long remain = length;
        for (int read;;
                output.write(buffer, 0, read), remain -= read) {
            if (bytesToRead > remain) {
                bytesToRead = (int) remain;
            }
            if (bytesToRead <= 0) {
                break;
            }
            read = input.read(buffer, 0, bytesToRead);
            if (read == -1) {
                break;
            }
        }
        return length - remain;
    }

}
