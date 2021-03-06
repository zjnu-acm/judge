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
package test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

/**
 * @author zhanhb
 */
@RunWith(JUnitPlatform.class)
public class ExtensionsViewer {

    private static String getExtension(Path path) {
        String name = path.getFileName().toString();
        int lastIndexOf = name.lastIndexOf('.');
        return lastIndexOf > 0 ? name.substring(lastIndexOf + 1) : "";
    }

    /**
     * @throws java.io.IOException
     */
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    @Test
    public void test() throws IOException {
        Process process = new ProcessBuilder()
                .command("git", "ls-files", "-z", "--", ":(glob,top,exclude).gitattributes")
                .redirectErrorStream(true).start();
        Map<String, List<Path>> map = Maps.newLinkedHashMap();
        try (InputStream is = process.getInputStream();
                InputStreamReader ir = new InputStreamReader(is, StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(ir)) {
            for (StringBuilder sb = new StringBuilder(20);;) {
                int x = br.read();
                if (x == -1) {
                    break;
                }
                if (x != 0) {
                    sb.append((char) x);
                    continue;
                }
                Path path = Paths.get(sb.toString());
                String extension = getExtension(path);
                if (extension.isEmpty()) {
                    continue;
                }
                map.computeIfAbsent(extension, __ -> Lists.newArrayList()).add(path);
                sb.setLength(0);
            }
        }

        String gitRoot = new String(IOUtils.toByteArray(
                new ProcessBuilder().command("git", "rev-parse", "--show-toplevel")
                        .start().getInputStream()), StandardCharsets.UTF_8).trim();

        map.keySet().removeIf(Files.lines(Paths.get(gitRoot, ".gitattributes"))
                .map(String::trim)
                .filter(str -> str.startsWith("*."))
                .map(str -> str.replaceAll("^\\*\\.|\\s.+$", ""))
                .collect(Collectors.toSet())::contains);
        System.out.println(map);
    }

}
