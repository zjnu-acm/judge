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
package cn.edu.zjnu.acm.judge.exception;

import org.springframework.http.HttpStatus;

/**
 *
 * @author zhanhb
 */
public enum BusinessCode {

    NOT_FOUND("未找到", HttpStatus.NOT_FOUND),
    UNAUTHORIZED("未登录", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("没有权限", HttpStatus.FORBIDDEN),
    CONTEST_STARTED("比赛已经开始"),
    CONTEST_ENDED("比赛已经结束"),
    SUBMISSION_NOT_FOUND("No such solution", HttpStatus.NOT_FOUND),
    USER_SEARCH_KEYWORD_SHORT("onlinejudge.user.search.short"),
    USER_NOT_FOUND("onlinejudge.user.notfound", HttpStatus.NOT_FOUND),
    CONTEST_NOT_FOUND("onlinejudge.contest.nosuchcontest", HttpStatus.NOT_FOUND),
    CONTEST_ONLY_SUBMIT("onlinejudge.contest.only.submit"),
    CONTEST_ONLY_REGISTER("onlinejudge.contest.only.register"),
    CONTEST_ONLY_VIEW_SOURCE("onlinejudge.contest.only.view.souree"),
    PROBLEM_NOT_FOUND("onlinejudge.problem.notfound", HttpStatus.NOT_FOUND),
    LANGUAGE_NOT_FOUND("onlinejudge.language.notfound"),
    RESET_PASSWORD_FORBIDDEN("由于安全原因，无法重置管理员的密码", HttpStatus.FORBIDDEN);

    private final HttpStatus status;
    private final String message;

    BusinessCode(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }

    BusinessCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.status = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }

}
