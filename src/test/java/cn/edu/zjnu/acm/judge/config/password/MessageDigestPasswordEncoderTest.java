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
package cn.edu.zjnu.acm.judge.config.password;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author zhanhb
 */
@Slf4j
@Transactional
public class MessageDigestPasswordEncoderTest {

    /**
     * Test of encode method, of class MessageDigestPasswordEncoder.
     */
    @Test
    public void testEncode() {
        log.info("encode");
        CharSequence password = "123456";
        MessageDigestPasswordEncoder instance = MessageDigestPasswordEncoder.md5();
        String expResult = "e10adc3949ba59abbe56e057f20f883e";
        String result = instance.encode(password);
        assertEquals(expResult, result);
    }

    /**
     * Test of matches method, of class MessageDigestPasswordEncoder.
     */
    @Test
    public void testMatches() {
        log.info("matches");
        CharSequence rawPassword = "";
        String encodedPassword = "Da39a3ee5e6b4b0d3255bfef95601890afd80709";
        MessageDigestPasswordEncoder instance = MessageDigestPasswordEncoder.sha1();
        boolean expResult = true;
        boolean result = instance.matches(rawPassword, encodedPassword);
        assertEquals(expResult, result);
    }

    @Test
    public void testAll() {
        Arrays.stream(MessageDigestPasswordEncoder.class.getMethods())
                .filter(method -> Modifier.isStatic(method.getModifiers()))
                .filter(method -> method.getParameterTypes().length == 0)
                .map(this::invokeStatic)
                .forEach(encoder -> assertTrue(encoder.matches("test", encoder.encode("test"))));
    }

    @SneakyThrows
    private MessageDigestPasswordEncoder invokeStatic(Method method) {
        return (MessageDigestPasswordEncoder) MethodHandles.publicLookup().unreflect(method).invoke();
    }

}
