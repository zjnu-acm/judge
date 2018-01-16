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
package com.github.zhanhb.judge.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author zhanhb
 */
@Getter
@RequiredArgsConstructor
public enum Checker {

    ac(Status.ACCEPTED),
    wa(Status.WRONG_ANSWER),
    tle(Status.TIME_LIMIT_EXCEED),
    mle(Status.MEMORY_LIMIT_EXCEED),
    ole(Status.OUTPUT_LIMIT_EXCEED),
    re(Status.RUNTIME_ERROR),
    pe(Status.PRESENTATION_ERROR);

    private final Status status;

}
