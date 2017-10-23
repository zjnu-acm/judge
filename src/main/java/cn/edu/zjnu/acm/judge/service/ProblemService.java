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

import cn.edu.zjnu.acm.judge.config.JudgeConfiguration;
import cn.edu.zjnu.acm.judge.data.form.ProblemForm;
import cn.edu.zjnu.acm.judge.domain.DomainLocale;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.exception.BusinessCode;
import cn.edu.zjnu.acm.judge.exception.BusinessException;
import cn.edu.zjnu.acm.judge.mapper.ProblemMapper;
import cn.edu.zjnu.acm.judge.mapper.UserProblemMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Locale;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author zhanhb
 */
@Service
public class ProblemService {

    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private LocaleService localeService;
    @Autowired
    private ContestService contestService;
    @Autowired
    private JudgeConfiguration judgeConfiguration;
    @Autowired
    private UserProblemMapper userProblemMapper;

    public Page<Problem> findAll(ProblemForm problemForm, String userId, Pageable pageable, Locale locale) {
        String resolve = localeService.resolve(locale);
        long total = problemMapper.count(problemForm, resolve);
        return new PageImpl<>(problemMapper.findAll(problemForm, userId, resolve, pageable), pageable, total);
    }

    public void updateSelective(long problemId, Problem p, String requestLocale) {
        Problem problem = problemMapper.findOne(problemId, requestLocale);
        if (problem == null) {
            throw new BusinessException(BusinessCode.PROBLEM_NOT_FOUND, problemId);
        }
        String locale = convert(requestLocale);
        if (locale != null) {
            problemMapper.touchI18n(problemId, locale);
        }
        p.setModifiedTime(Instant.now());
        problemMapper.updateSelective(problemId, p, locale);
    }

    @Nonnull
    @Transactional
    public Problem save(@Nonnull Problem problem) {
        boolean disabled = true;
        long[] contests = problem.getContests();
        if (contests != null) {
            for (long contest : contests) {
                if (contest == 0) {
                    disabled = false;
                }
            }
        }
        problem.setDisabled(disabled);
        problemMapper.save(problem);
        long id = problem.getId();
        if (contests != null) {
            for (long contest : contests) {
                if (contest != 0) {
                    contestService.addProblem(contest, id);
                }
            }
        }
        try {
            Files.createDirectories(getDataDirectory(id));
        } catch (IOException ex) {
        }
        return problem;
    }

    @Nullable
    private String convert(String lang) {
        DomainLocale domainLocale = localeService.findOne(lang);
        return domainLocale != null ? domainLocale.getId() : null;
    }

    @Nonnull
    public Problem findOne(long id, String lang) {
        Problem problem = problemMapper.findOne(id, convert(lang));
        if (problem == null) {
            throw new BusinessException(BusinessCode.PROBLEM_NOT_FOUND, id);
        }
        return problem;
    }

    @Nonnull
    public Problem findOne(long id) {
        Problem problem = problemMapper.findOne(id, null);
        if (problem == null) {
            throw new BusinessException(BusinessCode.PROBLEM_NOT_FOUND, id);
        }
        return problem;
    }

    @Nonnull
    public Problem findOneNoI18n(long id) {
        Problem problem = problemMapper.findOneNoI18n(id);
        if (problem == null) {
            throw new BusinessException(BusinessCode.PROBLEM_NOT_FOUND, id);
        }
        return problem;
    }

    public Path getDataDirectory(long id) {
        return judgeConfiguration.getDataDirectory(id);
    }

    @Transactional
    public void delete(long id) {
        long total = problemMapper.deleteI18n(id)
                + userProblemMapper.deleteByProblem(id)
                + problemMapper.delete(id);
        if (total == 0) {
            throw new BusinessException(BusinessCode.PROBLEM_NOT_FOUND, id);
        }
    }

}
