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
package a;

import cn.edu.zjnu.acm.judge.util.MatcherWrapper;
import cn.edu.zjnu.acm.judge.util.Strings;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;
import org.thymeleaf.util.StringUtils;

/**
 *
 * Created on 2014-12-25 12:38:12
 *
 * @author zhanhb
 */
public class JSEscaper {

    public static void main(String[] args) throws IOException {
        Path parent = Paths.get("src/main/webapp/ckfinder");
        Files.walk(parent).filter(Files::isRegularFile).filter(f -> f.getFileName().toString().endsWith(".js")).forEach(path -> {
            try {
                resolve(path);
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
        });
    }

    private static void resolve(Path path) throws IOException {
        Charset charset = StandardCharsets.UTF_8;
        String str = charset.newDecoder().decode(ByteBuffer.wrap(Files.readAllBytes(path))).toString();
        String old = str;
        if (str.startsWith("\ufeff")) {
            str = str.substring(1);
        }
        str = MatcherWrapper.matcher("\\\\[xX][0-9A-Fa-f]{2}", str).replaceAll(esc(2, 16));
        str = MatcherWrapper.matcher("\\\\u[0-9A-Fa-f]{4}", str).replaceAll(esc(2, 16));
        str = MatcherWrapper.matcher("(?-i)\\\\[0-7]{3}", str).replaceAll(esc(1, 8));
        str = MatcherWrapper.matcher("[\u007F-\uffff]", str).replaceAll(matcher -> {
            char ch = matcher.group().charAt(0);
            return ch < 256 ? "\\x" + Integer.toString(ch, 16).toUpperCase()
                    : "\\u" + Strings.slice("0000" + Integer.toString(ch, 16).toUpperCase(), -4);
        });
        if (!str.equals(old)) {
            Files.write(path, str.getBytes(charset));
        }
    }

    private static Function<MatcherWrapper, String> esc(int index, int radix) {
        return m -> StringUtils.escapeJavaScript(
                String.valueOf((char) Integer.parseInt(m.group().substring(index), radix)));
    }

}
