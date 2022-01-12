package jnc.platform.win32;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import jnc.foreign.LibraryLoader;
import jnc.foreign.Pointer;
import jnc.foreign.StructArray;
import jnc.foreign.annotation.In;
import jnc.foreign.annotation.Out;
import jnc.foreign.annotation.Stdcall;
import jnc.foreign.byref.AddressByReference;
import jnc.foreign.byref.IntByReference;
import jnc.foreign.byref.PointerByReference;
import jnc.foreign.typedef.int32_t;
import jnc.foreign.typedef.uint32_t;
import jnc.foreign.typedef.uintptr_t;

@Stdcall
@ParametersAreNonnullByDefault
@SuppressWarnings("SpellCheckingInspection")
public interface Advapi32 {

    @Nonnull
    Advapi32 INSTANCE = LibraryLoader.create(Advapi32.class).load("advapi32");

    /**
     * <a href="https://msdn.microsoft.com/en-us/library/windows/desktop/aa446583">CreateRestrictedToken</a>
     */
    @int32_t
    boolean CreateRestrictedToken(
            @uintptr_t long ExistingTokenHandle,
            @uint32_t int Flags,
            @uint32_t int DisableSidCount,
            @Nullable StructArray<SID_AND_ATTRIBUTES> SidsToDisable,
            @uint32_t int DeletePrivilegeCount,
            @Nullable StructArray<LUID_AND_ATTRIBUTES> PrivilegesToDelete,
            @uint32_t int RestrictedSidCount,
            @Nullable StructArray<SID_AND_ATTRIBUTES> SidsToRestrict,
            AddressByReference NewTokenHandle);

    /**
     * <a href="https://msdn.microsoft.com/en-us/library/windows/desktop/aa379591">SetTokenInformation</a>
     */
    @int32_t
    boolean SetTokenInformation(
            @uintptr_t long tokenHandle,
            TOKEN_INFORMATION_CLASS tokenInformationClass,
            @In TokenInformation tokenInformation,
            @uint32_t int tokenInformationLength);

    @int32_t
    boolean CreateProcessAsUserW(
            @uintptr_t long hToken,
            @Nullable Pointer lpApplicationName,
            @Nullable Pointer lpCommandLine,
            @In @Nullable SECURITY_ATTRIBUTES lpProcessAttributes,
            @In @Nullable SECURITY_ATTRIBUTES lpThreadAttributes,
            @uint32_t boolean bInheritHandles,
            @uint32_t int dwCreationFlags,
            @Nullable Pointer lpEnvironment,
            @Nullable Pointer lpCurrentDirectory,
            @In STARTUPINFO lpStartupInfo,
            @Out PROCESS_INFORMATION lpProcessInformation);

    @int32_t
    boolean OpenProcessToken(
            @uintptr_t long processHandle,
            @uint32_t int desiredAccess,
            AddressByReference tokenHandle);

    @uint32_t
    int GetLengthSid(Pointer pSid);

    @int32_t
    boolean ConvertSidToStringSidW(Pointer sid, PointerByReference /* LPTSTR* */ stringSid);

    @int32_t
    boolean ConvertStringSidToSidW(Pointer StringSid, PointerByReference /* PSID* */ Sid);

    @int32_t
    boolean GetTokenInformation(
            @uintptr_t long TokenHandle,
            TOKEN_INFORMATION_CLASS TokenInformationClass,
            @Nullable TokenInformation TokenInformation,
            @uint32_t int TokenInformationLength,
            IntByReference IntByReference);

    @int32_t
    boolean DuplicateToken(
            @uintptr_t long ExistingTokenHandle,
            SECURITY_IMPERSONATION_LEVEL SECURITY_IMPERSONATION_LEVEL,
            AddressByReference AddressByReference);

    @int32_t
    boolean DuplicateTokenEx(
            @uintptr_t long /*HANDLE*/ hExistingToken,
            @uint32_t int /*DWORD*/ dwDesiredAccess,
            @Nullable SECURITY_ATTRIBUTES lpTokenAttributes,
            SECURITY_IMPERSONATION_LEVEL ImpersonationLevel,
            TOKEN_TYPE TokenType,
            AddressByReference phNewToken);

    @int32_t
    boolean LookupPrivilegeValueW(@Nullable Pointer lpSystemName, Pointer lpName, LUID luid);

    @uint32_t
    int SetEntriesInAclW(
            @uint32_t int cCountOfExplicitEntries,
            @Nullable EXPLICIT_ACCESS pListOfExplicitEntries,
            @Nullable Pointer OldAcl,
            PointerByReference new_dacl);

    @uint32_t
    boolean CreateWellKnownSid(
            WELL_KNOWN_SID_TYPE WellKnownSidType,
            @uintptr_t long /*PSID*/ DomainSid,
            SID pSid,
            IntByReference cbSid);

    @int32_t
    boolean EqualSid(Pointer pSid1, Pointer pSid2);

    @int32_t
    boolean CopySid(@uint32_t int nDestinationSidLength, SID pDestinationSid, Pointer pSourceSid);

    @int32_t
    boolean LookupPrivilegeNameW(@Nullable Pointer lpSystemName, LUID lpLuid, char[] lpName, IntByReference cchName);

    @int32_t
    boolean IsValidSid(SID pSid);

    @int32_t
    boolean AdjustTokenPrivileges(
            @uintptr_t long /*HANDLE*/ TokenHandle,
            @uint32_t boolean DisableAllPrivileges,
            TOKEN_PRIVILEGES NewState,
            @uint32_t int BufferLength,
            @Nullable TOKEN_PRIVILEGES PreviousState,
            @Nullable IntByReference ReturnLength);
}
