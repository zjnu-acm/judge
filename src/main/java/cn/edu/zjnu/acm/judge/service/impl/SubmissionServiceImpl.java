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
package cn.edu.zjnu.acm.judge.service.impl;

import cn.edu.zjnu.acm.judge.data.dto.SubmissionDetail;
import cn.edu.zjnu.acm.judge.data.form.BestSubmissionForm;
import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.domain.Submission;
import cn.edu.zjnu.acm.judge.exception.BusinessCode;
import cn.edu.zjnu.acm.judge.exception.BusinessException;
import cn.edu.zjnu.acm.judge.mapper.ContestMapper;
import cn.edu.zjnu.acm.judge.mapper.ProblemMapper;
import cn.edu.zjnu.acm.judge.mapper.SubmissionMapper;
import cn.edu.zjnu.acm.judge.mapper.UserPreferenceMapper;
import cn.edu.zjnu.acm.judge.service.ContestService;
import cn.edu.zjnu.acm.judge.service.JudgePoolService;
import cn.edu.zjnu.acm.judge.service.LanguageService;
import cn.edu.zjnu.acm.judge.service.ProblemService;
import cn.edu.zjnu.acm.judge.service.SubmissionService;
import cn.edu.zjnu.acm.judge.util.ResultType;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author zhanhb
 */
@RequiredArgsConstructor
@Service("submissionService")
public class SubmissionServiceImpl implements SubmissionService {

    private static final int SUBMIT_INTERVAL = 10;

    private final ContestMapper contestMapper;
    private final SubmissionMapper submissionMapper;
    private final JudgePoolService judgePoolService;
    private final ProblemMapper problemMapper;
    private final ProblemService problemService;
    private final UserPreferenceMapper userPerferenceMapper;
    private final LanguageService languageService;
    private final ContestService contestService;

    private final Set<String> cache = Collections.newSetFromMap(CacheBuilder.newBuilder().expireAfterWrite(SUBMIT_INTERVAL, TimeUnit.SECONDS).<String, Boolean>build().asMap());

    @Override
    public boolean canView(HttpServletRequest request, Submission submission) {
        if (UserDetailsServiceImpl.isAdminLoginned(request)) {
            return true;
        }
        // TODO cast to Authentication
        if (UserDetailsServiceImpl.isUser((Authentication) request.getUserPrincipal(), submission.getUser())) {
            return true;
        }
        boolean sourceBrowser = UserDetailsServiceImpl.isSourceBrowser(request);
        if (sourceBrowser) {
            Long contestId = submission.getContest();
            if (contestId == null) {
                return true;
            }
            Contest contest = contestMapper.findOne(contestId);
            return contest == null || contest.isEnded();
        }
        return false;
    }

    @Override
    public Page<Submission> bestSubmission(Long contestId, long problemId, Pageable pageable, long total) {
        BestSubmissionForm form = BestSubmissionForm.builder().contestId(contestId).problemId(problemId).build();
        List<Submission> bestSubmissions = submissionMapper.bestSubmission(form, pageable);
        return new PageImpl<>(bestSubmissions, pageable, total);
    }

    private void check(int languageId, String source, String userId) {
        languageService.getAvailableLanguage(languageId);
        if (source.length() > 32768) {
            throw new BusinessException(BusinessCode.SOURCE_CODE_LONG);
        }
        if (source.length() < 10) {
            throw new BusinessException(BusinessCode.SOURCE_CODE_SHORT);
        }
        // 10秒交一次。。。
        if (cache.contains(userId) || !cache.add(userId)) {
            throw new BusinessException(BusinessCode.SUBMISSION_FREQUENTLY, SUBMIT_INTERVAL);
        }
    }

    @Override
    public CompletableFuture<?> submit(int languageId, String source, String userId,
            String ip, long problemId, boolean addToPool) {
        check(languageId, source, userId);
        problemService.findOneNoI18n(problemId); //检查该题是否存在
        return submit0(Submission.builder()
                .problem(problemId)
                .ip(ip), source, userId, languageId, addToPool);
    }

    @Override
    public CompletableFuture<?> contestSubmit(int languageId, String source, String userId, String ip, long contestId, long problemNum) {
        check(languageId, source, userId);
        Contest contest = contestService.findOneByIdAndNotDisabled(contestId);
        // contest not started yet, can't submit the problem.
        if (!contest.isStarted()) {
            throw new BusinessException(BusinessCode.CONTEST_PROBLEM_NOT_FOUND, contestId, problemNum);
        }
        Problem problem = contestService.getProblem(contestId, problemNum, null);
        return submit0(Submission.builder()
                // submit problem as normal when contest is ended.
                .contest(contest.isEnded() ? null : contestId)
                .problem(problem.getOrigin())
                .ip(ip), source, userId, languageId, true);
    }

    private CompletableFuture<?> submit0(Submission.Builder builder, String source,
            String userId, int languageId, boolean addToPool) {
        Instant now = Instant.now();
        Submission submission = builder
                .user(userId)
                .inDate(now)
                .sourceLength(source.length())
                .language(languageId)
                .score(ResultType.QUEUING)
                .build();
        // 插入solution数据库表
        submissionMapper.save(submission);
        long submissionId = submission.getId();
        problemMapper.setInDate(submission.getProblem(), now);

        // 插入source_code表
        submissionMapper.saveSource(submissionId, source);
        userPerferenceMapper.setLanguage(userId, languageId);
        if (addToPool) {
            return judgePoolService.add(submissionId);
        }
        return CompletableFuture.completedFuture(null);
    }

    @Nullable
    @Override
    public String findCompileInfo(long submissionId) {
        Submission submission = submissionMapper.findOne(submissionId);
        if (submission == null) {
            throw new BusinessException(BusinessCode.SUBMISSION_NOT_FOUND, submissionId);
        }
        return submissionMapper.findCompileInfoById(submissionId);
    }

    @Transactional
    @Override
    public void delete(long id) {
        int result = submissionMapper.deleteSource(id)
                + submissionMapper.deleteCompileinfo(id)
                + submissionMapper.deleteDetail(id)
                + submissionMapper.deleteSolution(id);
        if (result == 0) {
            throw new BusinessException(BusinessCode.SUBMISSION_NOT_FOUND, id);
        }
    }

    @Override
    @VisibleForTesting
    public void remove(String userId) {
        cache.remove(userId);
    }

    @Override
    public List<SubmissionDetail> getSubmissionDetail(long submissionId) {
        String submissionDetail = submissionMapper.getSubmissionDetail(submissionId);
        if (submissionDetail == null) {
            return ImmutableList.of();
        }
        String[] detailsArray = submissionDetail.split(",");
        SubmissionDetail[] details = new SubmissionDetail[detailsArray.length / 4];
        for (int i = 0; i < detailsArray.length / 4; ++i) {
            details[i] = SubmissionDetail.builder()
                    .result(ResultType.getCaseScoreDescription(Integer.parseInt(detailsArray[i << 2])))
                    .score(detailsArray[i << 2 | 1])
                    .time(detailsArray[i << 2 | 2])
                    .memory(detailsArray[i << 2 | 3])
                    .build();
        }
        return ImmutableList.copyOf(details);
    }

}
