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
import cn.edu.zjnu.acm.judge.domain.Submission;
import cn.edu.zjnu.acm.judge.domain.SubmissionCriteria;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 *
 * @author zhanhb
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class SubmissionMapperTest {

    @Autowired
    private SubmissionMapper instance;

    /**
     * Test of findAllByCriteria method, of class SubmissionMapper.
     */
    @Test
    public void testFindAllByCriteria() {
        log.info("findAllByCriteria");
        SubmissionCriteria submissionCriteria = SubmissionCriteria
                .builder()
                .contest(1058L)
                .problem(1449L)
                .size(50)
                .build();
        List<Submission> result = instance.findAllByCriteria(submissionCriteria);
        log.info("{}", result.size());
    }

    /**
     * Test of bestSubmission method, of class SubmissionMapper.
     */
    @Test
    public void testBestSubmission() {
        log.info("bestSubmission");
        long problemId = 1000;
        Sort sort = new Sort(Sort.Direction.DESC, "time", "memory", "code_length");
        PageRequest pageRequest = new PageRequest(5, 20, sort);
        instance.bestSubmission(problemId, pageRequest);
        sort = new Sort(Sort.Direction.DESC, "solution_id");
        pageRequest = new PageRequest(5, 20, sort);
        instance.bestSubmission(problemId, pageRequest);
        pageRequest = new PageRequest(9, 1);
        instance.bestSubmission(problemId, pageRequest);
    }

}
