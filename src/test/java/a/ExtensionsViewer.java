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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author zhanhb
 */
public class ExtensionsViewer {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public static void main(String[] args) throws IOException {
        Path path = Paths.get(".");
        Map<String, List<Path>> map = Files.walk(path).filter(p -> p.getNameCount() == 1 || !p.getName(1).toString()
                .matches("target|\\.(?:git|idea|svn|settings)"))
                .filter(Files::isRegularFile)
                .filter(pp -> !getExtension(pp).isEmpty())
                .collect(Collectors.groupingBy(ExtensionsViewer::getExtension));
        map.keySet().removeIf(Files.lines(path.resolve(".gitattributes"))
                .map(str -> str.trim())
                .filter(str -> str.startsWith("*."))
                .map(str -> str.replaceAll("\\*\\.|\\s.+", ""))
                .collect(Collectors.toSet())::contains);
        System.out.println(map);
    }

    private static String getExtension(Path path) {
        String name = path.getFileName().toString();
        return name.lastIndexOf('.') > 0 ? name.substring(name.lastIndexOf('.') + 1) : "";
    }

}
