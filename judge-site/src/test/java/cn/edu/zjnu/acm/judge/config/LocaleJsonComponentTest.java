/*
 * Copyright 2017 ZJNU ACM.
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
package cn.edu.zjnu.acm.judge.config;

import cn.edu.zjnu.acm.judge.Application;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * @author zhanhb
 */
@RunWith(JUnitPlatform.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class LocaleJsonComponentTest {

    @Autowired
    private ObjectMapper objectMapper;

    public static Stream<Arguments> data() throws Exception {
        return Stream.of(
                Locale.US,
                Locale.forLanguageTag("zh-HK-x-lvariant-Hant"),
                Locale.forLanguageTag("zh-TW-x-lvariant-Hant"),
                Locale.TAIWAN,
                new Locale("zh", "TW", "Hant"),
                Locale.ENGLISH,
                Locale.ROOT,
                null
        ).map(x -> arguments(x));
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testToJson(Locale locale) throws IOException {
        String str = objectMapper.writeValueAsString(locale);
        String tag = Optional.ofNullable(locale).map(Locale::toLanguageTag).orElse(null);
        String expectStr = new Gson().toJson(tag);
        assertThat(str).isEqualTo(expectStr);
        Locale result = objectMapper.readValue(str, Locale.class);
        log.info("str={}, expect={}, result={}", str, locale, result);
        assertThat(result).isEqualTo(locale);

        str = objectMapper.writeValueAsString(new LocaleHolder(locale));
        log.info("str={}", str);

        result = objectMapper.readValue(str, LocaleHolder.class).getLocale();
        assertThat(result).isEqualTo(locale);
    }

}
