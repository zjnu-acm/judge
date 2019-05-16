/*
 * Copyright 2015-2019 ZJNU ACM.
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

import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.domain.Submission;
import cn.edu.zjnu.acm.judge.domain.SubmissionDetail;
import cn.edu.zjnu.acm.judge.exception.BusinessCode;
import cn.edu.zjnu.acm.judge.exception.BusinessException;
import cn.edu.zjnu.acm.judge.mapper.SubmissionDetailMapper;
import cn.edu.zjnu.acm.judge.mapper.SubmissionMapper;
import cn.edu.zjnu.acm.judge.mapper.UserProblemMapper;
import cn.edu.zjnu.acm.judge.sandbox.SimpleValidator;
import cn.edu.zjnu.acm.judge.sandbox.Status;
import cn.edu.zjnu.acm.judge.sandbox.Validator;
import cn.edu.zjnu.acm.judge.sandbox.win32.SpecialValidator;
import cn.edu.zjnu.acm.judge.service.JudgeRunner;
import cn.edu.zjnu.acm.judge.service.JudgeService;
import cn.edu.zjnu.acm.judge.service.LanguageService;
import cn.edu.zjnu.acm.judge.service.ProblemService;
import cn.edu.zjnu.acm.judge.service.SystemService;
import cn.edu.zjnu.acm.judge.support.JudgeData;
import cn.edu.zjnu.acm.judge.support.JudgeException;
import cn.edu.zjnu.acm.judge.support.RunRecord;
import cn.edu.zjnu.acm.judge.support.RunResult;
import cn.edu.zjnu.acm.judge.util.ResultType;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author zhanhb
 */
@RequiredArgsConstructor
@Service("judgeService")
@Slf4j
public class JudgeServiceImpl implements JudgeService {

    private final SubmissionMapper submissionMapper;
    private final SubmissionDetailMapper submissionDetailMapper;
    private final UserProblemMapper userProblemMapper;
    private final ProblemService problemService;
    private final SystemService systemService;
    private final LanguageService languageService;
    private final JudgeRunner judgeRunner;

    private void updateSubmissionStatus(String userId, long problemId) {
        userProblemMapper.update(userId, problemId);
        userProblemMapper.updateUser(userId);
        userProblemMapper.updateProblem(problemId);
    }

    @Override
    public void execute(long submissionId) {
        Submission submission = submissionMapper.findOne(submissionId);
        if (submission == null) {
            throw new BusinessException(BusinessCode.SUBMISSION_NOT_FOUND);
        }
        long problemId = submission.getProblem();
        Problem problem = problemService.findOneNoI18n(problemId);
        try {
            RunRecord runRecord = RunRecord.builder()
                    .language(languageService.getAvailableLanguage(submission.getLanguage()))
                    .source(submissionDetailMapper.findSourceById(submissionId))
                    .memoryLimit(problem.getMemoryLimit())
                    .timeLimit(problem.getTimeLimit())
                    .build();
            Path dataDirectory = systemService.getDataDirectory(problemId);
            JudgeData judgeData = new JudgeData(dataDirectory);
            Path specialFile = systemService.getSpecialJudgeExecutable(problemId);
            boolean isSpecial = systemService.isSpecialJudge(problemId);
            Path work = systemService.getWorkDirectory(submissionId); //建立临时文件
            final Validator validator = isSpecial
                    ? new SpecialValidator(specialFile.toString(), work)
                    : SimpleValidator.PE_AS_ACCEPTED;
            boolean deleteTempFile = systemService.isDeleteTempFile();
            RunResult runResult = judgeRunner.run(runRecord, work, judgeData, validator, deleteTempFile);
            SubmissionDetail detail = SubmissionDetail.builder().id(submissionId)
                    .compileInfo(runResult.getCompileInfo())
                    .detail(runResult.getDetail())
                    .systemInfo(runResult.getSystemInfo())
                    .build();
            if (runResult.getType() == Status.COMPILATION_ERROR) {
                submissionMapper.updateResult(submissionId, ResultType.COMPILE_ERROR, 0, 0);
                submissionDetailMapper.update(detail);
            } else {
                int score = runResult.getScore();
                long time = runResult.getTime();
                long memory = runResult.getMemory();
                submissionMapper.updateResult(submissionId, score, time, memory);
                submissionDetailMapper.update(detail);
            }
            updateSubmissionStatus(submission.getUser(), problemId);
        } catch (JudgeException | IOException | Error ex) {
            log.error("got an exception when judging submission {}", submissionId, ex);
            submissionMapper.updateResult(submissionId, ResultType.SYSTEM_ERROR, 0, 0);
            StringWriter sw = new StringWriter();
            try (PrintWriter pw = new PrintWriter(sw)) {
                ex.printStackTrace(pw);
            }
            submissionDetailMapper.update(SubmissionDetail.builder().id(submissionId)
                    .systemInfo(sw.toString()).build());
        }
    }

}
