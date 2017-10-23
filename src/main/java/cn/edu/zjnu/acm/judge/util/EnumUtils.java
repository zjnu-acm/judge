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

import java.util.Collection;
import javax.annotation.Nonnull;

/**
 *
 * @author zhanhb
 */
public interface EnumUtils {

    static <E extends Enum<E>> int toMask(@Nonnull Collection<E> list) {
        int mask = 0;
        for (E e : list) {
            mask |= 1 << e.ordinal();
        }
        return mask;
    }

}
