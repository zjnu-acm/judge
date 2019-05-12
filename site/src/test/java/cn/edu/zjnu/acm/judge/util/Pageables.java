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
package cn.edu.zjnu.acm.judge.util;

import java.util.ArrayList;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 *
 * @author zhanhb
 */
@UtilityClass
@SuppressWarnings("FinalClass")
public class Pageables {

    public static Pageable[] bestSubmission() {
        Sort sort = Sort.by(Sort.Direction.DESC, "time", "memory", "code_length");
        PageRequest a = PageRequest.of(5, 20, sort);
        sort = Sort.by(Sort.Direction.DESC, "solution_id");
        PageRequest b = PageRequest.of(5, 20, sort);
        PageRequest c = PageRequest.of(9, 1);
        Pageable d = PageRequest.of(6, 21);
        return new Pageable[]{a, b, c, d, buggy()};
    }

    public static Pageable[] users() {
        Pageable a = PageRequest.of(0, 50);

        Pageable b = PageRequest.of(0, 50, Sort.by(Sort.Order.desc("solved"), Sort.Order.asc("submit")));
        return new Pageable[]{a, b, buggy()};
    }

    private Pageable buggy() {
        Sort.Order order = Sort.Order.desc("dummy");
        ArrayList<Sort.Order> list = new ArrayList<>(1);
        list.add(order);
        Pageable pageable = PageRequest.of(0, 50, Sort.by(list));
        list.clear();
        return pageable;
    }

}
