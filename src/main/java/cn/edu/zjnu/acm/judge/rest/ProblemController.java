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
package cn.edu.zjnu.acm.judge.rest;

import cn.edu.zjnu.acm.judge.config.JudgeConfiguration;
import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.exception.BusinessCode;
import cn.edu.zjnu.acm.judge.exception.BusinessException;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.ContestMapper;
import cn.edu.zjnu.acm.judge.mapper.ProblemMapper;
import cn.edu.zjnu.acm.judge.service.ContestService;
import cn.edu.zjnu.acm.judge.service.ProblemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Locale;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.StringUtils;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 *
 * @author zhanhb
 */
@RequestMapping(value = "/api/problems", produces = APPLICATION_JSON_VALUE)
@RestController
@Secured("ROLE_ADMIN")
@Slf4j
public class ProblemController {

    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private ProblemService problemService;
    @Autowired
    private ContestService contestService;
    @Autowired
    private JudgeConfiguration judgeConfiguration;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ContestMapper contestMapper;

    @PostMapping
    public Problem save(@RequestBody Problem problem) {
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

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") long id) {
        problemMapper.setDisabled(id, true);
    }

    @PostMapping("{id}/resume")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resume(@PathVariable("id") long id) {
        problemMapper.setDisabled(id, false);
    }

    @GetMapping("{id}/dataDir")
    public String dataDir(@PathVariable("id") long id) throws IOException {
        return objectMapper.writeValueAsString(judgeConfiguration.getDataDirectory(id).toString());
    }

    @GetMapping("{id}")
    public Problem findOne(@PathVariable("id") long id,
            @RequestParam(value = "locale", required = false) String lang) {
        Problem problem = problemMapper.findOne(id, convert(lang));
        if (problem == null) {
            throw new BusinessException(BusinessCode.NOT_FOUND);
        }
        return problem;
    }

    @GetMapping
    public Page<Problem> list(@PageableDefault(100) Pageable pageable, Locale locale) {
        log.info("pageable: {}", pageable);
        return problemService.findAll(pageable, locale);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") long problemId, @RequestBody Problem p,
            @RequestParam(value = "locale", required = false) String lang) {
        Problem problem = problemMapper.findOne(problemId, lang);
        if (problem == null) {
            throw new MessageException("no such problem " + problemId, HttpStatus.NOT_FOUND);
        }
        Long oldContestId = problem.getContest();
        Long contestId = p.getContest();
        String computedLang = convert(lang);

        if (!StringUtils.isEmpty(computedLang)) {
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
                .contest(p.getContest())
                .modifiedTime(Instant.now())
                .build(), computedLang);
        if (oldContestId != null && !Objects.equals(oldContestId, contestId)) {
            boolean started = contestMapper.findOneByIdAndDisabledFalse(oldContestId).isStarted();
            if (!started) {
                contestService.removeProblem(oldContestId, problemId);
            }
        }
        if (contestId != null) {
            contestMapper.updateProblemTitle(contestId, problemId);
            if (!Objects.equals(oldContestId, contestId)) {
                Contest newContest = contestMapper.findOneByIdAndDisabledFalse(contestId);
                if (newContest == null) {
                    throw new BusinessException(BusinessCode.NO_SUCH_CONTEST);
                }
                boolean started = newContest.isStarted();
                if (!started) {
                    contestService.addProblem(contestId, problemId);
                }
            }
        }
    }

    private String convert(String lang) {
        return "default".equalsIgnoreCase(lang) || "und".equalsIgnoreCase(lang) ? "" : lang;
    }

}
