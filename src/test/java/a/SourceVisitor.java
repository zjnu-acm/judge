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
package a;

import com.google.common.base.Strings;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 *
 * @author zhanhb
 */
public class SourceVisitor {

    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public static void main(String[] args) throws IOException {
        System.setOut(new PrintStream("target/test.txt", "ISO-8859-1"));
        String dashes = Strings.repeat("-", 45);
        StringWriter sw = new StringWriter();
        PrintWriter out = new PrintWriter(sw);
        Files.walk(Paths.get("src/main"))
                .filter(path -> path.getFileName().toString().matches(".*\\.(html|jspx?)"))
                .forEach(path -> {
                    try {
                        String string = new String(Files.readAllBytes(path), StandardCharsets.ISO_8859_1);
                        string = string.replaceFirst("/\\*[\000-\uFFFF]+?org/licenses/LICENSE[\000-\uFFFF]+?\\*/\r?\n?", "");
                        string = string.replaceFirst("\r?\n?/\\*[\000-\uFFFF]+?@author zhanhb[\000-\uFFFF]+?\\*/", "");
                        out.println(dashes + path.getFileName() + dashes);
                        out.println(string);
                    } catch (IOException ex) {
                        throw new UncheckedIOException(ex);
                    }
                });
        Files.walk(Paths.get("src/main"))
                .filter(path -> path.getFileName().toString().endsWith(".java"))
                .filter(path -> !path.getFileName().toString().equals("package-info.java"))
                .forEach(path -> {
                    try {
                        String string = new String(Files.readAllBytes(path), StandardCharsets.ISO_8859_1);
                        if (!string.contains("@author zhanhb")) {
                            return;
                        }
                        string = string.replaceFirst("/\\*[\000-\uFFFF]+?org/licenses/LICENSE[\000-\uFFFF]+?\\*/\r?\n?", "");
                        string = string.replaceFirst("\r?\n?/\\*[\000-\uFFFF]+?@author zhanhb[\000-\uFFFF]+?\\*/", "");
                        out.println(dashes + path.getFileName() + dashes);
                        out.println(string);
                    } catch (IOException ex) {
                        throw new UncheckedIOException(ex);
                    }
                });
        out.flush();
        String s = new BufferedReader(new StringReader(sw.toString()))
                .lines()
                .filter(str -> !str.startsWith("import "))
                .filter(str -> !str.startsWith("package "))
                .collect(Collectors.joining("\n"));
        System.out.print(s.replaceAll(dashes + "(\\r?\\n)+", dashes + "\\\n"));
    }

}
