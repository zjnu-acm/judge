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

import java.io.Serializable;
import java.time.Instant;
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
public class Submission implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    private long problem;
    private String user;
    private Long contest;
    private long time;
    private long memory;
    private int score;
    private int language;
    private String ip;
    private int sourceLength;
    private Instant inDate;
    private Integer on;

}
