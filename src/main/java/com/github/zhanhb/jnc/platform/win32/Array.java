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

import java.util.function.Supplier;
import jnc.foreign.Struct;

@SuppressWarnings("FinalMethod")
public class Array<T extends Struct> extends Struct {

    private final T[] array;

    public Array(Class<T> clazz, Supplier<T> supplier, int length) {
        @SuppressWarnings("unchecked")
        T[] tmp = (T[]) java.lang.reflect.Array.newInstance(clazz, length);
        for (int i = 0; i < length; i++) {
            tmp[i] = clazz.cast(inner(supplier.get()));
        }
        array = tmp;
    }

    public final T get(int i) {
        return array[i];
    }

    public final int length() {
        return array.length;
    }

}
