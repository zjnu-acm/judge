/*
 * Copyright 2019 ZJNU ACM.
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

import com.google.gson.Gson;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.unbescape.uri.UriEscape;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 *
 * @author zhanhb
 */
@RunWith(JUnitPlatform.class)
@Slf4j
public class URLEncoderTest {

    /**
     * Test of encode method, of class URLEncoder.
     */
    @Test
    public void testEncode() {
        log.info("encode");
        String str = "hello@ \u4f60\u597d";
        URLEncoder encoder = URLEncoder.QUERY;
        String expect = "hello@%20%E4%BD%A0%E5%A5%BD";

        assertThat(encoder.encode(str)).isEqualTo(expect);
        assertThat(encoder.encode(str + str)).isEqualTo(expect + expect);
        assertThat(encoder.encode(str + str + 1)).isEqualTo(expect + expect + 1);

        str = com.google.common.base.Strings.repeat(str, 13);
        assertThat(encoder.encode(str)).isNotEqualTo(URLEncoder.URI_COMPONENT.encode(str));

        str = "abc@@@123456";
        assertThat(encoder.encode(str)).isSameAs(str);
    }

    private String printableAscii() {
        return IntStream.range(32, 127).collect(StringBuilder::new,
                (sb, x) -> sb.append((char) x),
                StringBuilder::append).toString();
    }

    @Test
    public void testUriComponent() throws ScriptException {
        String printableAscii = printableAscii();

        ScriptEngine javascript = new ScriptEngineManager(null).getEngineByName("javascript");
        Object expect = javascript.eval("encodeURIComponent(" + new Gson().toJson(printableAscii) + ")");
        assertThat(URLEncoder.URI_COMPONENT.encode(printableAscii)).isEqualTo(expect);
    }

    public static List<Arguments> data() {
        return Arrays.asList(
                arguments(URLEncoder.PATH, (Function<String, String>) UriEscape::escapeUriPath),
                arguments(URLEncoder.QUERY, (Function<String, String>) UriEscape::escapeUriQueryParam),
                arguments(URLEncoder.FRAGMENT, (Function<String, String>) UriEscape::escapeUriFragmentId),
                arguments(URLEncoder.PATH_SEGMENT, (Function<String, String>) UriEscape::escapeUriPathSegment)
        );
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testPath(URLEncoder encoder, Function<String, String> function) {
        String printableAscii = printableAscii();
        String expect = function.apply(printableAscii);
        assertThat(encoder.encode(printableAscii)).isEqualTo(expect);
    }

}
