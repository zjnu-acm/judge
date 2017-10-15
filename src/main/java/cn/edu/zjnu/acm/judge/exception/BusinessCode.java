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
    CONTEST_STARTED("比赛已经开始"),
    CONTEST_NOT_STARTED("onlinejudge.contest.not.started"),
    CONTEST_ENDED("比赛已经结束"),
    SUBMISSION_NOT_FOUND("No such solution", HttpStatus.NOT_FOUND),
    USER_SEARCH_KEYWORD_SHORT("onlinejudge.user.search.short"),
    USER_NOT_FOUND("onlinejudge.user.notfound", HttpStatus.NOT_FOUND),
    CONTEST_NOT_FOUND("onlinejudge.contest.nosuchcontest", HttpStatus.NOT_FOUND),
    CONTEST_ONLY_SUBMIT("onlinejudge.contest.only.submit"),
    CONTEST_ONLY_REGISTER("onlinejudge.contest.only.register"),
    CONTEST_ONLY_VIEW_SOURCE("onlinejudge.contest.only.view.source"),
    PROBLEM_NOT_FOUND("onlinejudge.problem.notfound", HttpStatus.NOT_FOUND),
    CONTEST_PROBLEM_NOT_FOUND("onlinejudge.contest.problem.notfound", HttpStatus.NOT_FOUND),
    LANGUAGE_NOT_FOUND("onlinejudge.language.notfound"),
    IMPORT_USER_EMPTY("导入用户为空"),
    EMPTY_USER_ID("用户名为空"),
    EMPTY_PASSWORD("onlinejudge.password.empty"),
    PASSWORD_TOO_LONG("onlinejudge.password.long"),
    PASSWORD_TOO_SHORT("onlinejudge.password.short"),
    PASSWORD_HAS_SPACE("onlinejudge.password.space"),
    PASSWORD_INVALID_CHARACTER("onlinejudge.password.invalid"),
    INVALID_EXCEL("onlinejudge.excel.invalid"),
    RESET_PASSWORD_FORBIDDEN("由于安全原因，无法重置管理员的密码", HttpStatus.FORBIDDEN),
    IMPORT_USER_EXISTS("部分用户已经存在，请移除或选择存在策略后再导入"),
    IMPORT_USER_EXISTS_CHANGE("用户已经变化，请重新导入Excel", HttpStatus.CONFLICT),
    IMPORT_USER_RESET_PASSWORD_FORBIDDEN("由于安全原因，无法更新管理员的密码", HttpStatus.FORBIDDEN);

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
