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
package cn.edu.zjnu.acm.judge.config.jackson;

import cn.edu.zjnu.acm.judge.Application;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author zhanhb
 */
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class LocaleSerializeTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testToJson() throws IOException {
        for (Locale expect : new Locale[]{
            Locale.US,
            new Locale("zh", "TW", "Hant"),
            Locale.ENGLISH,
            Locale.ROOT,
            null
        }) {
            String str = objectMapper.writeValueAsString(expect);
            assertEquals(expect == null ? "null" : '"' + expect.toLanguageTag() + '"', str);
            Locale result = objectMapper.readValue(str, Locale.class);
            log.info("str={}, expect={}, result={}", str, expect, result);
            assertEquals(expect, result);

            str = objectMapper.writeValueAsString(LocaleHolder.builder().locale(expect).build());
            log.info("str={}, expect={}, result={}", str, expect, result);

            result = objectMapper.readValue(str, LocaleHolder.class).getLocale();
            assertEquals(expect, result);
        }
    }

}
