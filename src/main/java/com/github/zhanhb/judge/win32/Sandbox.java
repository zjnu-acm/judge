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

import com.github.zhanhb.jnc.platform.win32.LUID;
import com.github.zhanhb.jnc.platform.win32.SID;
import com.github.zhanhb.jnc.platform.win32.Win32Exception;
import com.google.common.collect.ImmutableList;

import static com.github.zhanhb.jnc.platform.win32.WELL_KNOWN_SID_TYPE.WinAuthenticatedUserSid;
import static com.github.zhanhb.jnc.platform.win32.WELL_KNOWN_SID_TYPE.WinBuiltinUsersSid;
import static com.github.zhanhb.jnc.platform.win32.WELL_KNOWN_SID_TYPE.WinInteractiveSid;
import static com.github.zhanhb.jnc.platform.win32.WELL_KNOWN_SID_TYPE.WinNullSid;
import static com.github.zhanhb.jnc.platform.win32.WELL_KNOWN_SID_TYPE.WinRestrictedCodeSid;
import static com.github.zhanhb.jnc.platform.win32.WELL_KNOWN_SID_TYPE.WinWorldSid;
import static com.github.zhanhb.jnc.platform.win32.WinError.ERROR_BAD_ARGUMENTS;
import static com.github.zhanhb.jnc.platform.win32.WinNT.SE_CHANGE_NOTIFY_NAME;

/**
 *
 * @author zhanhb
 */
public class Sandbox {

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

            ImmutableList.Builder<LUID> privilegeExceptions = ImmutableList.builder();
            ImmutableList.Builder<SID> sidExceptions = ImmutableList.builder();

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
                    sidExceptions.add(SID.ofWellKnown(WinBuiltinUsersSid));
                    sidExceptions.add(SID.ofWellKnown(WinWorldSid));
                    sidExceptions.add(SID.ofWellKnown(WinInteractiveSid));
                    sidExceptions.add(SID.ofWellKnown(WinAuthenticatedUserSid));
                    privilegeExceptions.add(LUID.lookup(null, SE_CHANGE_NOTIFY_NAME));
                    break;
                case USER_INTERACTIVE:
                    sidExceptions.add(SID.ofWellKnown(WinBuiltinUsersSid));
                    sidExceptions.add(SID.ofWellKnown(WinWorldSid));
                    sidExceptions.add(SID.ofWellKnown(WinInteractiveSid));
                    sidExceptions.add(SID.ofWellKnown(WinAuthenticatedUserSid));
                    privilegeExceptions.add(LUID.lookup(null, SE_CHANGE_NOTIFY_NAME));
                    restrictedToken.addRestrictingSid(SID.ofWellKnown(WinBuiltinUsersSid));
                    restrictedToken.addRestrictingSid(SID.ofWellKnown(WinWorldSid));
                    restrictedToken.addRestrictingSid(SID.ofWellKnown(WinRestrictedCodeSid));
                    restrictedToken.addRestrictingSidCurrentUser();
                    restrictedToken.addRestrictingSidLogonSession();
                    break;
                case USER_LIMITED:
                    sidExceptions.add(SID.ofWellKnown(WinBuiltinUsersSid));
                    sidExceptions.add(SID.ofWellKnown(WinWorldSid));
                    sidExceptions.add(SID.ofWellKnown(WinInteractiveSid));
                    privilegeExceptions.add(LUID.lookup(null, SE_CHANGE_NOTIFY_NAME));
                    restrictedToken.addRestrictingSid(SID.ofWellKnown(WinBuiltinUsersSid));
                    restrictedToken.addRestrictingSid(SID.ofWellKnown(WinWorldSid));
                    restrictedToken.addRestrictingSid(SID.ofWellKnown(WinRestrictedCodeSid));

                    // This token has to be able to create objects in BNO.
                    // Unfortunately, on Vista+, it needs the current logon sid
                    // in the token to achieve this. You should also set the process to be
                    // low integrity level so it can't access object created by other
                    // processes.
                    restrictedToken.addRestrictingSidLogonSession();
                    break;
                case USER_RESTRICTED:
                    privilegeExceptions.add(LUID.lookup(null, SE_CHANGE_NOTIFY_NAME));
                    restrictedToken.addUserSidForDenyOnly();
                    restrictedToken.addRestrictingSid(SID.ofWellKnown(WinRestrictedCodeSid));
                    break;
                case USER_LOCKDOWN:
                    restrictedToken.addUserSidForDenyOnly();
                    restrictedToken.addRestrictingSid(SID.ofWellKnown(WinNullSid));
                    break;
                default:
                    throw new Win32Exception(ERROR_BAD_ARGUMENTS);
            }

            if (denySids) {
                restrictedToken.addAllSidsForDenyOnly(sidExceptions.build());
            }

            if (removePrivileges) {
                restrictedToken.deleteAllPrivileges(privilegeExceptions.build());
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
