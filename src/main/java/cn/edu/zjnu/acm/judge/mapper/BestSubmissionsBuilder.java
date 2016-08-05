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

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.StreamSupport;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 *
 * @author zhanhb
 */
public class BestSubmissionsBuilder {

    private final Set<String> allowColumns;

    public BestSubmissionsBuilder() {
        this.allowColumns = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        this.allowColumns.addAll(Arrays.asList("memory", "time", "code_length", "in_date", "solution_id"));
    }

    public String bestSubmissions(@Param("problemId") long problemId, @Param("pageable") Pageable pageable) {
        Sort sort = pageable.getSort();

        Set<String> dejaVu = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        Sort.Order[] orders = sort != null ? StreamSupport.stream(sort.spliterator(), false)
                .filter(order -> allowColumns.contains(order.getProperty()))
                .filter(order -> dejaVu.add(order.getProperty()))
                .toArray(Sort.Order[]::new) : new Sort.Order[0];

        StringBuilder sb = new StringBuilder("select " + SubmissionMapper.LIST_COLUMNS + " from solution s where solution_id in(select solution_id from");
        for (int i = orders.length - 1; i >= 0; --i) {
            sb.append("(select s.user_id");
            for (int j = 0; j < i; ++j) {
                sb.append(",s.").append(orders[j].getProperty());
            }
            sb.append(',').append(orders[i].isAscending() ? "min" : "max").append("(s.").append(orders[i].getProperty()).append(")").append(orders[i].getProperty());
            sb.append(" from");
        }
        sb.append(" solution s where s.problem_id=").append(problemId).append(" and score=100 group by user_id ");
        for (int i = 0, length = orders.length; i < length; ++i) {
            sb.append(")t join solution s on s.problem_id=").append(problemId).append(" and score=100 and s.user_id=t.user_id");
            for (int j = 0; j <= i; ++j) {
                String property = orders[i].getProperty();
                sb.append(" and s.").append(property).append("=t.").append(property);
            }
            sb.append(" group by t.user_id");
            for (int j = 0; j <= i; ++j) {
                String property = orders[i].getProperty();
                sb.append(",t.").append(property);
            }
        }
        sb.append(')');
        if (orders.length > 0) {
            sb.append(" order by ");
            for (int i = 0, len = orders.length; i < len; ++i) {
                if (i > 0) {
                    sb.append(",");
                }
                sb.append("s.").append(orders[i].getProperty());
                if (!orders[i].isAscending()) {
                    sb.append(" desc");
                }
            }
        }
        sb.append(" limit ").append(pageable.getOffset()).append(",").append(pageable.getPageSize());
        return sb.toString();
    }

}
