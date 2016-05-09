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
package cn.edu.zjnu.acm.judge.config;

import cn.edu.zjnu.acm.judge.domain.Language;
import cn.edu.zjnu.acm.judge.exception.NoSuchLanguageException;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @author zhanhb
 */
public class LanguageFactory {

    private static final Map<Integer, Language> languages;

    static {
        try (InputStream is = LanguageFactory.class.getClassLoader().getResourceAsStream("language.json");
                InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            Gson gson = new Gson();
            Language[] allLanguages = gson.fromJson(reader, Language[].class);
            languages = Arrays.asList(allLanguages).stream()
                    .collect(Collectors.toMap(Language::getId, Function.identity(), throwOnMerge(), TreeMap::new));
        } catch (IOException ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static <T> BinaryOperator<T> throwOnMerge() {
        return (u, v) -> {
            throw new IllegalStateException();
        };
    }

    public static Map<Integer, Language> getLanguages() {
        return Collections.unmodifiableMap(languages);
    }

    public static boolean isLanguage(int languageId) {
        return languages.containsKey(languageId);
    }

    public static Language getLanguage(int languageId) {
        return languages.computeIfAbsent(languageId, x -> {
            throw new NoSuchLanguageException("no such language " + languageId);
        });
    }

    private LanguageFactory() {
    }

}
