package com.github.zhanhb.judge.win32;

import com.github.zhanhb.judge.win32.struct.LUID_AND_ATTRIBUTES;
import com.github.zhanhb.judge.win32.struct.SID_AND_ATTRIBUTES;
import com.github.zhanhb.judge.win32.struct.SID_IDENTIFIER_AUTHORITY;
import java.util.Arrays;
import jnc.foreign.byref.AddressByReference;

/**
 *
 * @author zhanhb
 */
@SuppressWarnings("UtilityClassWithoutPrivateConstructor")
class Advapi32Util {

    static long newPSID(SID_IDENTIFIER_AUTHORITY pIdentifierAuthority,
            int... dwSubAuthorities) {
        int nSubAuthorityCount = dwSubAuthorities.length;
        if (nSubAuthorityCount > 8 || nSubAuthorityCount == 0) {
            throw new IllegalArgumentException();
        }
        int[] copy = Arrays.copyOf(dwSubAuthorities, 8);
        AddressByReference psidByReference = new AddressByReference();
        Kernel32Util.assertTrue(Advapi32.INSTANCE.AllocateAndInitializeSid(pIdentifierAuthority,
                (byte) nSubAuthorityCount,
                copy[0], copy[1], copy[2], copy[3],
                copy[4], copy[5], copy[6], copy[7],
                psidByReference));
        return psidByReference.getValue();
    }

    static long createRestrictedToken(
            long /*HANDLE*/ existingTokenHandle,
            int /*DWORD*/ flags,
            SID_AND_ATTRIBUTES[] sidsToDisable,
            LUID_AND_ATTRIBUTES[] privilegesToDelete,
            SID_AND_ATTRIBUTES[] sidsToRestrict) {
        AddressByReference newTokenHandle = new AddressByReference();
        Kernel32Util.assertTrue(Advapi32.INSTANCE.CreateRestrictedToken(
                existingTokenHandle, // ExistingTokenHandle
                flags, // Flags
                getLength(sidsToDisable), // DisableSidCount
                sidsToDisable, // SidsToDisable
                getLength(privilegesToDelete), // DeletePrivilegeCount
                privilegesToDelete, // PrivilegesToDelete
                getLength(sidsToRestrict), // RestrictedSidCount
                sidsToRestrict, // SidsToRestrict
                newTokenHandle // NewTokenHandle
        ));
        return newTokenHandle.getValue();
    }

    private static <T> int getLength(T[] array) {
        return array == null ? 0 : array.length;
    }

    static long openProcessToken(long /*HANDLE*/ processHandle, int desiredAccess) {
        AddressByReference tokenHandle = new AddressByReference();
        Kernel32Util.assertTrue(Advapi32.INSTANCE.OpenProcessToken(
                processHandle,
                desiredAccess,
                tokenHandle));
        return tokenHandle.getValue();
    }

}
