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
package cn.edu.zjnu.acm.judge.util;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public interface Utility {

    static String getRandomString(int length) {
        return getRandomString(length, ThreadLocalRandom.current());
    }

    static String getRandomString(int length, Random random) {
        char[] val = new char[length];
        for (int i = 0; i < length; ++i) {
            int x = random.nextInt(62);
            switch ((x + 16) / 26) {
                case 0:
                    val[i] = (char) (x + '0');
                    break;
                case 1:
                    val[i] = (char) (x + 'A' - 10);
                    break;
                case 2:
                    val[i] = (char) (x + 'a' - 36);
                    break;
            }
        }
        return new String(val);
    }

}
