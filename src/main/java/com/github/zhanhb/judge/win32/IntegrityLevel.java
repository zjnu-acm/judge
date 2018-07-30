/**
 * Copyright (c) 2006-2008 The Chromium Authors. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.github.zhanhb.judge.win32;

public enum IntegrityLevel {

    INTEGRITY_LEVEL_SECURE("S-1-16-28672"),
    INTEGRITY_LEVEL_PROTECTED("S-1-16-20480"),
    INTEGRITY_LEVEL_SYSTEM("S-1-16-16384"),
    INTEGRITY_LEVEL_HIGH("S-1-16-12288"),
    INTEGRITY_LEVEL_MEDIUM_HIGH("S-1-16-8448"),
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
