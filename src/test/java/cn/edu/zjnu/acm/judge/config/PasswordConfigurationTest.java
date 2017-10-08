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

import cn.edu.zjnu.acm.judge.Application;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author zhanhb
 */
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
public class PasswordConfigurationTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testMatches() {
        log.info("matches");
        String rawPassword = "123456";
        String encodedPassword = "123456";
        String md5 = "7c4a8d09ca3762af61e59520943dc26494f8941b";
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
        encodedPassword = md5;
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
        encodedPassword = "123456,123456";
        assertFalse(passwordEncoder.matches(rawPassword, encodedPassword));
        String wrong = md5.substring(0, md5.length() - 3) + "ttt";
        encodedPassword = String.format("%s,%s", md5, wrong);
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
        encodedPassword = String.format("%s,%s", wrong, md5);
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
        encodedPassword = String.format("%s,%s", wrong, wrong);
        assertFalse(passwordEncoder.matches(rawPassword, encodedPassword));
        String p = md5;
        assertFalse(passwordEncoder.matches(p, p));
        p = p.substring(0, 20);
        assertTrue(passwordEncoder.matches(p, p));
        encodedPassword = String.format("%s,%s", rawPassword, rawPassword);
        assertFalse(passwordEncoder.matches(rawPassword, encodedPassword));
    }

    @Test
    public void testEncode() {
        log.info("passwordEncoder");
        String rawPassword = "123456";
        String result = passwordEncoder.encode(rawPassword);
        assertTrue(passwordEncoder.matches(rawPassword, result));

    }

    @Test
    public void testNull() {
        log.info("passwordEncoder");
        assertNull(passwordEncoder.encode(null));
        assertFalse(passwordEncoder.matches(null, ""));
        assertFalse(passwordEncoder.matches("", null));
        assertTrue(passwordEncoder.matches(null, null));
    }

}
