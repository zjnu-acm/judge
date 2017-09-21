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
package cn.edu.zjnu.acm.judge.data.form;

import java.util.EnumSet;
import java.util.Locale;

/**
 *
 * @author zhanhb
 */
public enum ContestStatus {

    PENDING, RUNNING, ENDED, ERROR;

    public static EnumSet<ContestStatus> parse(String[] filter) {
        EnumSet<ContestStatus> set = EnumSet.noneOf(ContestStatus.class);
        if (filter != null) {
            for (String exclude : filter) {
                if (exclude != null) {
                    for (String name : exclude.split("\\W+")) {
                        ContestStatus contestStatus;
                        try {
                            contestStatus = ContestStatus.valueOf(name.trim().toUpperCase(Locale.US));
                        } catch (IllegalArgumentException ex) {
                            continue;
                        }
                        set.add(contestStatus);
                    }
                }
            }
        }
        return set;
    }

}
