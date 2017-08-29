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
package cn.edu.zjnu.acm.judge.domain;

import cn.edu.zjnu.acm.judge.Application;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.*;

/**
 *
 * @author zhanhb
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class DomainLocaleTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testToJson() throws IOException {
        for (Locale locale : new Locale[]{
            new Locale("zh", "TW", "Hant"),
            Locale.US,
            Locale.ENGLISH,
            Locale.ROOT,
            null
        }) {
            DomainLocale expect = DomainLocale.builder().id(locale).name("test").build();
            String str = objectMapper.writeValueAsString(expect);
            DomainLocale result = objectMapper.readValue(str, DomainLocale.class);
            log.info("str={}, expect={}, result={}", str, expect, result);
            assertEquals(expect, result);
        }
    }

}
