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
package i18n;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import static java.util.ResourceBundle.getBundle;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * @author zhanhb
 */
@RunWith(JUnitPlatform.class)
@Slf4j
public class MessageSourceTest {

    private static final Locale zh = Locale.CHINESE;
    private static final Locale zh_cn = Locale.CHINA;
    private static final Locale zh_hant = Locale.forLanguageTag("zh-Hant");
    private static final Locale zh_hk = Locale.forLanguageTag("zh-hk");
    private static final Locale zh_hans_hk = Locale.forLanguageTag("zh-Hans-HK");
    private static final Locale zh_tw = Locale.forLanguageTag("zh-tw");

    @BeforeAll
    public static void setUpClass() {
        log.info("zh={}", zh.toLanguageTag());
        log.info("zh-cn={}", zh_cn.toLanguageTag());
        log.info("zh-hk={}", zh_hk.toLanguageTag());
        log.info("zh-hans-hk={}", zh_hans_hk.toLanguageTag());
        log.info("zh-tw={}", zh_tw.toLanguageTag());
    }

    public static Stream<Arguments> data() {
        String bundleName = "i18n.messages";
        return Stream.of(
                arguments(getBundle(bundleName, Locale.ROOT), getBundle(bundleName, zh)),
                arguments(getBundle(bundleName, zh), getBundle(bundleName, zh_hant)),
                arguments(getBundle(bundleName, zh), getBundle(bundleName, zh_hk)),
                arguments(getBundle(bundleName, zh), getBundle(bundleName, zh_tw))
        );
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testCurrentMissingKey(ResourceBundle comparedBundle, ResourceBundle currentBundle) {
        Locale current = currentBundle.getLocale();
        Locale compared = comparedBundle.getLocale();
        assertThat(current)
                .describedAs("%s vs %s", current.toLanguageTag(), compared.toLanguageTag())
                .isNotEqualTo(compared);
        for (String key : comparedBundle.keySet()) {
            String valueCurrent = currentBundle.getString(key);
            String valueCompared = comparedBundle.getString(key);
            if (valueCurrent.equals(valueCompared)) {
                log.warn("missing '{}', '{} vs {}'", key, current.toLanguageTag(), compared.toLanguageTag());
            }
        }
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testComparedMissingKey(ResourceBundle comparedBundle, ResourceBundle currentBundle) {
        for (String key : currentBundle.keySet()) {
            if (key.equals("ckfinder.lang")) {
                continue;
            }
            assertThat(comparedBundle.keySet().contains(key))
                    .overridingErrorMessage("key %s present in %s but missing in %s",
                            key, currentBundle.getLocale(), comparedBundle.getLocale())
                    .isTrue();
        }
    }

}
