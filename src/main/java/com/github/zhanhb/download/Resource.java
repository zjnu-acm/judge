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

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import javax.annotation.Nullable;

public abstract class Resource {

    // -------------------------------------------------------------- Constants
    /**
     * HTTP date format.
     */
    private static final SimpleDateFormat format;

    /**
     * GMT timezone - all HTTP dates are on GMT
     */
    static {
        format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    abstract boolean exists();

    abstract boolean isReadable();

    abstract String getFilename();

    /**
     * Get content length.
     *
     * @return content length value
     * @throws java.io.IOException when an I/O exception occur.
     */
    abstract long getContentLength() throws IOException;

    /**
     * Get last modified time.
     *
     * @return lastModified time value
     * @throws java.io.IOException
     */
    abstract long getLastModified() throws IOException;

    /**
     * @return Returns the lastModifiedHttp.
     */
    @Nullable
    String getLastModifiedHttp() {
        try {
            long modifiedDate = getLastModified();
            if (modifiedDate >= 0) {
                synchronized (format) {
                    return format.format(modifiedDate);
                }
            }
        } catch (IOException ex) {
        }
        return null;
    }

    public abstract String getMimeType();

    /**
     * Get ETag.
     *
     * @return strong ETag if available, else weak ETag.
     */
    @Nullable
    String getETag() {
        // The weakETag is contentLength + lastModified
        try {
            long contentLength = getContentLength();
            long lastModified = getLastModified();
            if (contentLength >= 0 || lastModified >= 0) {
                return "W/\"" + contentLength + "-"
                        + lastModified + "\"";
            }
        } catch (IOException ex) {
        }
        return null;
    }

    /**
     * Always open a new input stream.
     *
     * @return a new input stream.
     * @throws IOException
     */
    abstract InputStream openStream() throws IOException;

}
