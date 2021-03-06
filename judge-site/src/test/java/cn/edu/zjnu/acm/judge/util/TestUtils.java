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
package cn.edu.zjnu.acm.judge.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 *
 * @author zhanhb
 */
public class TestUtils {

    public static void testConstructor(Class<?> cl) throws Exception {
        Constructor<?> c = cl.getDeclaredConstructor();
        c.setAccessible(true);
        Throwable cause = assertThrows(InvocationTargetException.class, c::newInstance).getTargetException();
        assertThat(cause).describedAs("targetException").isInstanceOf(AssertionError.class);
    }

}
