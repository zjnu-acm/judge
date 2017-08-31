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
import cn.edu.zjnu.acm.judge.domain.DomainLocale;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.exception.BusinessCode;
import cn.edu.zjnu.acm.judge.exception.BusinessException;
import cn.edu.zjnu.acm.judge.mapper.ProblemMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    public Page<Problem> findAll(Pageable pageable, Locale locale) {
        long total = problemMapper.count();
        return new PageImpl<>(problemMapper.findAll(pageable, localeService.resolve(locale)), pageable, total);
    }

    public void update(long problemId, Problem p, String lang) {
        Problem problem = problemMapper.findOne(problemId, lang);
        if (problem == null) {
            throw new BusinessException(BusinessCode.NOT_FOUND);
        }
        String computedLang = convert(lang);
        if (computedLang != null) {
            problemMapper.touchI18n(problemId, computedLang);
        }
        problemMapper.update(problem.toBuilder()
                .title(p.getTitle())
                .description(p.getDescription())
                .input(p.getInput())
                .output(p.getOutput())
                .sampleInput(p.getSampleInput())
                .sampleOutput(p.getSampleOutput())
                .hint(p.getHint())
                .source(p.getSource())
                .timeLimit(p.getTimeLimit())
                .memoryLimit(p.getMemoryLimit())
                .modifiedTime(Instant.now())
                .build(), computedLang);
    }

    public Problem save(Problem problem) {
        problemMapper.save(problem);
        long id = problem.getId();
        Path problemDir = judgeConfiguration.getDataDirectory(id);
        Long contest = problem.getContest();
        if (contest != null) {
            contestService.addProblem(contest, id);
        }
        try {
            Files.createDirectories(problemDir);
        } catch (IOException ex) {
        }
        return problem;
    }

    private String convert(String lang) {
        DomainLocale domainLocale = localeService.findOne(lang);
        return domainLocale != null ? domainLocale.getId() : null;
    }

    public Problem findOne(long id, String lang) {
        Problem problem = problemMapper.findOne(id, convert(lang));
        if (problem == null) {
            throw new BusinessException(BusinessCode.NOT_FOUND);
        }
        return problem;
    }

    public void setDisabled(long id, boolean b) {
        problemMapper.setDisabled(id, b);
    }

    public Path getDataDirectory(long id) {
        return judgeConfiguration.getDataDirectory(id);
    }

}
