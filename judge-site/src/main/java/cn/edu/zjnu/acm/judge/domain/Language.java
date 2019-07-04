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
package cn.edu.zjnu.acm.judge.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.annotation.Nullable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 *
 * @author zhanhb
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(builderClassName = "Builder")
@Data
@NoArgsConstructor
public class Language implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    @NonNull
    private String name;
    @NonNull
    private String sourceExtension;
    @Nullable
    private String compileCommand;
    @Nullable
    private String executeCommand;
    @NonNull
    private String executableExtension;
    private long timeFactor;
    private long extTime;
    private long extMemory;
    private String description;
    private Instant createdTime;
    private Instant modifiedTime;
    private Boolean disabled;

}
