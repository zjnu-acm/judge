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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.Locale;
import org.springframework.boot.jackson.JsonComponent;

/**
 *
 * @author zhanhb
 */
@JsonComponent
public class LocaleJsonComponent {

    public static class CustomLocaleDeserializer extends StdDeserializer<Locale> {

        private static final long serialVersionUID = 1L;

        public CustomLocaleDeserializer() {
            super(Locale.class);
        }

        @Override
        public Locale deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException {
            return Locale.forLanguageTag(p.getText());
        }

    }

    public static class CustomLocaleSerializer extends StdSerializer<Locale> {

        private static final long serialVersionUID = 1L;

        public CustomLocaleSerializer() {
            super(Locale.class);
        }

        @Override
        public void serialize(Locale value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
            gen.writeString(value.toLanguageTag());
        }

    }

}
