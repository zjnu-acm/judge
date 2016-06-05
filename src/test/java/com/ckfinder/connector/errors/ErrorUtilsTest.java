/*
 * Copyright 2016 ZJNU ACM.
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
package com.ckfinder.connector.errors;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 *
 * @author zhanhb
 */
public class ErrorUtilsTest {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        URL location = ErrorUtils.class.getProtectionDomain().getCodeSource().getLocation();
        Path base = Paths.get(location.toURI()).resolve(ErrorUtils.class.getPackage().getName().replace('.', '/'));
        ErrorUtils instance = ErrorUtils.getInstance();
        Field field = ErrorUtils.class.getDeclaredField("langMap");
        field.setAccessible(true);
        Object obj = field.get(instance);
        for (Map.Entry<String, Map<Integer, String>> en : ((Map<String, Map<Integer, String>>) obj).entrySet()) {
            String key = en.getKey();
            Map<Integer, String> value = en.getValue();
            Locale locale = new Locale(key);
            Properties p = new Properties();
            for (Map.Entry<Integer, String> entry : value.entrySet()) {
                Integer key1 = entry.getKey();
                String value1 = entry.getValue();
                p.setProperty("" + key1, value1);
            }
            Path path = base.resolve("LocalStrings_" + locale + ".properties");
            try (OutputStream out = Files.newOutputStream(path)) {
                p.store(out, null);
            }
            ResourceBundle.getBundle("com/ckfinder/connector/errors/LocalStrings", locale);
        }

    }

}
