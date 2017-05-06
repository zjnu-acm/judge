package com.github.zhanhb.judge.win32;

import com.sun.jna.platform.win32.WinNT;
import java.util.Arrays;

/**
 *
 * @author zhanhb
 */
@SuppressWarnings("UtilityClassWithoutPrivateConstructor")
public class Advapi32Util {

    public static WinNT.PSID newPSID(Advapi32.SID_IDENTIFIER_AUTHORITY pIdentifierAuthority,
            int... dwSubAuthorities) {
        int nSubAuthorityCount = dwSubAuthorities.length;
        if (nSubAuthorityCount > 8 || nSubAuthorityCount == 0) {
            throw new IllegalArgumentException();
        }
        int[] copy = Arrays.copyOf(dwSubAuthorities, 8);
        WinNT.PSIDByReference psidByReference = new WinNT.PSIDByReference();
        Kernel32Util.assertTrue(Advapi32.INSTANCE.AllocateAndInitializeSid(pIdentifierAuthority,
                (byte) nSubAuthorityCount,
                copy[0], copy[1], copy[2], copy[3],
                copy[4], copy[5], copy[6], copy[7],
                psidByReference));
        return psidByReference.getValue();
    }

    public static WinNT.HANDLE createRestrictedToken(
            WinNT.HANDLE existingTokenHandle,
            int /*DWORD*/ flags,
            Advapi32.SID_AND_ATTRIBUTES[] sidsToDisable,
            WinNT.LUID_AND_ATTRIBUTES[] privilegesToDelete,
            Advapi32.SID_AND_ATTRIBUTES[] sidsToRestrict) {
        WinNT.HANDLEByReference newTokenHandle = new WinNT.HANDLEByReference();
        Kernel32Util.assertTrue(Advapi32.INSTANCE.CreateRestrictedToken(
                existingTokenHandle, // ExistingTokenHandle
                flags, // Flags
                sidsToDisable != null ? sidsToDisable.length : 0, // DisableSidCount
                sidsToDisable, // SidsToDisable
                privilegesToDelete != null ? privilegesToDelete.length : 0, // DeletePrivilegeCount
                privilegesToDelete, // PrivilegesToDelete
                sidsToRestrict != null ? sidsToRestrict.length : 0, // RestrictedSidCount
                sidsToRestrict, // SidsToRestrict
                newTokenHandle // NewTokenHandle
        ));
        return newTokenHandle.getValue();
    }

    public static WinNT.HANDLE openProcessToken(WinNT.HANDLE processHandle, int desiredAccess) {
        WinNT.HANDLEByReference tokenHandle = new WinNT.HANDLEByReference();
        Kernel32Util.assertTrue(Advapi32.INSTANCE.OpenProcessToken(
                processHandle,
                desiredAccess,
                tokenHandle));
        return tokenHandle.getValue();
    }

}
