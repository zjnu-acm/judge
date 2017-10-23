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
package cn.edu.zjnu.acm.judge.service;

import cn.edu.zjnu.acm.judge.data.form.BestSubmissionForm;
import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.domain.Submission;
import cn.edu.zjnu.acm.judge.exception.BusinessCode;
import cn.edu.zjnu.acm.judge.exception.BusinessException;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.ContestMapper;
import cn.edu.zjnu.acm.judge.mapper.ProblemMapper;
import cn.edu.zjnu.acm.judge.mapper.SubmissionMapper;
import cn.edu.zjnu.acm.judge.mapper.UserPreferenceMapper;
import cn.edu.zjnu.acm.judge.util.ResultType;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.cache.CacheBuilder;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
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
@Service
public class SubmissionService {

    @Autowired
    private ContestMapper contestMapper;
    @Autowired
    private SubmissionMapper submissionMapper;
    @Autowired
    private JudgePool judgePool;
    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private ProblemService problemService;
    @Autowired
    private UserPreferenceMapper userPerferenceMapper;
    @Autowired
    private LanguageService languageService;
    @Autowired
    private ContestService contestService;

    private final Set<String> cache = Collections.newSetFromMap(CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.SECONDS).<String, Boolean>build().asMap());

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

    public Page<Submission> bestSubmission(Long contestId, long problemId, Pageable pageable, long total) {
        BestSubmissionForm form = BestSubmissionForm.builder().contestId(contestId).problemId(problemId).build();
        List<Submission> bestSubmissions = submissionMapper.bestSubmission(form, pageable);
        return new PageImpl<>(bestSubmissions, pageable, total);
    }

    private void check(int languageId, String source, String userId) {
        languageService.getAvailableLanguage(languageId);
        if (source.length() > 32768) {
            throw new MessageException("Source code too long, submit FAILED;if you really need submit this source please contact administrator");
        }
        if (source.length() < 10) {
            throw new MessageException("Source code too short, submit FAILED;if you really need submit this source please contact administrator");
        }
        // 10秒交一次。。。
        if (cache.contains(userId) || !cache.add(userId)) {
            throw new MessageException("Sorry, please don't submit again within 10 seconds.");
        }
    }

    public CompletableFuture<?> submit(int languageId, String source, String userId, String ip, long problemId) {
        check(languageId, source, userId);
        problemService.findOneNoI18n(problemId); //检查该题是否存在
        return submit0(Submission.builder()
                .problem(problemId)
                .ip(ip), source, userId, languageId);
    }

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
                .ip(ip), source, userId, languageId);
    }

    private CompletableFuture<?> submit0(Submission.Builder builder, String source, String userId, int languageId) {
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
        return judgePool.add(submissionId);
    }

    public String findCompileInfo(long submissionId) {
        Submission submission = submissionMapper.findOne(submissionId);
        if (submission == null) {
            throw new BusinessException(BusinessCode.SUBMISSION_NOT_FOUND, submissionId);
        }
        return submissionMapper.findCompileInfoById(submissionId);
    }

    @Transactional
    public void delete(long id) {
        int result = submissionMapper.deleteSource(id)
                + submissionMapper.deleteCompileinfo(id)
                + submissionMapper.deleteDetail(id)
                + submissionMapper.deleteSolution(id);
        if (result == 0) {
            throw new BusinessException(BusinessCode.SUBMISSION_NOT_FOUND, id);
        }
    }

    @VisibleForTesting
    void remove(String userId) {
        cache.remove(userId);
    }

}
