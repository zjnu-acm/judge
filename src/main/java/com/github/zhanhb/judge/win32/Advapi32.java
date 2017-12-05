package com.github.zhanhb.judge.win32;

import com.github.zhanhb.judge.win32.struct.LUID_AND_ATTRIBUTES;
import com.github.zhanhb.judge.win32.struct.SID_AND_ATTRIBUTES;
import com.github.zhanhb.judge.win32.struct.SID_IDENTIFIER_AUTHORITY;
import com.github.zhanhb.judge.win32.struct.TOKEN_INFORMATION;
import com.github.zhanhb.judge.win32.struct.TOKEN_INFORMATION_CLASS;
import jnr.ffi.annotations.In;
import jnr.ffi.annotations.Out;
import jnr.ffi.byref.AddressByReference;
import jnr.ffi.byref.PointerByReference;
import jnr.ffi.types.int32_t;
import jnr.ffi.types.u_int32_t;
import jnr.ffi.types.u_int8_t;
import jnr.ffi.types.uintptr_t;

public interface Advapi32 {

    Advapi32 INSTANCE = Native.loadLibrary("Advapi32", Advapi32.class);

    @int32_t
    /* BOOL */ int CreateRestrictedToken(
            @In @uintptr_t long /*HANDLE*/ ExistingTokenHandle,
            @In @u_int32_t int /*DWORD*/ Flags,
            @In @u_int32_t int /*DWORD*/ DisableSidCount,
            @In SID_AND_ATTRIBUTES[] SidsToDisable,
            @In @u_int32_t int /*DWORD*/ DeletePrivilegeCount,
            @In LUID_AND_ATTRIBUTES[] PrivilegesToDelete,
            @In @u_int32_t int /*DWORD*/ RestrictedSidCount,
            @In SID_AND_ATTRIBUTES[] SidsToRestrict,
            @Out AddressByReference /*Pointer of HANDLE*/ NewTokenHandle
    );

    @int32_t
    /* BOOL */ int AllocateAndInitializeSid(
            @In SID_IDENTIFIER_AUTHORITY pIdentifierAuthority,
            @In @u_int8_t byte /*BYTE*/ nSubAuthorityCount,
            @In @u_int32_t int /*DWORD*/ dwSubAuthority0,
            @In @u_int32_t int /*DWORD*/ dwSubAuthority1,
            @In @u_int32_t int /*DWORD*/ dwSubAuthority2,
            @In @u_int32_t int /*DWORD*/ dwSubAuthority3,
            @In @u_int32_t int /*DWORD*/ dwSubAuthority4,
            @In @u_int32_t int /*DWORD*/ dwSubAuthority5,
            @In @u_int32_t int /*DWORD*/ dwSubAuthority6,
            @In @u_int32_t int /*DWORD*/ dwSubAuthority7,
            @Out AddressByReference /*Pointer of SID*/ pSid);

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
    /* BOOL */ int SetTokenInformation(
            @In @uintptr_t long /*HANDLE*/ tokenHandle,
            @In @u_int32_t int /*TOKEN_INFORMATION_CLASS*/ tokenInformationClass,
            @In TOKEN_INFORMATION tokenInformation,
            @In @u_int32_t int /*DWORD*/ tokenInformationLength
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
    /* BOOL */ int OpenProcessToken(
            @In @uintptr_t long /*HANDLE*/ processHandle,
            @In @u_int32_t int desiredAccess,
            @Out AddressByReference /*HANDLE*/ tokenHandle);

    @u_int32_t
    /* UINT */ int GetLengthSid(@In @uintptr_t long pSid);

    @int32_t
    /* BOOL */ int ConvertSidToStringSidW(@In @uintptr_t long sid, @Out PointerByReference stringSid);

}
