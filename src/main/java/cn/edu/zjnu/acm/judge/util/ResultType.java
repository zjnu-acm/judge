/*
 * Copyright 2014 zhanhb.
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
package cn.edu.zjnu.acm.judge.util;

/**
 *
 * @author zhanhb
 */
@Deprecated
public interface ResultType {

    int ACCEPTED = 0;
    int PRESENTATION_ERROR = 1;
    int TIME_LIMIT_EXCEED = 2;
    int MEMORY_LIMIT_EXCEED = 3;
    int WRONG_ANSWER = 4;
    int RUNTIME_ERROR = 5;
    int OUTPUT_LIMIT_EXCEED = 6;
    int COMPILE_ERROR = -7;
    int SYSTEM_ERROR = -98;
    int VALIDATE_ERROR = -99;
    int QUEUING = -10000;
    int SCORE_ACCEPT = 100;

    static String getCaseScoreDescription(int score) {
        switch (score) {
            case ACCEPTED:
            case SCORE_ACCEPT:
                return "Accepted";
            case PRESENTATION_ERROR:
                return "Presentation Error";
            case TIME_LIMIT_EXCEED:
                return "Time Limit Exceed";
            case MEMORY_LIMIT_EXCEED:
                return "Memory Limit Exceed";
            case WRONG_ANSWER:
                return "Wrong Answer";
            case RUNTIME_ERROR:
                return "Runtime Error";
            case OUTPUT_LIMIT_EXCEED:
                return "Output Limit Exceed";
            case COMPILE_ERROR:
                return "Compile Error";
            case QUEUING:
                return "<font color=green>Waiting</font>";
            case SYSTEM_ERROR:
                return "System Error";
            case VALIDATE_ERROR:
                return "Validate Error";
        }
        return "Other";
    }

    static String getShowsourceString(int pampd) {
        switch (pampd) {
            case QUEUING:
            case COMPILE_ERROR:
                return getCaseScoreDescription(pampd);
            case SCORE_ACCEPT:
                return getCaseScoreDescription(0);
            default:
                return Integer.toString(pampd);
        }
    }

    static String getResultDescription(int i) {
        if (0 <= i && i < 100) {
            return "Unaccepted";
        }
        return getCaseScoreDescription(i);
    }

}
