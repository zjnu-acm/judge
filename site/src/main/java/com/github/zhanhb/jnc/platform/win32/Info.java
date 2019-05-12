/*
 * Copyright 2018 ZJNU ACM.
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
package com.github.zhanhb.jnc.platform.win32;

import java.util.function.Function;
import java.util.function.Supplier;
import jnc.foreign.Struct;

/**
 *
 * @author zhanhb
 */
class Info {

    static <T extends Struct> Info of(
            Supplier<T> supplier,
            Function<T, ? extends Struct> anySizeArrayElement) {
        T struct = supplier.get();
        Struct apply = anySizeArrayElement.apply(struct);
        return new Info(struct.size(), apply.size());
    }

    private final int a;
    private final int e;

    Info(int a, int e) {
        this.a = a;
        this.e = e;
    }

    public int toCount(int size) {
        return Math.max((size - a + e - 1) / e, 0) + 1;
    }

}
