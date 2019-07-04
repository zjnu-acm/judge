/*
 * Copyright 2014 zhanhb.
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

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;

public class CopyHelper extends SimpleFileVisitor<Path> {

    public static Path copy(Path src, Path dest, CopyOption... options) throws IOException {
        return Files.walkFileTree(src, new CopyHelper(src, dest, options));
    }

    private final Path src;
    private final Path dest;
    private final CopyOption[] options;

    public CopyHelper(Path srcRoot, Path destRoot, CopyOption... options) {
        this.src = Objects.requireNonNull(srcRoot, "source path should not be null");
        this.dest = Objects.requireNonNull(destRoot, "dest path should not be null");
        this.options = Objects.requireNonNull(options);
    }

    private Path resolve(Path dir) {
        String str = src.relativize(dir).toString();
        return dest.resolve(str);
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        Path resolve = resolve(dir);
        if (!Files.exists(resolve)) {
            Files.createDirectories(resolve);
            BasicFileAttributeView view = Files.getFileAttributeView(resolve, BasicFileAttributeView.class);
            view.setTimes(attrs.lastModifiedTime(), attrs.lastAccessTime(), attrs.creationTime());
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Files.copy(file, resolve(file), options);
        return FileVisitResult.CONTINUE;
    }
}
