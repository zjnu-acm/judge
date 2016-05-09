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
package cn.edu.zjnu.acm.judge.contest;

import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.domain.Problem;
import cn.edu.zjnu.acm.judge.exception.EntityNotFoundException;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.ContestMapper;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.ObjIntConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

/**
 * @author zhanhb
 */
@Slf4j
@Controller
@RequestMapping("/contests/{contestId}")
public class ContestController {

    @Autowired
    private ContestMapper contestMapper;

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
    @RequestMapping(value = "standing", produces = APPLICATION_JSON_VALUE, method = {RequestMethod.GET, RequestMethod.HEAD})
    public UserStanding[] standing(@PathVariable("contestId") long contestId) {
        Map<String, UserStanding> hashMap = new HashMap<>(80);
        contestMapper.standing(contestId).forEach(standing
                -> hashMap.computeIfAbsent(standing.getUser(), UserStanding::new)
                .add(standing.getProblem(), standing.getTime(), standing.getPenalty())
        );
        contestMapper.attenders(contestId).forEach(attender
                -> Optional.ofNullable(hashMap.get(attender.getId()))
                .ifPresent(us -> us.setNick(attender.getNick()))
        );
        UserStanding[] standings = hashMap.values().stream().sorted(UserStanding.COMPARATOR).toArray(UserStanding[]::new);
        setIndexes(standings, Comparator.nullsFirst(UserStanding.COMPARATOR), UserStanding::setIndex);
        return standings;
    }

    @RequestMapping(value = "standing", produces = {TEXT_HTML_VALUE, ALL_VALUE}, method = {RequestMethod.GET, RequestMethod.HEAD})
    public String standingHtml(@PathVariable("contestId") long id, Model model) {
        Contest contest = contestMapper.findOneByIdAndDefunctN(id);
        // TODO
        model.addAttribute("contestId", id);
        model.addAttribute("contest", contest);
        if (contest == null) {
            throw new MessageException("No such contest");
        }
        if (!contest.isStarted()) {
            throw new MessageException("Contest not started yet");
        }
        // TODO user is empty
        List<Problem> problems = contestMapper.getProblems(id, null);
        model.addAttribute("id", id);
        model.addAttribute("problems", problems);
        model.addAttribute("standing", standing(id));
        return "contests/standing";
    }

    //@ResponseBody
    //@RequestMapping(value = "problems", produces = APPLICATION_JSON_VALUE, method = {RequestMethod.GET, RequestMethod.HEAD})
    public List<Problem> problems(@PathVariable("contestId") long contest) {
        // TODO user is empty
        return contestMapper.getProblems(contest, null);
    }

    @RequestMapping(value = "", method = {RequestMethod.GET, RequestMethod.HEAD})
    public String index(@PathVariable("contestId") long contestId, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("contestId", contestId);
        return "redirect:/contests/{contestId}/problems";
    }

    @RequestMapping(value = "problems/{pid}", method = {RequestMethod.GET, RequestMethod.HEAD})
    public String showProblem(@PathVariable("contestId") long contestId,
            @PathVariable("pid") long problemOrder,
            RedirectAttributes redirectAttributes) {
        Problem problem = contestMapper.getProblem(contestId, problemOrder);
        if (problem == null) {
            throw new EntityNotFoundException();
        }
        redirectAttributes.addAttribute("problem_id", problem.getOrign());
        return "redirect:/showproblem";
    }

}
