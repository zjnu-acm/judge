package com.github.zhanhb.jnc.platform.win32;

import jnc.foreign.LibraryLoader;
import jnc.foreign.Pointer;
import jnc.foreign.abi.Stdcall;
import jnc.foreign.annotation.In;
import jnc.foreign.annotation.Out;
import jnc.foreign.byref.AddressByReference;
import jnc.foreign.byref.IntByReference;
import jnc.foreign.byref.PointerByReference;
import jnc.foreign.typedef.int32_t;
import jnc.foreign.typedef.uint32_t;
import jnc.foreign.typedef.uintptr_t;

@Stdcall
public interface Advapi32 {

    Advapi32 INSTANCE = LibraryLoader.create(Advapi32.class).load("Advapi32");

    /**
     *
     * @param ExistingTokenHandle
     * @param Flags
     * @param DisableSidCount
     * @param SidsToDisable
     * @param DeletePrivilegeCount
     * @param PrivilegesToDelete
     * @param RestrictedSidCount
     * @param SidsToRestrict
     * @param NewTokenHandle
     * @return
     * @see
     * <a href="https://msdn.microsoft.com/en-us/library/windows/desktop/aa446583(v=vs.85).aspx">CreateRestrictedToken</a>
     */
    @int32_t
    boolean CreateRestrictedToken(
            @uintptr_t long /*HANDLE*/ ExistingTokenHandle,
            @uint32_t int /*DWORD*/ Flags,
            @uint32_t int /*DWORD*/ DisableSidCount,
            Array<SID_AND_ATTRIBUTES> SidsToDisable,
            @uint32_t int /*DWORD*/ DeletePrivilegeCount,
            Array<LUID_AND_ATTRIBUTES> PrivilegesToDelete,
            @uint32_t int /*DWORD*/ RestrictedSidCount,
            Array<SID_AND_ATTRIBUTES> SidsToRestrict,
            AddressByReference /* PHANDLE */ NewTokenHandle
    );

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

    @int32_t
    boolean CreateProcessAsUserW(
            @uintptr_t long /*HANDLE*/ hToken,
            Pointer lpApplicationName,
            Pointer lpCommandLine,
            @In SECURITY_ATTRIBUTES lpProcessAttributes,
            @In SECURITY_ATTRIBUTES lpThreadAttributes,
            @uint32_t boolean bInheritHandles,
            @uint32_t int /*DWORD*/ dwCreationFlags,
            Pointer lpEnvironment,
            Pointer lpCurrentDirectory,
            @In STARTUPINFO lpStartupInfo,
            @Out PROCESS_INFORMATION lpProcessInformation);

    @int32_t
    boolean OpenProcessToken(
            @uintptr_t long /*HANDLE*/ processHandle,
            @uint32_t int desiredAccess,
            AddressByReference /*PHANDLE*/ tokenHandle);

    @uint32_t
    /* UINT */ int GetLengthSid(@uintptr_t long pSid);

    @int32_t
    boolean ConvertSidToStringSidW(@uintptr_t long sid, PointerByReference /* LPTSTR* */ stringSid);

    @int32_t
    boolean ConvertStringSidToSidW(Pointer StringSid, AddressByReference /* PSID* */ Sid);

    @int32_t
    boolean GetTokenInformation(
            @uintptr_t long TokenHandle,
            @uint32_t int/*TOKEN_INFORMATION_CLASS*/ TokenInformationClass,
            TOKEN_INFORMATION TokenInformation,
            @uint32_t int TokenInformationLength,
            IntByReference ReturnLength);

    @int32_t
    boolean DuplicateToken(
            @uintptr_t long ExistingTokenHandle,
            @uint32_t int/*SECURITY_IMPERSONATION_LEVEL*/ ImpersonationLevel,
            AddressByReference DuplicateTokenHandle);

    @int32_t
    boolean DuplicateTokenEx(
            @uintptr_t long /*HANDLE*/ hExistingToken,
            @uint32_t int /*DWORD*/ dwDesiredAccess,
            SECURITY_ATTRIBUTES lpTokenAttributes,
            @uint32_t int /*SECURITY_IMPERSONATION_LEVEL*/ ImpersonationLevel,
            @uint32_t int /*TOKEN_TYPE*/ TokenType,
            AddressByReference phNewToken
    );

    @int32_t
    boolean LookupPrivilegeValueW(Pointer lpSystemName, Pointer lpName, LUID luid);

    @uint32_t
    int SetEntriesInAclW(
            @uint32_t int cCountOfExplicitEntries,
            EXPLICIT_ACCESS pListOfExplicitEntries,
            @uintptr_t long /*PACL*/ OldAcl,
            AddressByReference /* PACL* */ new_dacl);

    @uint32_t
    boolean CreateWellKnownSid(
            @uint32_t int WellKnownSidType,
            @uintptr_t long /*PSID*/ DomainSid,
            SID /*PSID*/ pSid,
            IntByReference cbSid);

    @int32_t
    boolean EqualSid(@uintptr_t long pSid1, @uintptr_t long pSid2);

    @int32_t
    boolean CopySid(
            @uint32_t int nDestinationSidLength,
            SID pDestinationSid,
            @uintptr_t long pSourceSid);

    @int32_t
    boolean LookupPrivilegeNameW(Pointer lpSystemName, LUID lpLuid,
            char[] lpName, IntByReference cchName);

}
