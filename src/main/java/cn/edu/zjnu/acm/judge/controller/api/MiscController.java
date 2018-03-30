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

import cn.edu.zjnu.acm.judge.config.JudgeConfiguration;
import cn.edu.zjnu.acm.judge.data.dto.ValueHolder;
import cn.edu.zjnu.acm.judge.data.form.SystemInfoForm;
import cn.edu.zjnu.acm.judge.mapper.UserProblemMapper;
import cn.edu.zjnu.acm.judge.service.ContestOnlyService;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 *
 * @author zhanhb
 */
@RequestMapping(value = "/api/misc", produces = APPLICATION_JSON_VALUE)
@RestController
@Secured("ROLE_ADMIN")
public class MiscController {

    @Autowired
    private UserProblemMapper userProblemMapper;
    @Autowired
    private JudgeConfiguration judgeConfiguration;
    @Autowired
    private ContestOnlyService contestOnlyService;

    @PostMapping("fix")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CompletableFuture<?> fix() {
        userProblemMapper.init();
        CompletableFuture<?> b = CompletableFuture.runAsync(userProblemMapper::updateProblems);
        CompletableFuture<?> c = CompletableFuture.runAsync(userProblemMapper::updateUsers);
        return CompletableFuture.allOf(b, c);
    }

    @PutMapping("systemInfo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setSystemInfo(@RequestBody SystemInfoForm json) {
        String info = json.getInfo();
        boolean pureText = json.isPureText();
        SystemInfoForm systemInfo = new SystemInfoForm();
        if (StringUtils.hasText(info)) {
            systemInfo.setPureText(pureText);
            systemInfo.setInfo(info.trim());
        } else {
            systemInfo.setPureText(true);
        }
        judgeConfiguration.setSystemInfo(systemInfo);
    }

    @GetMapping("systemInfo")
    public SystemInfoForm systemInfo() {
        SystemInfoForm systemInfo = judgeConfiguration.getSystemInfo();
        if (systemInfo == null) {
            systemInfo = new SystemInfoForm();
            systemInfo.setPureText(true);
            return systemInfo;
        }
        if (!StringUtils.hasText(systemInfo.getInfo())) {
            systemInfo.setPureText(true);
        }
        return systemInfo;
    }

    @GetMapping("contestOnly")
    public ValueHolder<Long> contestOnly() {
        return new ValueHolder<>(contestOnlyService.getContestOnly());
    }

    @PutMapping("contestOnly")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setContestOnly(@RequestBody ValueHolder<Long> form) {
        contestOnlyService.setContestOnly(form.getValue());
    }

}
