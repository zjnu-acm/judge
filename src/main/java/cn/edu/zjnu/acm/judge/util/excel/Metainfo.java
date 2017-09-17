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
package cn.edu.zjnu.acm.judge.util.excel;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

/**
 *
 * @author zhanhb
 */
class Metainfo<T> {

    private static final ConcurrentMap<Locale, ConcurrentMap<Class<?>, Metainfo<?>>> MAP = new ConcurrentHashMap<>(1);

    @SuppressWarnings("unchecked")
    static <T> Metainfo<T> forType(Class<T> elementType, Locale paramLocale) {
        Locale locale = paramLocale == null ? Locale.ROOT : paramLocale;
        ConcurrentMap<Class<?>, Metainfo<?>> metainfos = MAP.computeIfAbsent(locale, __ -> new ConcurrentHashMap<>(1));
        return (Metainfo<T>) metainfos.computeIfAbsent(elementType, clazz -> {
            ResourceBundle bundle = null;
            String bundleName = clazz.getName().replace('.', '/');
            try {
                bundle = ResourceBundle.getBundle(bundleName, locale, clazz.getClassLoader());
            } catch (MissingResourceException ignore) {
            }
            Field[] fields = elementType.getDeclaredFields();
            Map<String, Field> map = new LinkedHashMap<>(fields.length);
            for (Field field : fields) {
                Excel excel = field.getAnnotation(Excel.class);
                if (excel != null) {
                    String key = excel.value();
                    String name = key;
                    try {
                        if (bundle != null) {
                            name = bundle.getString(key);
                        }
                    } catch (MissingResourceException ignore) {
                    }
                    field.setAccessible(true);
                    map.put(name, field);
                }
            }
            return new Metainfo<>(map);
        });
    }

    private final Map<String, Field> fieldMap;

    Metainfo(Map<String, Field> fieldMap) {
        this.fieldMap = fieldMap;
    }

    public Stream<String> getHeaderAsStream() {
        return fieldMap.keySet().stream();
    }

    public Stream<Field> getFieldsAsStream() {
        return fieldMap.values().stream();
    }

    public Map<String, Field> getFieldMap() {
        return Collections.unmodifiableMap(fieldMap);
    }

}
