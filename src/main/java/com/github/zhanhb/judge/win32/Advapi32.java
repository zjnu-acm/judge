package com.github.zhanhb.judge.win32;

import com.github.zhanhb.judge.win32.struct.LUID_AND_ATTRIBUTES;
import com.github.zhanhb.judge.win32.struct.SID_AND_ATTRIBUTES;
import com.github.zhanhb.judge.win32.struct.SID_IDENTIFIER_AUTHORITY;
import com.github.zhanhb.judge.win32.struct.TOKEN_INFORMATION;
import com.github.zhanhb.judge.win32.struct.TOKEN_INFORMATION_CLASS;
import jnc.foreign.LibraryLoader;
import jnc.foreign.abi.Stdcall;
import jnc.foreign.annotation.In;
import jnc.foreign.byref.AddressByReference;
import jnc.foreign.byref.PointerByReference;
import jnc.foreign.typedef.int32_t;
import jnc.foreign.typedef.uint32_t;
import jnc.foreign.typedef.uint8_t;
import jnc.foreign.typedef.uintptr_t;

@Stdcall
public interface Advapi32 {

    Advapi32 INSTANCE = LibraryLoader.create(Advapi32.class).load("Advapi32");

    @int32_t
    boolean CreateRestrictedToken(
            @uintptr_t long /*HANDLE*/ ExistingTokenHandle,
            @uint32_t int /*DWORD*/ Flags,
            @uint32_t int /*DWORD*/ DisableSidCount,
            @In SID_AND_ATTRIBUTES[] SidsToDisable,
            @uint32_t int /*DWORD*/ DeletePrivilegeCount,
            @In LUID_AND_ATTRIBUTES[] PrivilegesToDelete,
            @uint32_t int /*DWORD*/ RestrictedSidCount,
            @In SID_AND_ATTRIBUTES[] SidsToRestrict,
            AddressByReference /* PHANDLE */ NewTokenHandle
    );

    @int32_t
    boolean AllocateAndInitializeSid(
            @In SID_IDENTIFIER_AUTHORITY pIdentifierAuthority,
            @uint8_t byte /*BYTE*/ nSubAuthorityCount,
            @uint32_t int /*DWORD*/ dwSubAuthority0,
            @uint32_t int /*DWORD*/ dwSubAuthority1,
            @uint32_t int /*DWORD*/ dwSubAuthority2,
            @uint32_t int /*DWORD*/ dwSubAuthority3,
            @uint32_t int /*DWORD*/ dwSubAuthority4,
            @uint32_t int /*DWORD*/ dwSubAuthority5,
            @uint32_t int /*DWORD*/ dwSubAuthority6,
            @uint32_t int /*DWORD*/ dwSubAuthority7,
            AddressByReference /* PSID* */ pSid);

    /**
     * @param tokenHandle
     * @param tokenInformationClass TOKEN_INFORMATION_CLASS
     * @param tokenInformation
     * @param tokenInformationLength
     * @return
     * @see TOKEN_INFORMATION_CLASS
     * @see
     * <a href="https://msdn.microsoft.com/en-us/library/windows/desktop/aa379591(v=vs.85).aspx">SetTokenInformation</a>
     */
    @int32_t
    boolean SetTokenInformation(
            @uintptr_t long /*HANDLE*/ tokenHandle,
            @uint32_t int /*TOKEN_INFORMATION_CLASS*/ tokenInformationClass,
            @In TOKEN_INFORMATION tokenInformation,
            @uint32_t int /*DWORD*/ tokenInformationLength
    );

    /*https://msdn.microsoft.com/en-us/library/windows/desktop/aa446631(v=vs.85).aspx*/
    @uintptr_t
    long FreeSid(@In @uintptr_t long pSid);

    int SECURITY_MANDATORY_UNTRUSTED_RID = 0x00000000;
    int SECURITY_MANDATORY_LOW_RID = 0x00001000;
    int SECURITY_MANDATORY_MEDIUM_RID = 0x00002000;

    int SE_GROUP_INTEGRITY = 0x00000020;

    int DISABLE_MAX_PRIVILEGE = 1;
    int SANDBOX_INERT = 2;

    @int32_t
    boolean OpenProcessToken(
            @uintptr_t long /*HANDLE*/ processHandle,
            @uint32_t int desiredAccess,
            AddressByReference /*PHANDLE*/ tokenHandle);

    @uint32_t
    /* UINT */ int GetLengthSid(@uintptr_t long pSid);

    @int32_t
    boolean ConvertSidToStringSidW(@uintptr_t long sid, PointerByReference /* LPTSTR* */ stringSid);

}
