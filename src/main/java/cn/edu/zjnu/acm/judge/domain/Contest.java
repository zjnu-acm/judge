/*
 * Copyright 2015 ZJNU ACM.
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

import cn.edu.zjnu.acm.judge.util.SpecialCall;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
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
@Builder(builderClassName = "Builder")
@Data
@NoArgsConstructor
public class Contest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String title;
    private String description;
    private Instant startTime;
    private Instant endTime;
    private Boolean disabled;
    private Instant createdTime;
    private Instant modifiedTime;
    private List<Problem> problems;

    @JsonIgnore
    @SpecialCall("contests/problems")
    public boolean isStarted() {
        return startTime == null || startTime.isBefore(Instant.now());
    }

    @JsonIgnore
    public boolean isEnded() {
        return endTime != null && endTime.isBefore(Instant.now());
    }

    @JsonIgnore
    public boolean isError() {
        return startTime != null && endTime != null && startTime.isAfter(endTime);
    }

}
