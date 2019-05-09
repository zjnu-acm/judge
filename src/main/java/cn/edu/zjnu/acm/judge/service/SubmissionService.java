/*
 * Copyright 2019 ZJNU ACM.
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

import cn.edu.zjnu.acm.judge.domain.Submission;
import com.google.common.annotations.VisibleForTesting;
import java.util.concurrent.CompletableFuture;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author zhanhb
 */
public interface SubmissionService {

    Page<Submission> bestSubmission(Long contestId, long problemId, Pageable pageable, long total);

    boolean canView(HttpServletRequest request, Submission submission);

    CompletableFuture<?> contestSubmit(int languageId, String source, String userId, String ip, long contestId, long problemNum);

    void delete(long id);

    @Nullable
    String findCompileInfo(long submissionId);

    CompletableFuture<?> submit(int languageId, String source, String userId, String ip, long problemId, boolean addToPool);

    @VisibleForTesting
    void remove(String userId);

}
