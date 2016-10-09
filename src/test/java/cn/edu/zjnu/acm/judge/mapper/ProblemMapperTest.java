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
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author zhanhb
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class ProblemMapperTest {

    @Autowired
    private ProblemMapper instance;
    private String lang = "en";

    void setLang(String lang) {
        this.lang = lang;
    }

    /**
     * Test of findOneByIdAndDisabledFalse method, of class ProblemMapper.
     */
    @Test
    public void testFindOneByIdAndDisabledFalse() {
        log.debug("findOneByIdAndDisabledFalse");
        long pid = 0L;
        instance.findOneByIdAndDisabledFalse(pid, lang);
    }

    /**
     * Test of findAll method, of class ProblemMapper.
     */
    @Test
    public void testFindAll() {
        log.debug("findAll");
        Pageable pageable = new PageRequest(0, 20);
        instance.findAll(pageable, lang);
    }

    /**
     * Test of findAllByDisabledFalse method, of class ProblemMapper.
     */
    @Test
    public void testFindAllByDisabledFalse() {
        log.debug("findAllByDisabledFalse");
        String userId = "coach";
        long start = 1000L;
        long end = 1099L;
        instance.findAllByDisabledFalse(userId, start, end, lang);
        instance.findAllByDisabledFalse(null, start, end, lang);
    }

    /**
     * Test of findOne method, of class ProblemMapper.
     */
    @Test
    public void testFindOne() {
        log.debug("findOne");
        instance.findOne(0, lang);
        instance.findOne(1000, lang);
    }

    /**
     * Test of findOne method, of class ProblemMapper.
     */
    @Test
    public void testFindOneNoI18n() {
        log.debug("findOne");
        instance.findOneNoI18n(0);
        instance.findOneNoI18n(1000);
    }

    /**
     * Test of findAllBySearchTitleOrSourceAndDisabledFalse method, of class
 ProblemMapper.
     */
    @Test
    public void testFindAllBySearchTitleOrSourceAndDisabledFalse() {
        log.debug("findAllBySearchTitleOrSourceAndDisabledFalse");
        String query = "初级";
        String userId = "coach";
        List<Problem> result = instance.findAllBySearchTitleOrSourceAndDisabledFalse(query, userId, lang);
        int size = result.size();
        result = instance.findAllBySearchTitleOrSourceAndDisabledFalse(query, null, lang);
        assertEquals(size, result.size());
    }

    /**
     * Test of touchI18n method, of class ProblemMapper.
     */
    @Test
    public void testTouchI18n() {
        log.info("touchI18n");
        long problemId = 0L;
        instance.touchI18n(problemId, lang);
    }

    /**
     * Test of updateI18n method, of class ProblemMapper.
     */
    @Test
    public void testUpdateI18n() {
        log.info("updateI18n");
        long problemId = 0L;
        Problem problem = new Problem();
        problem = problem.toBuilder()
                .title("")
                .input("")
                .output("")
                .description("")
                .hint("")
                .source("")
                .build();
        instance.updateI18n(problemId, lang, problem);
        problem = new Problem().toBuilder().title("").build();
        instance.updateI18n(problemId, lang, problem);
    }

}
