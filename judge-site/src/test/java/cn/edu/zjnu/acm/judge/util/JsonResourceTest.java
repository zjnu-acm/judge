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

import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author zhanhb
 */
@RunWith(JUnitPlatform.class)
@Slf4j
public class JsonResourceTest {

    /**
     * Test of get method, of class JsonResource.
     */
    @Test
    public void testMalformed() {
        for (Locale locale : new Locale[]{null, Locale.FRANCE}) {
            {
                String expResult = "abc";
                JsonResource resource = JsonResource.withInitial(() -> expResult);
                assertThat(resource.get(locale)).isEqualTo(expResult);
                // test cache
                assertThat(resource.get(locale)).isEqualTo(expResult);
            }
            JsonResource resource = JsonResource.withInitial(() -> null);
            assertThat(resource.get(locale)).isNull();
            // test cache
            assertThat(resource.get(locale)).isNull();
        }
    }

    /**
     * Test of get method, of class JsonResource.
     */
    @Test
    public void testJson() {
        JsonResource resource = JsonResource.withInitial(() -> "{\"en\":\"value-en\",\"zh\":\"value-zh\",\"zh-hant\":\"value-hant\"}");
        assertThat(resource.get(Locale.CANADA)).isEqualTo("value-en");
        assertThat(resource.get(Locale.CHINA)).isEqualTo("value-zh");
        assertThat(resource.get(Locale.CHINESE)).isEqualTo("value-zh");
        assertThat(resource.get(Locale.TAIWAN)).isEqualTo("value-hant");
        assertThat(resource.get(Locale.forLanguageTag("zh-hk"))).isEqualTo("value-hant");

        // test cache
        for (Locale locale : new Locale[]{null, Locale.CANADA, Locale.CHINA}) {
            assertThat(resource.get(locale)).isEqualTo(resource.get(locale));
        }

        // test cache2
        String value = resource.get(Locale.CANADA);
        resource.get(Locale.CHINA);
        assertThat(resource.get(Locale.CANADA)).isEqualTo(value);
    }

    @Test
    public void testFallback() {
        assertThat(JsonResource.withInitial(() -> "{\"ch\":\"fallback\"}").get(Locale.ITALY)).isEqualTo("fallback");
        assertThat(JsonResource.withInitial(() -> "{}").get(Locale.ITALY)).isNull();
    }

}
