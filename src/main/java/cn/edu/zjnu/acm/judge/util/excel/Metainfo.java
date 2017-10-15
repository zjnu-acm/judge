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

import com.google.common.collect.ImmutableMap;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import lombok.Value;

/**
 *
 * @author zhanhb
 */
class Metainfo<T> {

    private static final ConcurrentMap<Locale, ConcurrentMap<Class<?>, Metainfo<?>>> MAP = new ConcurrentHashMap<>(1);

    @SuppressWarnings("unchecked")
    static <T> Metainfo<T> forType(Class<T> elementType, @Nullable Locale paramLocale) {
        Locale locale = paramLocale == null ? Locale.ROOT : paramLocale;
        ConcurrentMap<Class<?>, Metainfo<?>> metainfos = MAP.computeIfAbsent(locale, __ -> new ConcurrentHashMap<>(1));
        return (Metainfo<T>) metainfos.computeIfAbsent(elementType, type -> {
            ResourceBundle bundle = null;
            String bundleName = type.getName().replace('.', '/');
            try {
                bundle = ResourceBundle.getBundle(bundleName, locale, type.getClassLoader());
            } catch (MissingResourceException ignore) {
            }
            Field[] fields = elementType.getDeclaredFields();
            List<Member> list = new ArrayList<>(fields.length);
            for (Field field : fields) {
                Excel excel = field.getAnnotation(Excel.class);
                if (excel != null) {
                    String key = excel.name();
                    int order = excel.order();
                    String name = key;
                    try {
                        if (bundle != null) {
                            name = bundle.getString(key);
                        }
                    } catch (MissingResourceException ignore) {
                    }
                    field.setAccessible(true);
                    list.add(new Member(name, order, field));
                }
            }
            // entries in ImmutableMap are sorted in the order as the input
            return new Metainfo<>(list.stream().sorted(Comparator.comparingInt(Member::getOrder))
                    .collect(ImmutableMap.toImmutableMap(Member::getName, Member::getField)));
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

    public Field getField(String name) {
        return fieldMap.get(name);
    }

    int size() {
        return fieldMap.size();
    }

    @Value
    @SuppressWarnings("FinalClass")
    private static class Member {

        private String name;
        private int order;
        private Field field;

    }

}
