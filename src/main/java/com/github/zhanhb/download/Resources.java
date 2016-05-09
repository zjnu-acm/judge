/*
 * Copyright 2015 zhanhb.
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

/**
 *
 * @author zhanhb
 * @date Jun 6, 2015, 0:36:42
 */
public class Resources {

    public static Builder of(File file) {
        return new Builder(file);
    }

    public static Builder of(Path path) {
        return new Builder(path);
    }

    private Resources() {
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    @SuppressWarnings("PublicInnerClass")
    public static class Builder {

        private Path path;
        private String name;
        private long lastModified = -1;
        private String mimeType;

        private Builder(Path path) {
            if (path == null) {
                throw new NullPointerException("path");
            }
            this.path = path;
        }

        private Builder(File file) {
            if (file == null) {
                throw new NullPointerException("file");
            }
            this.path = file.toPath();
        }

        public String name() {
            return name;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public long lastModified() throws IOException {
            if (lastModified != -1) {
                return lastModified;
            }
            if (path != null) {
                return Files.getLastModifiedTime(path).toMillis();
            }
            return lastModified;
        }

        public Builder lastModified(long lastModified) {
            this.lastModified = lastModified;
            return this;
        }

        public Builder lastModified(Date date) {
            this.lastModified = date != null ? date.getTime() : -1;
            return this;
        }

        public String mimeType() {
            return mimeType;
        }

        public Builder mimeType(String mimeType) {
            this.mimeType = mimeType;
            return this;
        }

        public Resource build() {
            return new FileResource(path, name, mimeType, lastModified);
        }
    }

    private static class FileResource extends Resource {

        private final Path path;
        private final String name;
        private final String mimeType;
        private final long lastModified;

        private FileResource(Path path, String name, String mimeType, long lastModified) {
            this.path = path;
            this.name = name;
            this.mimeType = mimeType;
            this.lastModified = lastModified;
        }

        @Override
        boolean exists() {
            return Files.exists(path);
        }

        @Override
        boolean isReadable() {
            return (Files.isReadable(this.path) && !Files.isDirectory(this.path));
        }

        @Override
        String getFilename() {
            return name != null ? name : path.getFileName().toString();
        }

        @Override
        InputStream openStream() throws IOException {
            return Files.newInputStream(path);
        }

        @Override
        long getContentLength() throws IOException {
            return Files.size(path);
        }

        @Override
        long getLastModified() throws IOException {
            if (lastModified != -1) {
                return lastModified;
            }
            return Files.getLastModifiedTime(path).toMillis();
        }

        @Override
        public String getMimeType() {
            return mimeType;
        }
    }

}
