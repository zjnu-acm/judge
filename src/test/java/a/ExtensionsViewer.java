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
package a;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author zhanhb
 */
public class ExtensionsViewer {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        Path path = Paths.get(".");
        Set<String> set = new HashSet<>(20);

        Files.list(path).filter(p -> Files.isDirectory(p)
                && !p.getFileName().toString().matches("target|\\.(?:git|idea|svn)")).forEach(p -> {
            try {
                Files.walk(p)
                        .map(pp -> pp.getFileName())
                        .map(Object::toString)
                        .map(str -> str.contains(".") ? str.substring(str.lastIndexOf('.') + 1, str.length()) : "")
                        .filter(Objects::nonNull)
                        .filter(string -> !string.isEmpty())
                        .forEach(set::add);
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
        });
        System.out.println(set);
    }

}
