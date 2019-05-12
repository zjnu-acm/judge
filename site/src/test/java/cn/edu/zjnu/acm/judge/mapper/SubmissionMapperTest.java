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
import cn.edu.zjnu.acm.judge.data.form.BestSubmissionForm;
import cn.edu.zjnu.acm.judge.data.form.SubmissionQueryForm;
import cn.edu.zjnu.acm.judge.domain.Submission;
import cn.edu.zjnu.acm.judge.util.Pageables;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author zhanhb
 */
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = Application.class)
@Transactional
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
        SubmissionQueryForm submissionCriteria = SubmissionQueryForm
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
        BestSubmissionForm form = BestSubmissionForm.builder().problemId(problemId).build();
        for (Pageable pageable : Pageables.bestSubmission()) {
            instance.bestSubmission(form, pageable);
        }
    }

}
