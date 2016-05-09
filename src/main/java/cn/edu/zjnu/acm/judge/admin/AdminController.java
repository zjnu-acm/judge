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
import cn.edu.zjnu.acm.judge.service.UserDetailService;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author zhanhb
 */
@Controller
public class AdminController {

    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private ContestMapper contestMapper;
    @Autowired
    private UserProblemMapper userProblemMapper;
    @Autowired
    private JudgeConfiguration judgeConfiguration;

    @RequestMapping(value = "/admin/contests/new", method = {RequestMethod.GET, RequestMethod.HEAD})
    protected String addcontestpage(HttpServletRequest request) {
        UserDetailService.requireAdminLoginned(request);
        return "admin/addcontestpage";
    }

    @RequestMapping(value = "/admin", method = {RequestMethod.GET, RequestMethod.HEAD})
    protected String index(HttpServletRequest request) {
        UserDetailService.requireAdminLoginned(request);
        return "admin/index";
    }

    @RequestMapping(value = "/admin/problems/{problemId}/disable", method = {RequestMethod.GET, RequestMethod.HEAD})
    protected String disableProblem(HttpServletRequest request,
            @PathVariable("problemId") long problemId) {
        UserDetailService.requireAdminLoginned(request);

        problemMapper.disable(problemId);

        request.setAttribute("problemId", problemId);
        return "admin/problems/delete";
    }

    // TODO request method
    @RequestMapping(value = "/admin/problems/{problemId}/resume", method = {RequestMethod.GET, RequestMethod.HEAD})
    protected String resumeProblem(HttpServletRequest request,
            @PathVariable("problemId") long problemId) {
        UserDetailService.requireAdminLoginned(request);
        problemMapper.enable(problemId);

        request.setAttribute("problemId", problemId);
        return "admin/problems/resume";
    }

    @RequestMapping(value = "/admin/contests", method = RequestMethod.POST)
    protected String addContest(
            int syear, int smonth, int sday, int shour, int sminute,
            int eyear, int emonth, int eday, int ehour, int eminute,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            HttpServletRequest request) {
        UserDetailService.requireAdminLoginned(request);

        Instant startTime = LocalDateTime.of(syear, smonth, sday, shour, sminute).atZone(ZoneId.systemDefault()).toInstant();
        Instant endTime = LocalDateTime.of(eyear, emonth, eday, ehour, eminute).atZone(ZoneId.systemDefault()).toInstant();

        Contest contest = Contest.builder()
                .title(title)
                .description(description)
                .startTime(startTime)
                .endTime(endTime)
                .build();
        contestMapper.save(contest);

        request.setAttribute("contestId", contest.getId());
        request.setAttribute("startTime", startTime);
        request.setAttribute("endTime", endTime);

        return "admin/contests/add";
    }

    @RequestMapping(value = "/admin/tools", method = RequestMethod.POST)
    public String tools(HttpServletRequest request,
            @RequestParam(value = "op", required = false) String op,
            @RequestParam(value = "sinfo", required = false) String sinfo,
            @RequestParam(value = "pureText", required = false) String pureText) {
        UserDetailService.requireAdminLoginned(request);
        if (StringUtils.isBlank(op)) {
            throw new MessageException("Nothing I can do for you.");
        }

        switch (op.toLowerCase()) {
            case "test":
                userProblemMapper.init();
                userProblemMapper.updateProblems();
                userProblemMapper.updateUsers();
                break;
            case "setsysteminfo":
                if (StringUtils.isBlank(sinfo)) {
                    judgeConfiguration.setSystemInfo(null);
                } else if (pureText != null) {
                    judgeConfiguration.setSystemInfo(StringEscapeUtils.escapeHtml4(sinfo.trim()));
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
