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

import cn.edu.zjnu.acm.judge.data.form.BestSubmissionForm;
import cn.edu.zjnu.acm.judge.util.Pageables;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author zhanhb
 */
@Slf4j
@Transactional
public class BestSubmissionsBuilderTest {

    /**
     * Test of bestSubmissions method, of class BestSubmissionsBuilder.
     */
    @Test
    public void testBestSubmissions() {
        log.info("bestSubmissions");
        for (Pageable pageable : Pageables.bestSubmission()) {
            BestSubmissionForm form = BestSubmissionForm.builder().problemId(1000).build();
            log.debug(BestSubmissionsBuilder.bestSubmissions(form, pageable));
        }
    }

}
