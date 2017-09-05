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
package cn.edu.zjnu.acm.judge.controller.api;

import cn.edu.zjnu.acm.judge.data.dto.ContestDto;
import cn.edu.zjnu.acm.judge.data.form.ContestAddProblemForm;
import cn.edu.zjnu.acm.judge.data.form.ContestForm;
import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.service.ContestService;
import java.util.List;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 *
 * @author zhanhb
 */
@RequestMapping(value = "/api/contests", produces = APPLICATION_JSON_VALUE)
@RestController
@Secured("ROLE_ADMIN")
@Slf4j
public class ContestController {

    @Autowired
    private ContestService contestService;

    @PostMapping
    public Contest save(@RequestBody Contest contest) {
        log.info("contest: {}", contest);
        return contestService.save(contest);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") long id) {
        contestService.delete(id);
    }

    @GetMapping
    public List<Contest> list(ContestForm form) {
        log.info("form: {}", form);
        return contestService.findAll(form);
    }

    @GetMapping("{id}")
    public ContestDto findOne(@PathVariable("id") long contestId, Locale locale) {
        return contestService.getContestAndProblems(contestId, null, locale);
    }

    @PostMapping("{id}/problems")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void add(@PathVariable("id") long id, @RequestBody ContestAddProblemForm[] body) {
        contestService.addProblems(id, body);
    }

    @DeleteMapping("{id}/problems/{problemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") long id, @PathVariable("problemId") long problemId) {
        contestService.removeProblem(id, problemId);
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") long id, @RequestBody Contest contest) {
        contestService.updateSelective(id, contest);
    }

}
