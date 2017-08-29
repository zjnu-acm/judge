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
package cn.edu.zjnu.acm.judge.data.dto;

import cn.edu.zjnu.acm.judge.domain.Contest;
import cn.edu.zjnu.acm.judge.domain.Problem;
import java.io.Serializable;
import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

/**
 *
 * @author zhanhb
 */
@Builder(builderClassName = "Builder")
@EqualsAndHashCode
@Getter
@SuppressWarnings("FinalClass")
@Value
public class ContestDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private Contest contest;
    private List<Problem> problems;

}
