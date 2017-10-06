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
package cn.edu.zjnu.acm.judge.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author zhanhb
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(builderClassName = "Builder", toBuilder = true)
@Data
@NoArgsConstructor
public class Problem implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String title;
    private String description;
    private String input;
    private String output;
    private String sampleInput;
    private String sampleOutput;
    private String hint;
    private String source;
    private Long timeLimit;
    private Long memoryLimit;

    private Long accepted;
    private Long submit;
    private Long submitUser;
    private Long solved;
    private Instant inDate;
    private Boolean disabled;
    private Instant createdTime;
    private Instant modifiedTime;

    private Long origin;
    private long[] contests;

    // 0 not submitted, 1 accepted, 2 wrong answer
    private Integer status;

    @JsonIgnore
    public int getRatio() {
        return submit == null || accepted == null || submit == 0 ? 0 : (int) Math.round(accepted * 100.0 / submit);
    }

    @JsonIgnore
    public int getDifficulty() {
        return submit == null || accepted == null || submit == 0 ? 0 : (int) Math.round((submit - accepted) * 100.0 / submit);
    }

}
