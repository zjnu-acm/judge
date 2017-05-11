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

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.Instant;
import javax.xml.bind.annotation.XmlTransient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author zhanhb
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(builderClassName = "Builder")
@Data
@NoArgsConstructor
@Setter(AccessLevel.PRIVATE)
public class Contest implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    private String title;
    private String description;
    private Instant startTime;
    private Instant endTime;
    private boolean disabled;
    private Instant createdTime;
    private Instant modifiedTime;

    @JsonIgnore
    @XmlTransient
    public boolean isStarted() {
        return startTime == null || startTime.isBefore(Instant.now());
    }

    @JsonIgnore
    @XmlTransient
    public boolean isEnded() {
        return endTime != null && endTime.isBefore(Instant.now());
    }

    @JsonIgnore
    @XmlTransient
    public boolean isError() {
        return startTime != null && endTime != null && startTime.isAfter(endTime);
    }

}
