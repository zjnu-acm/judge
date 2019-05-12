/*
 * Copyright 2015 zhanhb.
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
package cn.edu.zjnu.acm.judge;

import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author zhanhb
 */
@Configuration
@Slf4j
@SuppressWarnings("UseOfSystemOutOrSystemErr")
class SystemOutCharset {

    static {
        try {
            log.info("charset of System.out: {}", getCharset0(System.out));
            log.info("charset of System.err: {}", getCharset0(System.err));
            System.getProperties().store(System.out, null);
        } catch (ClassNotFoundException | NoSuchFieldException | SecurityException ignore) {
        } catch (Exception ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static Object get(Field field, Object o) throws Exception {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        return field.get(o);
    }

    private static Charset getCharset0(PrintStream out) throws Exception {
        Object o = get(PrintStream.class.getDeclaredField("charOut"), out);
        o = get(OutputStreamWriter.class.getDeclaredField("se"), o);
        return (Charset) get(Class.forName("sun.nio.cs.StreamEncoder").getDeclaredField("cs"), o);
    }

    // it's a component, we should have a no args constructor
    // here we declared to avoid IDE warnings.
    SystemOutCharset() {
    }

}
