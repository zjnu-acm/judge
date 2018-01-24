/**
 * Copyright (c) 2006-2008 The Chromium Authors. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.github.zhanhb.judge.win32;

import com.github.zhanhb.jnc.platform.win32.LUID;
import com.github.zhanhb.jnc.platform.win32.SID;
import com.github.zhanhb.jnc.platform.win32.Win32Exception;
import com.google.common.collect.ImmutableSet;
import java.util.Set;

import static com.github.zhanhb.jnc.platform.win32.WELL_KNOWN_SID_TYPE.WinAuthenticatedUserSid;
import static com.github.zhanhb.jnc.platform.win32.WELL_KNOWN_SID_TYPE.WinBuiltinUsersSid;
import static com.github.zhanhb.jnc.platform.win32.WELL_KNOWN_SID_TYPE.WinInteractiveSid;
import static com.github.zhanhb.jnc.platform.win32.WELL_KNOWN_SID_TYPE.WinNullSid;
import static com.github.zhanhb.jnc.platform.win32.WELL_KNOWN_SID_TYPE.WinRestrictedCodeSid;
import static com.github.zhanhb.jnc.platform.win32.WELL_KNOWN_SID_TYPE.WinWorldSid;
import static com.github.zhanhb.jnc.platform.win32.WinError.ERROR_BAD_ARGUMENTS;
import static com.github.zhanhb.jnc.platform.win32.WinNT.SE_CHANGE_NOTIFY_NAME;

public class Sandbox {

    private final SID SID_NULL = SID.ofWellKnown(WinNullSid);
    private final SID SID_WORLD = SID.ofWellKnown(WinWorldSid);
    private final SID SID_INTERACTIVE = SID.ofWellKnown(WinInteractiveSid);
    private final SID SID_AUTHENTICATED_USER = SID.ofWellKnown(WinAuthenticatedUserSid);
    private final SID SID_RESTRICTED_CODE = SID.ofWellKnown(WinRestrictedCodeSid);
    private final SID SID_BUILTIN_USERS = SID.ofWellKnown(WinBuiltinUsersSid);
    private final Set<LUID> CHANGE_NOTIFY = ImmutableSet.of(LUID.lookup(SE_CHANGE_NOTIFY_NAME));
    private final Set<SID> USER_NON_ADMIN_EXCEPTION = ImmutableSet.of(SID_WORLD, SID_INTERACTIVE, SID_AUTHENTICATED_USER, SID_BUILTIN_USERS);
    private final Set<SID> USER_INTERACTIVE_EXCEPTION = USER_NON_ADMIN_EXCEPTION;
    private final Set<SID> USER_LIMITED_EXCEPTION = ImmutableSet.of(SID_WORLD, SID_INTERACTIVE, SID_BUILTIN_USERS);

    public long createRestrictedToken(
            TokenLevel securityLevel,
            IntegrityLevel integrityLevel,
            TokenType tokenType,
            boolean lockdownDefaultDacl) {
        // Initialized with the current process token
        try (RestrictedToken restrictedToken = new RestrictedToken(/*nullptr*/0)) {
            if (lockdownDefaultDacl) {
                restrictedToken.setLockdownDefaultDacl();
            }

            Set<LUID> privilegeExceptions = ImmutableSet.of();
            Set<SID> sidExceptions = ImmutableSet.of();

            boolean denySids = true;
            boolean removePrivileges = true;

            switch (securityLevel) {
                case USER_UNPROTECTED:
                    denySids = false;
                    removePrivileges = false;
                    break;
                case USER_RESTRICTED_SAME_ACCESS:
                    denySids = false;
                    removePrivileges = false;

                    restrictedToken.addRestrictingSidAllSids();
                    break;
                case USER_NON_ADMIN:
                    sidExceptions = USER_NON_ADMIN_EXCEPTION;
                    privilegeExceptions = CHANGE_NOTIFY;
                    break;
                case USER_INTERACTIVE:
                    sidExceptions = USER_INTERACTIVE_EXCEPTION;
                    privilegeExceptions = CHANGE_NOTIFY;
                    restrictedToken.addRestrictingSid(SID_BUILTIN_USERS);
                    restrictedToken.addRestrictingSid(SID_WORLD);
                    restrictedToken.addRestrictingSid(SID_RESTRICTED_CODE);
                    restrictedToken.addRestrictingSidCurrentUser();
                    restrictedToken.addRestrictingSidLogonSession();
                    break;
                case USER_LIMITED:
                    sidExceptions = USER_LIMITED_EXCEPTION;
                    privilegeExceptions = CHANGE_NOTIFY;
                    restrictedToken.addRestrictingSid(SID_BUILTIN_USERS);
                    restrictedToken.addRestrictingSid(SID_WORLD);
                    restrictedToken.addRestrictingSid(SID_RESTRICTED_CODE);

                    // This token has to be able to create objects in BNO.
                    // Unfortunately, on Vista+, it needs the current logon sid
                    // in the token to achieve this. You should also set the process to be
                    // low integrity level so it can't access object created by other
                    // processes.
                    restrictedToken.addRestrictingSidLogonSession();
                    break;
                case USER_RESTRICTED:
                    privilegeExceptions = CHANGE_NOTIFY;
                    restrictedToken.addUserSidForDenyOnly();
                    restrictedToken.addRestrictingSid(SID_RESTRICTED_CODE);
                    break;
                case USER_LOCKDOWN:
                    restrictedToken.addUserSidForDenyOnly();
                    restrictedToken.addRestrictingSid(SID_NULL);
                    break;
                default:
                    throw new Win32Exception(ERROR_BAD_ARGUMENTS);
            }

            if (denySids) {
                restrictedToken.addAllSidsForDenyOnly(sidExceptions);
            }

            if (removePrivileges) {
                restrictedToken.deleteAllPrivileges(privilegeExceptions);
            }

            restrictedToken.setIntegrityLevel(integrityLevel);

            switch (tokenType) {
                case PRIMARY:
                    return restrictedToken.createRestrictedToken();
                case IMPERSONATION:
                    return restrictedToken.createRestrictedTokenForImpersonation();
                default:
                    throw new Win32Exception(ERROR_BAD_ARGUMENTS);
            }
        }
    }

}
