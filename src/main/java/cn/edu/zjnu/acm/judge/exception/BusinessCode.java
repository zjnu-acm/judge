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

/**
 *
 * @author zhanhb
 */
public enum BusinessCode {

    NOT_FOUND("未找到"),
    UNAUTHORIZED("未登录"),
    FORBIDDEN("没有权限"),
    NO_SUCH_CONTEST("onlinejudge.contest.nosuchcontest"),
    CONTEST_STARTED("比赛已经开始"),
    CONTEST_ENDED("比赛已经结束");

    private final String message;

    BusinessCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
