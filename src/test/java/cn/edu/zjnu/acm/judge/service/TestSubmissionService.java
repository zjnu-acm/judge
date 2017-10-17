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
package cn.edu.zjnu.acm.judge.service;

import cn.edu.zjnu.acm.judge.exception.BusinessCode;
import cn.edu.zjnu.acm.judge.exception.BusinessException;
import cn.edu.zjnu.acm.judge.mapper.TestSubmissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author zhanhb
 */
@Service
public class TestSubmissionService {

    @Autowired
    private TestSubmissionMapper submissionMapper;

    public void delete(long id) {
        int result = submissionMapper.deleteSource(id)
                + submissionMapper.deleteCompileinfo(id)
                + submissionMapper.deleteDetail(id)
                + submissionMapper.deleteSolution(id);
        if (result == 0) {
            throw new BusinessException(BusinessCode.SUBMISSION_NOT_FOUND, id);
        }
    }

}
