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
package cn.edu.zjnu.acm.judge.mapper;

import cn.edu.zjnu.acm.judge.Application;
import cn.edu.zjnu.acm.judge.domain.Problem;
import java.util.List;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author zhanhb
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class ProblemMapperTest {

    @Autowired
    private ProblemMapper instance;
    private final Locale locale = Locale.SIMPLIFIED_CHINESE;

    /**
     * Test of findOneByIdAndDefunctN method, of class ProblemMapper.
     */
    @Test
    public void testFindOneByIdAndDefunctN() {
        log.debug("findOneByIdAndDefunctN");
        long pid = 0L;
        Problem expResult = null;
        Problem result = instance.findOneByIdAndDefunctN(pid, locale.getLanguage());
    }

    /**
     * Test of findAll method, of class ProblemMapper.
     */
    @Test
    public void testFindAll() {
        log.debug("findAll");
        Pageable pageable = new PageRequest(0, 20);
        List<Problem> result = instance.findAll(pageable, locale.getLanguage());
    }

    /**
     * Test of findAllByDefunctN method, of class ProblemMapper.
     */
    @Test
    public void testFindAllByDefunctN() {
        log.debug("findAllByDefunctN");
        String userId = "coach";
        long start = 1000L;
        long end = 1099L;
        List<Problem> result = instance.findAllByDefunctN(userId, start, end, locale.getLanguage());
        result = instance.findAllByDefunctN(null, start, end, locale.getLanguage());
    }

    /**
     * Test of findOne method, of class ProblemMapper.
     */
    @Test
    public void testFindOne() {
        log.debug("findOne");
        Problem expResult = null;
        Problem result = instance.findOne(0);
        result = instance.findOne(1000);
    }

    /**
     * Test of findOne method, of class ProblemMapper.
     */
    @Test
    public void testFindOneNoI18n() {
        log.debug("findOne");
        long id = 0L;
        Problem expResult = null;
        Problem result = instance.findOneNoI18n(0);
        result = instance.findOneNoI18n(1000);
    }

    /**
     * Test of findAllBySearchTitleOrSourceAndDefunctN method, of class
     * ProblemMapper.
     */
    @Test
    public void testFindAllBySearchTitleOrSourceAndDefunctN() {
        log.debug("findAllBySearchTitleOrSourceAndDefunctN");
        String query = "初级";
        String userId = "coach";
        List<Problem> result = instance.findAllBySearchTitleOrSourceAndDefunctN(query, userId, locale.getLanguage());
        assertTrue(!result.isEmpty());
        int size = result.size();
        result = instance.findAllBySearchTitleOrSourceAndDefunctN(query, null, locale.getLanguage());
        assertEquals(size, result.size());
    }

}
