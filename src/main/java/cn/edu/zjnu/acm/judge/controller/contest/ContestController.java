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
package cn.edu.zjnu.acm.judge.controller.contest;

import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.exception.EntityNotFoundException;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.ContestMapper;
import cn.edu.zjnu.acm.judge.service.LocaleService;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Future;
import java.util.function.ObjIntConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

/**
 * @author zhanhb
 */
@Controller("contest")
@RequestMapping("/contests/{contestId}")
public class ContestController {

    private static final ConcurrentMap<Long, CompletableFuture<UserStanding[]>> STANDINGS = new ConcurrentHashMap<>(20);

    @Autowired
    private ContestMapper contestMapper;
    @Autowired
    private LocaleService localeService;

    @SuppressWarnings("ValueOfIncrementOrDecrementUsed")
    private <T> void setIndexes(T[] standings, Comparator<T> c, ObjIntConsumer<T> consumer) {
        Objects.requireNonNull(standings, "standings");
        Objects.requireNonNull(c, "c");
        Objects.requireNonNull(consumer, "consumer");
        int i = 0, len = standings.length, lastIndex = 0;

        for (T last = null, standing; i < len; last = standing) {
            standing = standings[i++];
            if (c.compare(standing, last) != 0) {
                lastIndex = i;
            }
            consumer.accept(standing, lastIndex);
        }
    }

    @ResponseBody
    @GetMapping(value = "standing", produces = APPLICATION_JSON_VALUE)
    public CompletableFuture<UserStanding[]> standing(@PathVariable("contestId") long contestId) {
        return STANDINGS.computeIfAbsent(contestId, id -> CompletableFuture.supplyAsync(() -> {
            Map<String, UserStanding> hashMap = new HashMap<>(80);
            contestMapper.standing(id).forEach(standing
                    -> hashMap.computeIfAbsent(standing.getUser(), UserStanding::new)
                            .add(standing.getProblem(), standing.getTime(), standing.getPenalty())
            );
            contestMapper.attenders(id).forEach(attender
                    -> Optional.ofNullable(hashMap.get(attender.getId()))
                            .ifPresent(us -> us.setNick(attender.getNick()))
            );
            UserStanding[] standings = hashMap.values().stream().sorted(UserStanding.COMPARATOR).toArray(UserStanding[]::new);
            setIndexes(standings, Comparator.nullsFirst(UserStanding.COMPARATOR), UserStanding::setIndex);
            STANDINGS.remove(id);
            return standings;
        }));
    }

    @GetMapping(value = "standing", produces = {TEXT_HTML_VALUE, ALL_VALUE})
    public Future<ModelAndView> standingHtml(@PathVariable("contestId") long contestId, Locale locale) {
        return standing(contestId).thenApplyAsync(standing -> {
            Contest contest = contestMapper.findOneByIdAndNotDisabled(contestId);
            // TODO
            ModelAndView modelAndView = new ModelAndView("contests/standing");
            ModelMap model = modelAndView.getModelMap();
            model.addAttribute("contestId", contestId);
            model.addAttribute("contest", contest);
            if (contest == null) {
                throw new MessageException("onlinejudge.contest.nosuchcontest", HttpStatus.NOT_FOUND);
            }
            if (!contest.isStarted()) {
                throw new MessageException("Contest not started yet", HttpStatus.OK);
            }
            // TODO user is empty
            List<Problem> problems = contestMapper.getProblems(contestId, null, localeService.resolve(locale));
            model.addAttribute("id", contestId);
            model.addAttribute("problems", problems);
            model.addAttribute("standing", standing);
            return modelAndView;
        });
    }

    @GetMapping
    public String index(@PathVariable("contestId") long contestId, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("contestId", contestId);
        return "redirect:/contests/{contestId}/problems";
    }

    @GetMapping("problems")
    public String problems(@PathVariable("contestId") long contestId, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("contest_id", contestId);
        return "redirect:/showcontest";
    }

    @GetMapping("problems/{pid}")
    public String showProblem(@PathVariable("contestId") long contestId,
            @PathVariable("pid") long problemNum,
            RedirectAttributes redirectAttributes) {
        Problem problem = contestMapper.getProblem(contestId, problemNum);
        if (problem == null) {
            throw new EntityNotFoundException();
        }
        redirectAttributes.addAttribute("problem_id", problem.getOrigin());
        return "redirect:/showproblem";
    }

}
