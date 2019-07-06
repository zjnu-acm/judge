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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * @author zhanhb
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonResource {

    public static JsonResource withInitial(Supplier<String> supplier) {
        return new JsonResource(Objects.requireNonNull(supplier, "supplier"));
    }

    private final Supplier<String> supplier;
    private volatile String cacheValue;
    private volatile Map<String, String> cachedMap;
    @SuppressWarnings("VolatileArrayField")
    private volatile Object[] cache1;
    @SuppressWarnings("VolatileArrayField")
    private volatile Object[] cache2;

    private String cache(Locale locale, String value) {
        cache2 = cache1;
        cache1 = new Object[]{locale, value};
        return value;
    }

    @Nullable
    @SuppressWarnings({"AssignmentToMethodParameter", "NestedAssignment"})
    public String get(@Nullable Locale locale) {
        String value = supplier.get();
        if (value == null) {
            return null;
        }
        Map<String, String> map = cachedMap;
        if (!value.equals(cacheValue)) {
            try {
                cachedMap = map = toMap(Lazy.mapper.readTree(value));
            } catch (IOException ex) {
                cacheValue = value;
                return value;
            }
            cacheValue = value;
        } else if (map == null) {
            // malformed json
            return value;
        }
        if (locale == null) {
            locale = Locale.ROOT;
        }
        Object[] a;
        if ((a = cache1) != null && locale.equals(a[0])) {
            return (String) a[1];
        }
        if ((a = cache2) != null && locale.equals(a[0])) {
            cache2 = cache1;
            cache1 = a;
            return (String) a[1];
        }
        String res = map.get(locale.toLanguageTag());
        if (res != null) {
            return cache(locale, res);
        }
        List<Locale> candidateLocales = Lazy.CONTROL.getCandidateLocales("", locale);
        for (Locale candidateLocale : candidateLocales) {
            res = map.get(candidateLocale.toLanguageTag());
            if (res != null) {
                return cache(locale, res);
            }
        }
        // fallback option
        res = map.get("en");
        if (res != null) {
            return cache(locale, res);
        }
        //noinspection LoopStatementThatDoesntLoop
        for (Map.Entry<String, String> entry : map.entrySet()) {
            return cache(locale, entry.getValue());
        }
        return cache(locale, null);
    }

    private Map<String, String> toMap(JsonNode jsonNode) {
        TreeMap<String, String> map = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        for (Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.fields(); fields.hasNext();) {
            Map.Entry<String, JsonNode> entry = fields.next();
            map.put(entry.getKey(), entry.getValue().asText());
        }
        return map;
    }

    private interface Lazy {

        ObjectMapper mapper = new ObjectMapper();
        ResourceBundle.Control CONTROL = ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_PROPERTIES);

    }

}
