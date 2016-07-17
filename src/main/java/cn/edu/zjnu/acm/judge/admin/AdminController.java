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
package cn.edu.zjnu.acm.judge.admin;

import cn.edu.zjnu.acm.judge.config.JudgeConfiguration;
import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.exception.MessageException;
import cn.edu.zjnu.acm.judge.mapper.ContestMapper;
import cn.edu.zjnu.acm.judge.mapper.ProblemMapper;
import cn.edu.zjnu.acm.judge.mapper.UserProblemMapper;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

/**
 *
 * @author zhanhb
 */
@Controller
@Secured("ROLE_ADMIN")
public class AdminController {

    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private ContestMapper contestMapper;
    @Autowired
    private UserProblemMapper userProblemMapper;
    @Autowired
    private JudgeConfiguration judgeConfiguration;

    @GetMapping("/admin/contests/new")
    protected String addcontestpage() {
        return "admin/addcontestpage";
    }

    @GetMapping("/admin")
    protected String index() {
        return "admin/index";
    }

    @GetMapping("/admin/problems/{problemId}/disable")
    protected String disableProblem(Model model, @PathVariable("problemId") long problemId) {

        problemMapper.setDisabled(problemId, true);

        model.addAttribute("problemId", problemId);
        return "admin/problems/disable";
    }

    // TODO request method
    @GetMapping("/admin/problems/{problemId}/enable")
    protected String enableProblem(Model model,
            @PathVariable("problemId") long problemId) {
        problemMapper.setDisabled(problemId, false);

        model.addAttribute("problemId", problemId);
        return "admin/problems/enable";
    }

    @PostMapping("/admin/contests")
    protected String addContest(
            int syear, int smonth, int sday, int shour, int sminute,
            int eyear, int emonth, int eday, int ehour, int eminute,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            Model model) {

        Instant startTime = LocalDateTime.of(syear, smonth, sday, shour, sminute).atZone(ZoneId.systemDefault()).toInstant();
        Instant endTime = LocalDateTime.of(eyear, emonth, eday, ehour, eminute).atZone(ZoneId.systemDefault()).toInstant();

        Contest contest = Contest.builder()
                .title(title)
                .description(description)
                .startTime(startTime)
                .endTime(endTime)
                .build();
        contestMapper.save(contest);

        model.addAttribute("contestId", contest.getId());
        model.addAttribute("startTime", startTime);
        model.addAttribute("endTime", endTime);

        return "admin/contests/add";
    }

    @PostMapping("/admin/tools")
    public String tools(
            @RequestParam(value = "op", required = false) String op,
            @RequestParam(value = "sinfo", required = false) String sinfo,
            @RequestParam(value = "pureText", required = false) String pureText) {
        if (StringUtils.isEmptyOrWhitespace(op)) {
            throw new MessageException("Nothing I can do for you.", HttpStatus.BAD_REQUEST);
        }

        switch (op.toLowerCase()) {
            case "test":
                userProblemMapper.init();
                userProblemMapper.updateProblems();
                userProblemMapper.updateUsers();
                break;
            case "setsysteminfo":
                if (StringUtils.isEmptyOrWhitespace(sinfo)) {
                    judgeConfiguration.setSystemInfo(null);
                } else if (pureText != null) {
                    judgeConfiguration.setSystemInfo(StringUtils.escapeXml(sinfo.trim()));
                } else {
                    judgeConfiguration.setSystemInfo(sinfo.trim());
                }
                break;
            default:
                throw new IllegalStateException("unknown operation '" + op + "'");
        }

        return "admin/recieved";
    }

}
