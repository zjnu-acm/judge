/**
 * Copyright (c) 2006-2008 The Chromium Authors. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package cn.edu.zjnu.acm.judge.sandbox.win32;

import java.util.function.ToLongFunction;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@RequiredArgsConstructor
public enum TokenType implements ToLongFunction<RestrictedToken> {

    IMPERSONATION(RestrictedToken::createRestrictedTokenForImpersonation),
    PRIMARY(RestrictedToken::createRestrictedToken);

    @Delegate
    private final ToLongFunction<RestrictedToken> function;

}
