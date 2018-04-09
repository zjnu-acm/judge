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
package cn.edu.zjnu.acm.judge.mapper;

import cn.edu.zjnu.acm.judge.data.form.BestSubmissionForm;
import com.google.common.collect.ImmutableSortedSet;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.StreamSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 *
 * @author zhanhb
 */
@Slf4j
public class BestSubmissionsBuilder {

    private static final Set<String> ALLOW_COLUMNS = ImmutableSortedSet.orderedBy(String.CASE_INSENSITIVE_ORDER).add("memory", "time", "code_length", "in_date", "solution_id").build();
    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.DESC, "in_date");

    public static String bestSubmissions(@Param("form") BestSubmissionForm form, @Param("pageable") Pageable pageable) {
        Long contestId = form.getContestId();
        long problemId = form.getProblemId();
        Set<String> dejaVu = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        Sort sort = Optional.ofNullable(pageable.getSort())
                .map(s -> s.and(DEFAULT_SORT)).orElse(DEFAULT_SORT);
        Sort.Order[] orders = StreamSupport.stream(sort.spliterator(), false)
                .filter(order -> ALLOW_COLUMNS.contains(order.getProperty()) && dejaVu.add(order.getProperty()))
                .toArray(Sort.Order[]::new);
        final int length = orders.length;
        log.debug("{}", Arrays.asList(orders));

        StringBuilder sb = new StringBuilder("select " + SubmissionMapper.LIST_COLUMNS + " from solution s where problem_id=").append(problemId).append(" and score=100 ");
        if (contestId != null) {
            sb.append("and contest_id=").append(contestId).append(' ');
        }
        for (int i = length - 1; i >= 0; --i) {
            sb.append("and(user_id");
            for (int j = 0; j <= i; ++j) {
                sb.append(',').append(orders[j].getProperty());
            }
            sb.append(")in(select user_id");
            for (int j = 0; j < i; ++j) {
                sb.append(',').append(orders[j].getProperty());
            }
            sb.append(',').append(orders[i].isAscending() ? "min" : "max").append("(").append(orders[i].getProperty()).append(")").append(orders[i].getProperty()).append(" from solution where problem_id=").append(problemId).append(" and score=100 ");
            if (contestId != null) {
                sb.append("and contest_id=").append(contestId).append(' ');
            }
        }
        for (int i = 0; i < length; ++i) {
            sb.append("group by user_id)");
        }
        if (length > 0) {
            sb.append(" order by ");
            for (int i = 0; i < length; ++i) {
                if (i > 0) {
                    sb.append(",");
                }
                sb.append(orders[i].getProperty());
                if (!orders[i].isAscending()) {
                    sb.append(" desc");
                }
            }
        }
        return sb.append(" limit ")
                .append(pageable.getOffset())
                .append(",")
                .append(pageable.getPageSize())
                .toString();
    }

    // should be public
    public BestSubmissionsBuilder() {
    }

}
