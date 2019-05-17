package jnc.platform.win32

import jnc.foreign.LibraryLoader
import jnc.foreign.Pointer
import jnc.foreign.abi.Stdcall
import jnc.foreign.annotation.In
import jnc.foreign.annotation.Out
import jnc.foreign.byref.AddressByReference
import jnc.foreign.byref.IntByReference
import jnc.foreign.byref.PointerByReference
import jnc.foreign.typedef.int32_t
import jnc.foreign.typedef.uint32_t
import jnc.foreign.typedef.uintptr_t
import jnc.platform.StructArray

/**
 * @author zhanhb
 */
@Stdcall
interface Advapi32 {

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
     * @see [CreateRestrictedToken](https://msdn.microsoft.com/en-us/library/windows/desktop/aa446583)
    ) */
    @int32_t
    fun CreateRestrictedToken(
            @uintptr_t /*HANDLE*/ ExistingTokenHandle: Long,
            @uint32_t /*DWORD*/ Flags: Int,
            @uint32_t /*DWORD*/ DisableSidCount: Int,
            SidsToDisable: StructArray<SID_AND_ATTRIBUTES>?,
            @uint32_t /*DWORD*/ DeletePrivilegeCount: Int,
            PrivilegesToDelete: StructArray<LUID_AND_ATTRIBUTES>?,
            @uint32_t /*DWORD*/ RestrictedSidCount: Int,
            SidsToRestrict: StructArray<SID_AND_ATTRIBUTES>?,
            /* PHANDLE */ NewTokenHandle: AddressByReference
    ): Boolean

    /**
     * @param tokenHandle
     * @param tokenInformationClass TOKEN_INFORMATION_CLASS
     * @param tokenInformation
     * @param tokenInformationLength
     * @return
     * @see TOKEN_INFORMATION_CLASS
     * @see [SetTokenInformation](https://msdn.microsoft.com/en-us/library/windows/desktop/aa379591)
    ) */
    @int32_t
    fun SetTokenInformation(
            @uintptr_t /*HANDLE*/ tokenHandle: Long,
            @uint32_t /*TOKEN_INFORMATION_CLASS*/ tokenInformationClass: Int,
            @In tokenInformation: TOKEN_INFORMATION,
            @uint32_t /*DWORD*/ tokenInformationLength: Int
    ): Boolean

    @int32_t
    fun CreateProcessAsUserW(
            @uintptr_t /*HANDLE*/ hToken: Long,
            lpApplicationName: Pointer?,
            lpCommandLine: Pointer?,
            @In lpProcessAttributes: SECURITY_ATTRIBUTES?,
            @In lpThreadAttributes: SECURITY_ATTRIBUTES?,
            @uint32_t bInheritHandles: Boolean,
            @uint32_t /*DWORD*/ dwCreationFlags: Int,
            lpEnvironment: Pointer?,
            lpCurrentDirectory: Pointer?,
            @In lpStartupInfo: STARTUPINFO,
            @Out lpProcessInformation: PROCESS_INFORMATION): Boolean

    @int32_t
    fun OpenProcessToken(
            @uintptr_t /*HANDLE*/ processHandle: Long,
            @uint32_t desiredAccess: Int,
            /*PHANDLE*/ tokenHandle: AddressByReference): Boolean

    @uint32_t
    fun GetLengthSid(@uintptr_t pSid: Long): /* UINT */ Int

    @int32_t
    fun ConvertSidToStringSidW(@uintptr_t sid: Long, /* LPTSTR* */ stringSid: PointerByReference): Boolean

    @int32_t
    fun ConvertStringSidToSidW(StringSid: Pointer, /* PSID* */ Sid: AddressByReference): Boolean

    @int32_t
    fun GetTokenInformation(
            @uintptr_t TokenHandle: Long,
            @uint32_t /*TOKEN_INFORMATION_CLASS*/ TokenInformationClass: Int,
            TokenInformation: TOKEN_INFORMATION?,
            @uint32_t TokenInformationLength: Int,
            ReturnLength: IntByReference): Boolean

    @int32_t
    fun DuplicateToken(
            @uintptr_t ExistingTokenHandle: Long,
            @uint32_t /*SECURITY_IMPERSONATION_LEVEL*/ ImpersonationLevel: Int,
            DuplicateTokenHandle: AddressByReference): Boolean

    @int32_t
    fun DuplicateTokenEx(
            @uintptr_t /*HANDLE*/ hExistingToken: Long,
            @uint32_t /*DWORD*/ dwDesiredAccess: Int,
            lpTokenAttributes: SECURITY_ATTRIBUTES?,
            @uint32_t /*SECURITY_IMPERSONATION_LEVEL*/ ImpersonationLevel: Int,
            @uint32_t /*TOKEN_TYPE*/ TokenType: Int,
            phNewToken: AddressByReference
    ): Boolean

    @int32_t
    fun LookupPrivilegeValueW(lpSystemName: Pointer?, lpName: Pointer, luid: LUID): Boolean

    @uint32_t
    fun SetEntriesInAclW(
            @uint32_t cCountOfExplicitEntries: Int,
            pListOfExplicitEntries: EXPLICIT_ACCESS?,
            @uintptr_t /*PACL*/ OldAcl: Long,
            /* PACL* */ new_dacl: AddressByReference): Int

    @uint32_t
    fun CreateWellKnownSid(
            @uint32_t WellKnownSidType: Int,
            @uintptr_t /*PSID*/ DomainSid: Long,
            /*PSID*/ pSid: SID,
            cbSid: IntByReference): Boolean

    @int32_t
    fun EqualSid(@uintptr_t pSid1: Long, @uintptr_t pSid2: Long): Boolean

    @int32_t
    fun CopySid(
            @uint32_t nDestinationSidLength: Int,
            pDestinationSid: SID,
            @uintptr_t pSourceSid: Long): Boolean

    @int32_t
    fun LookupPrivilegeNameW(lpSystemName: Pointer?, lpLuid: LUID,
                             lpName: CharArray, cchName: IntByReference): Boolean

    @int32_t
    fun IsValidSid(pSid: SID): Boolean

    companion object {
        @JvmField
        val INSTANCE = LibraryLoader.create(Advapi32::class.java).load("advapi32")
    }

}
