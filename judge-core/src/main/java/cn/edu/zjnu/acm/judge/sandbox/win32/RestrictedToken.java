/*
 * Copyright (c) 2006-2008 The Chromium Authors. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package cn.edu.zjnu.acm.judge.sandbox.win32;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.IntFunction;
import jnc.foreign.Pointer;
import jnc.foreign.StructArray;
import jnc.foreign.byref.AddressByReference;
import jnc.foreign.byref.IntByReference;
import jnc.foreign.byref.PointerByReference;
import jnc.platform.win32.ACCESS_MODE;
import jnc.platform.win32.Advapi32;
import jnc.platform.win32.EXPLICIT_ACCESS;
import jnc.platform.win32.Handle;
import jnc.platform.win32.Kernel32;
import jnc.platform.win32.Kernel32Util;
import jnc.platform.win32.LUID;
import jnc.platform.win32.LUID_AND_ATTRIBUTES;
import jnc.platform.win32.SID;
import jnc.platform.win32.SID_AND_ATTRIBUTES;
import jnc.platform.win32.TOKEN_DEFAULT_DACL;
import jnc.platform.win32.TOKEN_GROUPS;
import jnc.platform.win32.TOKEN_INFORMATION_CLASS;
import jnc.platform.win32.TOKEN_MANDATORY_LABEL;
import jnc.platform.win32.TOKEN_PRIVILEGES;
import jnc.platform.win32.TOKEN_USER;
import jnc.platform.win32.TRUSTEE;
import jnc.platform.win32.TokenInformation;
import jnc.platform.win32.WString;
import jnc.platform.win32.Win32Exception;
import lombok.extern.slf4j.Slf4j;

import static cn.edu.zjnu.acm.judge.sandbox.win32.IntegrityLevel.INTEGRITY_LEVEL_LAST;
import static jnc.platform.win32.ACCESS_MODE.GRANT_ACCESS;
import static jnc.platform.win32.ACCESS_MODE.REVOKE_ACCESS;
import static jnc.platform.win32.AccCtrl.NO_INHERITANCE;
import static jnc.platform.win32.MULTIPLE_TRUSTEE_OPERATION.NO_MULTIPLE_TRUSTEE;
import static jnc.platform.win32.SECURITY_IMPERSONATION_LEVEL.SecurityIdentification;
import static jnc.platform.win32.SECURITY_IMPERSONATION_LEVEL.SecurityImpersonation;
import static jnc.platform.win32.TOKEN_INFORMATION_CLASS.TokenDefaultDacl;
import static jnc.platform.win32.TOKEN_INFORMATION_CLASS.TokenGroups;
import static jnc.platform.win32.TOKEN_INFORMATION_CLASS.TokenIntegrityLevel;
import static jnc.platform.win32.TOKEN_INFORMATION_CLASS.TokenPrivileges;
import static jnc.platform.win32.TOKEN_INFORMATION_CLASS.TokenUser;
import static jnc.platform.win32.TOKEN_TYPE.TokenPrimary;
import static jnc.platform.win32.TRUSTEE_FORM.TRUSTEE_IS_SID;
import static jnc.platform.win32.TRUSTEE_TYPE.TRUSTEE_IS_UNKNOWN;
import static jnc.platform.win32.WELL_KNOWN_SID_TYPE.WinRestrictedCodeSid;
import static jnc.platform.win32.WinError.ERROR_SUCCESS;
import static jnc.platform.win32.WinNT.DUPLICATE_SAME_ACCESS;
import static jnc.platform.win32.WinNT.GENERIC_ALL;
import static jnc.platform.win32.WinNT.SECURITY_MAX_SID_SIZE;
import static jnc.platform.win32.WinNT.SE_GROUP_INTEGRITY;
import static jnc.platform.win32.WinNT.SE_GROUP_LOGON_ID;
import static jnc.platform.win32.WinNT.SE_GROUP_USE_FOR_DENY_ONLY;
import static jnc.platform.win32.WinNT.TOKEN_ALL_ACCESS;

/**
 * @author zhanhb
 */
@Slf4j
public class RestrictedToken implements Closeable {

    private static void addSidToDacl(Pointer pSid,
            Pointer /*PACL*/ oldDacl,
            ACCESS_MODE accessMode,
            int access,
            PointerByReference newDacl) {
        EXPLICIT_ACCESS newAccess = new EXPLICIT_ACCESS();
        newAccess.setAccessMode(accessMode);
        newAccess.setAccessPermissions(access);
        newAccess.setInheritance(NO_INHERITANCE);

        TRUSTEE trustee = newAccess.getTrustee();
        trustee.setMultipleTrustee(0/*nullptr*/);
        trustee.setMultipleTrusteeOperation(NO_MULTIPLE_TRUSTEE);
        trustee.setTrusteeForm(TRUSTEE_IS_SID);
        trustee.setTrusteeType(TRUSTEE_IS_UNKNOWN);
        trustee.setName(pSid);

        if (log.isDebugEnabled()) {
            log.debug("addSidToDacl: {} {}", accessMode, SID.toString(pSid));
        }

        int error = Advapi32.INSTANCE.SetEntriesInAclW(1, newAccess, oldDacl,
                newDacl);
        if (error != ERROR_SUCCESS) {
            throw new Win32Exception(error);
        }
    }

    private static void addSidToDefaultDacl(long token,
            Pointer pSid,
            ACCESS_MODE accessMode,
            int /*ACCESS_MASK*/ access) {
        if (token == 0) {
            throw new NullPointerException();
        }
        TOKEN_DEFAULT_DACL defaultDacl = getTokenDefaultDacl(token);

        PointerByReference newDacl = new PointerByReference();
        addSidToDacl(pSid, defaultDacl.getDefaultDacl(), accessMode, access, newDacl);
        Pointer dacl = newDacl.getValue();
        try {
            TOKEN_DEFAULT_DACL newTokenDacl = new TOKEN_DEFAULT_DACL();
            newTokenDacl.setDefaultDacl(dacl);
            Kernel32Util.assertTrue(Advapi32.INSTANCE.SetTokenInformation(token,
                    TokenDefaultDacl, newTokenDacl, newTokenDacl.size()));
        } finally {
            Kernel32Util.freeLocalMemory(dacl);
        }
    }

    private static void revokeLogonSidFromDefaultDacl(long token) {
        TOKEN_GROUPS tokenGroups = getTokenGroups(token);
        for (int i = 0, n = tokenGroups.getGroupCount(); i < n; ++i) {
            SID_AND_ATTRIBUTES group = tokenGroups.get(i);
            if ((group.getAttributes() & SE_GROUP_LOGON_ID) != 0) {
                addSidToDefaultDacl(token, group.getSid(), REVOKE_ACCESS, 0);
                break;
            }
        }
    }

    private static void addUserSidToDefaultDacl(long /*HANDLE*/ token, int /*ACCESS_MASK*/ access) {
        TOKEN_USER tokenUser = getTokenUser(token);
        addSidToDefaultDacl(token, tokenUser.getUser().getSid(), GRANT_ACCESS, access);
    }

    private static void setTokenIntegrityLevel(long token, IntegrityLevel integrityLevel) {
        String integrityLevelStr = integrityLevel.getString();
        if (integrityLevelStr == null) {
            // No mandatory level specified, we don't change it.
            return;
        }

        PointerByReference integritySid = new PointerByReference();
        Kernel32Util.assertTrue(Advapi32.INSTANCE.ConvertStringSidToSidW(
                WString.toNative(integrityLevelStr), integritySid));
        try {
            TOKEN_MANDATORY_LABEL label = new TOKEN_MANDATORY_LABEL();
            SID_AND_ATTRIBUTES sidAndAttributes = label.getLabel();
            sidAndAttributes.setAttributes(SE_GROUP_INTEGRITY);
            sidAndAttributes.setSid(integritySid.getValue());

            int size = label.size() + Advapi32.INSTANCE.GetLengthSid(integritySid.getValue());
            Kernel32Util.assertTrue(Advapi32.INSTANCE.SetTokenInformation(token,
                    TokenIntegrityLevel, label, size));
        } finally {
            Kernel32Util.freeLocalMemory(integritySid.getValue());
        }
    }

    private static <T extends TokenInformation> T getTokenInfo(
            long token, TOKEN_INFORMATION_CLASS infoClass,
            IntFunction<T> bySize) {
        // get the required buffer size.
        IntByReference size = new IntByReference();
        // always false, no sufficient space to put the TokenInformation
        Advapi32.INSTANCE.GetTokenInformation(token, infoClass,
                null, 0, size);
        int value = size.getValue();
        Kernel32Util.assertTrue(value != 0);
        T buffer = bySize.apply(value);
        Kernel32Util.assertTrue(Advapi32.INSTANCE.GetTokenInformation(token,
                infoClass, buffer, buffer.size(), size));
        return buffer;
    }

    private static TOKEN_GROUPS getTokenGroups(long token) {
        return getTokenInfo(token, TokenGroups, TOKEN_GROUPS::ofSize);
    }

    private static TOKEN_PRIVILEGES getTokenPrivileges(long token) {
        return getTokenInfo(token, TokenPrivileges, TOKEN_PRIVILEGES::ofSize);
    }

    private static TOKEN_DEFAULT_DACL getTokenDefaultDacl(long token) {
        if (token == 0) {
            throw new NullPointerException();
        }

        return getTokenInfo(token, TokenDefaultDacl, TOKEN_DEFAULT_DACL::ofSize);
    }

    private static TOKEN_USER getTokenUser(long token) {
        TOKEN_USER tokenUser = TOKEN_USER.withPadding(SECURITY_MAX_SID_SIZE);

        IntByReference size = new IntByReference();
        Kernel32Util.assertTrue(Advapi32.INSTANCE.GetTokenInformation(
                token, TokenUser, tokenUser,
                tokenUser.size(), size));
        return tokenUser;
    }

    // The list of restricting sids in the restricted token.
    private final List<SID> sidsToRestrict = new ArrayList<>(10); // PSID
    // The list of privileges to remove in the restricted token.
    private final List<LUID> privilegesToDisable = new ArrayList<>(16);
    // The list of sids to mark as Deny Only in the restricted token.
    private final List<SID> sidsForDenyOnly = new ArrayList<>(8); // PSID
    // The token to restrict. Can only be set in a constructor.
    private final long effectiveToken;
    // The token integrity level. Only valid on Vista.
    private IntegrityLevel integrityLevel;
    // Lockdown the default DACL when creating new tokens.
    private boolean lockdownDefaultDacl;

    // Initializes the RestrictedToken object with effectiveToken.
    // If effectiveToken is nullptr, it initializes the RestrictedToken object
    // with the effective token of the current process.
    public RestrictedToken(long /*HANDLE*/ effectiveToken) {
        integrityLevel = INTEGRITY_LEVEL_LAST;
        lockdownDefaultDacl = false;

        AddressByReference tempToken = new AddressByReference();
        long hProcess = Kernel32.INSTANCE.GetCurrentProcess();
        boolean result;
        if (effectiveToken != 0) {
            // We duplicate the handle to be able to use it even if the original handle
            // is closed.
            result = Kernel32.INSTANCE.DuplicateHandle(hProcess,
                    effectiveToken, hProcess, tempToken, 0, false,
                    DUPLICATE_SAME_ACCESS);
        } else {
            result = Advapi32.INSTANCE.OpenProcessToken(hProcess,
                    TOKEN_ALL_ACCESS, tempToken);
        }
        Kernel32Util.assertTrue(result);
        this.effectiveToken = tempToken.getValue();
    }

    // Creates a restricted token.
    public long createRestrictedToken() /*const*/ {
        int denySize = sidsForDenyOnly.size();
        int restrictSize = sidsToRestrict.size();
        int privilegesSize = privilegesToDisable.size();

        log.debug("createRestrictedToken: {} {} {}", sidsForDenyOnly, privilegesToDisable, sidsToRestrict);

        StructArray<SID_AND_ATTRIBUTES> denyOnlyArray = null;
        if (denySize != 0) {
            denyOnlyArray = new StructArray<>(SID_AND_ATTRIBUTES::new, denySize);

            for (int i = 0; i < denySize; ++i) {
                SID_AND_ATTRIBUTES sidAndAttributes = denyOnlyArray.get(i);
                sidAndAttributes.setAttributes(SE_GROUP_USE_FOR_DENY_ONLY);
                sidAndAttributes.setSid(sidsForDenyOnly.get(i).asPSID());
            }
        }

        StructArray<SID_AND_ATTRIBUTES> sidsToRestrictArray = null;
        if (restrictSize != 0) {
            sidsToRestrictArray = new StructArray<>(SID_AND_ATTRIBUTES::new, restrictSize);

            for (int i = 0; i < restrictSize; ++i) {
                SID_AND_ATTRIBUTES sidAndAttributes = sidsToRestrictArray.get(i);
                sidAndAttributes.setAttributes(0);
                sidAndAttributes.setSid(sidsToRestrict.get(i).asPSID());
            }
        }

        StructArray<LUID_AND_ATTRIBUTES> privilegesToDisableArray = null;
        if (privilegesSize != 0) {
            privilegesToDisableArray = new StructArray<>(LUID_AND_ATTRIBUTES::new, privilegesSize);

            for (int i = 0; i < privilegesSize; ++i) {
                LUID_AND_ATTRIBUTES luidAndAttributes = privilegesToDisableArray.get(i);
                luidAndAttributes.setAttributes(0);
                luidAndAttributes.getLuid().copyFrom(privilegesToDisable.get(i));
            }
        }

        boolean result;
        AddressByReference newTokenHandle = new AddressByReference();
        if (denySize != 0 || restrictSize != 0 || privilegesSize != 0) {
            result = Advapi32.INSTANCE.CreateRestrictedToken(
                    effectiveToken, 0,
                    denySize, denyOnlyArray,
                    privilegesSize, privilegesToDisableArray,
                    restrictSize, sidsToRestrictArray,
                    newTokenHandle);
        } else {
            // Duplicate the token even if it's not modified at this point
            // because any subsequent changes to this token would also affect the
            // current process.
            result = Advapi32.INSTANCE.DuplicateTokenEx(effectiveToken,
                    TOKEN_ALL_ACCESS, null, SecurityIdentification,
                    TokenPrimary, newTokenHandle);
        }
        Kernel32Util.assertTrue(result);

        long newToken = newTokenHandle.getValue();
        try {
            if (lockdownDefaultDacl) {
                // Don't add Restricted sid and also remove logon sid access.
                revokeLogonSidFromDefaultDacl(newToken);
            } else {
                SID restrictedCodeSid = SID.ofWellKnown(WinRestrictedCodeSid);
                // Modify the default dacl on the token to contain Restricted.
                addSidToDefaultDacl(newToken, restrictedCodeSid.asPSID(),
                        GRANT_ACCESS, GENERIC_ALL);
            }

            // Add user to default dacl.
            addUserSidToDefaultDacl(newToken, GENERIC_ALL);

            setTokenIntegrityLevel(newToken, integrityLevel);

            AddressByReference tokenHandle = new AddressByReference();
            long hProcess = Kernel32.INSTANCE.GetCurrentProcess();
            Kernel32Util.assertTrue(Kernel32.INSTANCE.DuplicateHandle(hProcess,
                    newToken, hProcess, tokenHandle, TOKEN_ALL_ACCESS,
                    false, // Don't inherit.
                    0));
            return tokenHandle.getValue();
        } finally {
            Handle.close(newToken);
        }
    }

    // Creates a restricted token and uses this new token to create a new token
    // for impersonation. Returns this impersonation token.
    //
    // The sample usage is the same as the createRestrictedToken function.
    public long createRestrictedTokenForImpersonation() /*const*/ {
        long restrictedToken = createRestrictedToken();
        try {
            AddressByReference impersonationTokenHandle = new AddressByReference();
            Kernel32Util.assertTrue(Advapi32.INSTANCE.DuplicateToken(
                    restrictedToken, SecurityImpersonation,
                    impersonationTokenHandle));
            long impersonationToken = impersonationTokenHandle.getValue();
            try {
                AddressByReference tokenHandle = new AddressByReference();
                long hProcess = Kernel32.INSTANCE.GetCurrentProcess();
                Kernel32Util.assertTrue(Kernel32.INSTANCE.DuplicateHandle(
                        hProcess, impersonationToken, hProcess,
                        tokenHandle, TOKEN_ALL_ACCESS, false, // Don't inherit.
                        0));
                return tokenHandle.getValue();
            } finally {
                Handle.close(impersonationToken);
            }
        } finally {
            Handle.close(restrictedToken);
        }
    }

    // Lists all sids in the token and mark them as Deny Only except for those
    // present in the exceptions parameter. If there is no exception needed,
    // the caller can pass an empty list or nullptr for the exceptions
    // parameter.
    //
    // Sample usage:
    //    List<SID> sidExceptions = new ArrayList<>();
    //    sidExceptions.add(SID.ofWellKnown(WinBuiltinUsersSid));
    //    sidExceptions.add(SID.ofWellKnown(WinWorldSid));
    //    restrictedToken.addAllSidsForDenyOnly(sidExceptions);
    // Note: A SID marked for Deny Only in a token cannot be used to grant
    // access to any resource. It can only be used to deny access.
    public void addAllSidsForDenyOnly(Collection<SID> exceptions) {
        Objects.requireNonNull(exceptions);
        TOKEN_GROUPS tokenGroups = getTokenGroups(effectiveToken);

        // Build the list of the deny only group SIDs
        for (int i = 0, n = tokenGroups.getGroupCount(); i < n; ++i) {
            SID_AND_ATTRIBUTES group = tokenGroups.get(i);
            if ((group.getAttributes() & (SE_GROUP_INTEGRITY | SE_GROUP_LOGON_ID)) == 0) {
                SID sid = SID.copyOf(group.getSid());
                boolean shouldIgnore = exceptions.contains(sid);
                if (!shouldIgnore) {
                    sidsForDenyOnly.add(sid);
                }
            }
        }
    }

    // Adds a user or group SID for Deny Only in the restricted token.
    // Parameter: sid is the SID to add in the Deny Only list.
    // The return getPSID is always ERROR_SUCCESS.
    //
    // Sample Usage:
    //    restrictedToken.addSidForDenyOnly(SID.ofWellKnown(WinBuiltinAdministratorsSid));
    public void addSidForDenyOnly(SID sid) {
        sidsForDenyOnly.add(Objects.requireNonNull(sid));
    }

    // Adds the user sid of the token for Deny Only in the restricted token.
    public void addUserSidForDenyOnly() {
        TOKEN_USER tokenUser = getTokenUser(effectiveToken);
        sidsForDenyOnly.add(SID.copyOf(tokenUser.getUser().getSid()));
    }

    // Lists all privileges in the token and add them to the list of privileges
    // to remove except for those present in the exceptions parameter. If
    // there is no exception needed, the caller can pass an empty list or nullptr
    // for the exceptions parameter.
    //
    // Sample usage:
    //    List<LUID> privilegeExceptions = new ArrayList<>();
    //    privilegeExceptions.add(LUID.lookup(SE_CHANGE_NOTIFY_NAME));
    //    privilegeExceptions.deleteAllPrivileges(privilegeExceptions);
    public void deleteAllPrivileges(Collection<LUID> exceptions) {
        Objects.requireNonNull(exceptions);
        TOKEN_PRIVILEGES tokenPrivileges = getTokenPrivileges(effectiveToken);
        log.debug("deleteAllPrivileges: delete all except '{}' from {}", exceptions, tokenPrivileges);
        // Build the list of privileges to disable
        for (int i = 0, n = tokenPrivileges.getPrivilegeCount(); i < n; ++i) {
            LUID luid = tokenPrivileges.get(i).getLuid();
            boolean shouldIgnore = exceptions.contains(luid);
            if (!shouldIgnore) {
                privilegesToDisable.add(luid);
            }
        }
    }

    // Adds a privilege to the list of privileges to remove in the restricted
    // token.
    // Parameter: privilege is the privilege name to remove. This is the string
    // representing the privilege. (e.g. "SeChangeNotifyPrivilege").
    //
    // Sample usage:
    //    restrictedToken.deletePrivilege(LUID.lookup(SE_LOAD_DRIVER_NAME));
    public void deletePrivilege(LUID privilege) {
        privilegesToDisable.add(Objects.requireNonNull(privilege));
    }

    // Adds a SID to the list of restricting sids in the restricted token.
    // Parameter: sid is the sid to add to the list restricting sids.
    // The return getPSID is always ERROR_SUCCESS.
    //
    // Sample usage:
    //    restrictedToken.addRestrictingSid(SID.ofWellKnown(WinBuiltinUsersSid));
    // Note: The list of restricting is used to force Windows to perform all
    // access checks twice. The first time using your user SID and your groups,
    // and the second time using your list of restricting sids. The access has
    // to be granted in both places to get access to the resource requested.
    public void addRestrictingSid(SID sid) {
        sidsToRestrict.add(Objects.requireNonNull(sid));  // No attributes
    }

    // Adds the logon sid of the token in the list of restricting sids for the
    // restricted token.
    public void addRestrictingSidLogonSession() {
        TOKEN_GROUPS tokenGroups = getTokenGroups(effectiveToken);

        Pointer logonSid = null;
        for (int i = 0, n = tokenGroups.getGroupCount(); i < n; ++i) {
            SID_AND_ATTRIBUTES group = tokenGroups.get(i);
            if ((group.getAttributes() & SE_GROUP_LOGON_ID) != 0) {
                logonSid = group.getSid();
                break;
            }
        }

        if (logonSid != null) {
            sidsToRestrict.add(SID.copyOf(logonSid));
        }
    }

    // Adds the owner sid of the token in the list of restricting sids for the
    // restricted token.
    public void addRestrictingSidCurrentUser() {
        TOKEN_USER tokenUser = getTokenUser(effectiveToken);
        sidsToRestrict.add(SID.copyOf(tokenUser.getUser().getSid()));
    }

    // Adds all group sids and the user sid to the restricting sids list.
    public void addRestrictingSidAllSids() {
        // Add the current user to the list.
        addRestrictingSidCurrentUser();
        TOKEN_GROUPS tokenGroups = getTokenGroups(effectiveToken);

        // Build the list of restricting sids from all groups.
        for (int i = 0, n = tokenGroups.getGroupCount(); i < n; ++i) {
            SID_AND_ATTRIBUTES group = tokenGroups.get(i);
            if ((group.getAttributes() & SE_GROUP_INTEGRITY) == 0) {
                addRestrictingSid(SID.copyOf(group.getSid()));
            }
        }
    }

    // Sets the token integrity level. This is only valid on Vista. The integrity
    // level cannot be higher than your current integrity level.
    public void setIntegrityLevel(IntegrityLevel integrityLevel) {
        this.integrityLevel = Objects.requireNonNull(integrityLevel);
    }

    // Set a flag which indicates the created token should have a locked down
    // default DACL when created.
    public void setLockdownDefaultDacl() {
        lockdownDefaultDacl = true;
    }

    @Override
    public void close() {
        Handle.close(effectiveToken);
    }

}
