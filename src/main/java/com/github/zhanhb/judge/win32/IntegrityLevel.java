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
package com.github.zhanhb.judge.win32;

/**
 *
 * @author zhanhb
 */
public enum IntegrityLevel {

    INTEGRITY_LEVEL_SYSTEM("S-1-16-16384"),
    INTEGRITY_LEVEL_HIGH("S-1-16-12288"),
    INTEGRITY_LEVEL_MEDIUM("S-1-16-8192"),
    INTEGRITY_LEVEL_MEDIUM_LOW("S-1-16-6144"),
    INTEGRITY_LEVEL_LOW("S-1-16-4096"),
    INTEGRITY_LEVEL_BELOW_LOW("S-1-16-2048"),
    INTEGRITY_LEVEL_UNTRUSTED("S-1-16-0"),
    INTEGRITY_LEVEL_LAST(null);

    private final String string;

    IntegrityLevel(String string) {
        this.string = string;
    }

    String getString() {
        return string;
    }

}
