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
package cn.edu.zjnu.acm.judge.service;

import cn.edu.zjnu.acm.judge.util.ResultType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author zhanhb
 */
@Getter
@RequiredArgsConstructor
public enum Checker {
    ac(ResultType.SCORE_ACCEPT, ResultType.ACCEPTED),
    wa(0, ResultType.WRONG_ANSWER),
    tle(0, ResultType.TIME_LIMIT_EXCEED),
    mle(0, ResultType.MEMORY_LIMIT_EXCEED),
    ole(0, ResultType.OUTPUT_LIMIT_EXCEED),
    re(0, ResultType.RUNTIME_ERROR),
    pe(ResultType.SCORE_ACCEPT, ResultType.ACCEPTED); // site default pe as ac

    private final int score;
    private final int status;

}
