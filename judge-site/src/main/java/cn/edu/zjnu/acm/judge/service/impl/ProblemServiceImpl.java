/*
 * Copyright 2017-2019 ZJNU ACM.
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

import cn.edu.zjnu.acm.judge.data.form.ProblemForm;
import cn.edu.zjnu.acm.judge.domain.DomainLocale;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.exception.BusinessCode;
import cn.edu.zjnu.acm.judge.exception.BusinessException;
import cn.edu.zjnu.acm.judge.mapper.ProblemMapper;
import cn.edu.zjnu.acm.judge.mapper.UserProblemMapper;
import cn.edu.zjnu.acm.judge.service.ContestService;
import cn.edu.zjnu.acm.judge.service.LocaleService;
import cn.edu.zjnu.acm.judge.service.ProblemService;
import cn.edu.zjnu.acm.judge.service.SystemService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.attoparser.AbstractMarkupHandler;
import org.attoparser.MarkupParser;
import org.attoparser.ParseException;
import org.attoparser.config.ParseConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author zhanhb
 */
@RequiredArgsConstructor
@Service("problemService")
@Slf4j
public class ProblemServiceImpl implements ProblemService {

    private final ProblemMapper problemMapper;
    private final LocaleService localeService;
    private final ContestService contestService;
    private final SystemService systemService;
    private final UserProblemMapper userProblemMapper;

    @Override
    public Page<Problem> findAll(ProblemForm problemForm, String userId, Pageable pageable, Locale locale) {
        String resolve = localeService.resolve(locale);
        long total = problemMapper.count(problemForm, resolve);
        return new PageImpl<>(problemMapper.findAll(problemForm, userId, resolve, pageable), pageable, total);
    }

    @Override
    @Transactional
    public void updateSelective(long problemId, Problem p, @Nullable String requestLocale) {
        Problem problem = problemMapper.findOne(problemId, requestLocale);
        if (problem == null) {
            throw new BusinessException(BusinessCode.PROBLEM_NOT_FOUND, problemId);
        }
        String locale = convert(requestLocale);
        if (locale != null) {
            problemMapper.touchI18n(problemId, locale);
        }
        p.setCreatedTime(null);
        p.setModifiedTime(Instant.now());
        problemMapper.updateSelective(problemId, p, locale);
    }

    @Override
    public void updateImgUrl(long problemId, String imgSrc, String newImgSrc) {
        problemMapper.updateImgUrl(problemId, imgSrc, newImgSrc);
    }

    @Nonnull
    @Transactional
    @Override
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
    @Override
    public Problem findOne(long id, String lang) {
        Problem problem = problemMapper.findOne(id, convert(lang));
        if (problem == null) {
            throw new BusinessException(BusinessCode.PROBLEM_NOT_FOUND, id);
        }
        return problem;
    }

    @Nonnull
    @Override
    public Problem findOne(long id) {
        Problem problem = problemMapper.findOne(id, null);
        if (problem == null) {
            throw new BusinessException(BusinessCode.PROBLEM_NOT_FOUND, id);
        }
        return problem;
    }

    @Nonnull
    @Override
    public Problem findOneNoI18n(long id) {
        Problem problem = problemMapper.findOneNoI18n(id);
        if (problem == null) {
            throw new BusinessException(BusinessCode.PROBLEM_NOT_FOUND, id);
        }
        return problem;
    }

    @Override
    public Path getDataDirectory(long id) {
        return systemService.getDataDirectory(id);
    }

    @Transactional
    @Override
    public void delete(long id) {
        long total = problemMapper.deleteI18n(id)
                + userProblemMapper.deleteByProblem(id)
                + problemMapper.delete(id);
        if (total == 0) {
            throw new BusinessException(BusinessCode.PROBLEM_NOT_FOUND, id);
        }
    }

    @Override
    public List<String> attachment(long problemId, String requestLocale) {
        MarkupParser parser = new MarkupParser(ParseConfiguration.htmlConfiguration());
        Problem problem = findOne(problemId, requestLocale);
        String description = problem.getDescription();
        String input = problem.getInput();
        String output = problem.getOutput();
        String hint = problem.getHint();
        String source = problem.getSource();

        @SuppressWarnings("CollectionWithoutInitialCapacity")
        List<String> list = new ArrayList<>();
        for (String string : new String[]{
            description, input, output, hint, source
        }) {
            try {
                if (string == null) {
                    continue;
                }
                parser.parse(string, new AbstractMarkupHandler() {
                    @Override
                    public void handleAttribute(
                            char[] buffer,
                            int nameOffset, int nameLen, int nameLine, int nameCol,
                            int operatorOffset, int operatorLen, int operatorLine, int operatorCol,
                            int valueContentOffset, int valueContentLen,
                            int valueOuterOffset, int valueOuterLen, int valueLine, int valueCol) {
                        String name = new String(buffer, nameOffset, nameLen);
                        if (name.equalsIgnoreCase("href") || name.equalsIgnoreCase("src")) {
                            String value = new String(buffer, valueContentOffset, valueContentLen);
                            list.add(value);
                        }
                    }
                });
            } catch (ParseException ex) {
                log.error("", ex);
            }
        }
        return list;
    }

}
