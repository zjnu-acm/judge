package com.github.zhanhb.judge.win32;

import com.github.zhanhb.judge.win32.struct.LUID_AND_ATTRIBUTES;
import com.github.zhanhb.judge.win32.struct.SID_AND_ATTRIBUTES;
import com.github.zhanhb.judge.win32.struct.SID_IDENTIFIER_AUTHORITY;
import java.util.Arrays;
import jnr.ffi.Pointer;
import jnr.ffi.byref.PointerByReference;

/**
 *
 * @author zhanhb
 */
@SuppressWarnings("UtilityClassWithoutPrivateConstructor")
class Advapi32Util {

    static Pointer newPSID(SID_IDENTIFIER_AUTHORITY pIdentifierAuthority,
            int... dwSubAuthorities) {
        int nSubAuthorityCount = dwSubAuthorities.length;
        if (nSubAuthorityCount > 8 || nSubAuthorityCount == 0) {
            throw new IllegalArgumentException();
        }
        int[] copy = Arrays.copyOf(dwSubAuthorities, 8);
        PointerByReference psidByReference = new PointerByReference();
        Kernel32Util.assertTrue(Advapi32.INSTANCE.AllocateAndInitializeSid(pIdentifierAuthority,
                (byte) nSubAuthorityCount,
                copy[0], copy[1], copy[2], copy[3],
                copy[4], copy[5], copy[6], copy[7],
                psidByReference));
        return psidByReference.getValue();
    }

    static Pointer createRestrictedToken(
            Pointer /*HANDLE*/ existingTokenHandle,
            int /*DWORD*/ flags,
            SID_AND_ATTRIBUTES[] sidsToDisable,
            LUID_AND_ATTRIBUTES[] privilegesToDelete,
            SID_AND_ATTRIBUTES[] sidsToRestrict) {
        PointerByReference newTokenHandle = new PointerByReference();
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

    static Pointer openProcessToken(Pointer /*HANDLE*/ processHandle, int desiredAccess) {
        PointerByReference tokenHandle = new PointerByReference();
        Kernel32Util.assertTrue(Advapi32.INSTANCE.OpenProcessToken(
                processHandle,
                desiredAccess,
                tokenHandle));
        return tokenHandle.getValue();
    }

}
