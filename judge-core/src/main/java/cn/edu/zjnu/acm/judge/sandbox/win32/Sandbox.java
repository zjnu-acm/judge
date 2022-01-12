/**
 * Copyright (c) 2006-2008 The Chromium Authors. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package cn.edu.zjnu.acm.judge.sandbox.win32;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import javax.annotation.Nullable;
import jnc.platform.win32.ACCESS_MODE;
import jnc.platform.win32.SID;
import jnc.platform.win32.Win32Exception;

import static jnc.platform.win32.WELL_KNOWN_SID_TYPE.WinAuthenticatedUserSid;
import static jnc.platform.win32.WELL_KNOWN_SID_TYPE.WinBuiltinUsersSid;
import static jnc.platform.win32.WELL_KNOWN_SID_TYPE.WinCreatorOwnerRightsSid;
import static jnc.platform.win32.WELL_KNOWN_SID_TYPE.WinInteractiveSid;
import static jnc.platform.win32.WELL_KNOWN_SID_TYPE.WinNullSid;
import static jnc.platform.win32.WELL_KNOWN_SID_TYPE.WinRestrictedCodeSid;
import static jnc.platform.win32.WELL_KNOWN_SID_TYPE.WinWorldSid;
import static jnc.platform.win32.WinError.ERROR_BAD_ARGUMENTS;
import static jnc.platform.win32.WinNT.GENERIC_ALL;
import static jnc.platform.win32.WinNT.READ_CONTROL;

public enum Sandbox {

    INSTANCE;

    private final SID SID_NULL = SID.ofWellKnown(WinNullSid);
    private final SID SID_WORLD = SID.ofWellKnown(WinWorldSid);
    private final SID SID_INTERACTIVE = SID.ofWellKnown(WinInteractiveSid);
    private final SID SID_AUTHENTICATED_USER = SID.ofWellKnown(WinAuthenticatedUserSid);
    private final SID SID_RESTRICTED_CODE = SID.ofWellKnown(WinRestrictedCodeSid);
    private final SID SID_BUILTIN_USERS = SID.ofWellKnown(WinBuiltinUsersSid);
    private final SID SID_CREATOR_OWNER_RIGHTS = SID.ofWellKnown(WinCreatorOwnerRightsSid);
    private final Set<SID> USER_INTERACTIVE_EXCEPTION = ImmutableSet.of(SID_BUILTIN_USERS, SID_WORLD, SID_INTERACTIVE, SID_AUTHENTICATED_USER);
    private final Set<SID> USER_RESTRICTED_NON_ADMIN_EXCEPTION = USER_INTERACTIVE_EXCEPTION;
    private final Set<SID> USER_LIMITED_EXCEPTION = ImmutableSet.of(SID_BUILTIN_USERS, SID_WORLD, SID_INTERACTIVE);

    public long createRestrictedToken(
            TokenLevel securityLevel,
            IntegrityLevel integrityLevel,
            TokenType tokenType,
            boolean lockdownDefaultDacl,
            @Nullable SID uniqueRestrictedSid) {
        // Initialized with the current process token
        try (RestrictedToken restrictedToken = new RestrictedToken(0 /*nullptr*/)) {
            if (lockdownDefaultDacl) {
                restrictedToken.setLockdownDefaultDacl();
            }
            if (uniqueRestrictedSid != null) {
                restrictedToken.addDefaultDaclSid(uniqueRestrictedSid,
                        ACCESS_MODE.GRANT_ACCESS, GENERIC_ALL);
                restrictedToken.addDefaultDaclSid(
                        SID_CREATOR_OWNER_RIGHTS,
                        ACCESS_MODE.GRANT_ACCESS, READ_CONTROL);
            }

            Set<SID> sidExceptions = ImmutableSet.of();

            boolean denySids = true;
            boolean removePrivileges = true;
            boolean removeTraversePrivilege = false;

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
                case USER_RESTRICTED_NON_ADMIN:
                    sidExceptions = USER_RESTRICTED_NON_ADMIN_EXCEPTION;
                    restrictedToken.addRestrictingSid(SID_BUILTIN_USERS);
                    restrictedToken.addRestrictingSid(SID_WORLD);
                    restrictedToken.addRestrictingSid(SID_INTERACTIVE);
                    restrictedToken.addRestrictingSid(SID_AUTHENTICATED_USER);
                    restrictedToken.addRestrictingSid(SID_RESTRICTED_CODE);
                    if (uniqueRestrictedSid != null) {
                        restrictedToken.addRestrictingSid(uniqueRestrictedSid);
                    }
                    break;
                case USER_INTERACTIVE:
                    sidExceptions = USER_INTERACTIVE_EXCEPTION;
                    restrictedToken.addRestrictingSid(SID_BUILTIN_USERS);
                    restrictedToken.addRestrictingSid(SID_WORLD);
                    restrictedToken.addRestrictingSid(SID_RESTRICTED_CODE);
                    restrictedToken.addRestrictingSidCurrentUser();
                    restrictedToken.addRestrictingSidLogonSession();
                    if (uniqueRestrictedSid != null) {
                        restrictedToken.addRestrictingSid(uniqueRestrictedSid);
                    }
                    break;
                case USER_LIMITED:
                    sidExceptions = USER_LIMITED_EXCEPTION;
                    restrictedToken.addRestrictingSid(SID_BUILTIN_USERS);
                    restrictedToken.addRestrictingSid(SID_WORLD);
                    restrictedToken.addRestrictingSid(SID_RESTRICTED_CODE);
                    if (uniqueRestrictedSid != null) {
                        restrictedToken.addRestrictingSid(uniqueRestrictedSid);
                    }

                    // This token has to be able to create objects in BNO.
                    // Unfortunately, on Vista+, it needs the current logon sid
                    // in the token to achieve this. You should also set the process to be
                    // low integrity level so it can't access object created by other
                    // processes.
                    restrictedToken.addRestrictingSidLogonSession();
                    break;
                case USER_RESTRICTED:
                    restrictedToken.addUserSidForDenyOnly();
                    restrictedToken.addRestrictingSid(SID_RESTRICTED_CODE);
                    if (uniqueRestrictedSid != null) {
                        restrictedToken.addRestrictingSid(uniqueRestrictedSid);
                    }
                    break;
                case USER_LOCKDOWN:
                    removeTraversePrivilege = true;
                    restrictedToken.addUserSidForDenyOnly();
                    restrictedToken.addRestrictingSid(SID_NULL);
                    if (uniqueRestrictedSid != null) {
                        restrictedToken.addRestrictingSid(uniqueRestrictedSid);
                    }
                    break;
                default:
                    throw new Win32Exception(ERROR_BAD_ARGUMENTS);
            }

            if (denySids) {
                restrictedToken.addAllSidsForDenyOnly(sidExceptions);
            }

            if (removePrivileges) {
                restrictedToken.deleteAllPrivileges(removeTraversePrivilege);
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
