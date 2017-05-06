package com.github.zhanhb.judge.win32;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.platform.win32.WinNT.PSID;
import com.sun.jna.platform.win32.WinNT.PSIDByReference;
import com.sun.jna.win32.W32APIOptions;
import java.util.List;

@SuppressWarnings({"PublicInnerClass", "PublicField"})
public interface Advapi32 extends com.sun.jna.platform.win32.Advapi32 {

    @SuppressWarnings("FieldNameHidesFieldInSuperclass")
    Advapi32 INSTANCE = Native.loadLibrary("Advapi32", Advapi32.class, W32APIOptions.UNICODE_OPTIONS);

    boolean CreateRestrictedToken(
            WinNT.HANDLE ExistingTokenHandle,
            int /*DWORD*/ Flags,
            int /*DWORD*/ DisableSidCount,
            SID_AND_ATTRIBUTES[] SidsToDisable,
            int /*DWORD*/ DeletePrivilegeCount,
            WinNT.LUID_AND_ATTRIBUTES[] PrivilegesToDelete,
            int /*DWORD*/ RestrictedSidCount,
            SID_AND_ATTRIBUTES[] SidsToRestrict,
            WinNT.HANDLEByReference NewTokenHandle
    );

    boolean AllocateAndInitializeSid(
            SID_IDENTIFIER_AUTHORITY pIdentifierAuthority,
            byte /*BYTE*/ nSubAuthorityCount,
            int /*DWORD*/ dwSubAuthority0,
            int /*DWORD*/ dwSubAuthority1,
            int /*DWORD*/ dwSubAuthority2,
            int /*DWORD*/ dwSubAuthority3,
            int /*DWORD*/ dwSubAuthority4,
            int /*DWORD*/ dwSubAuthority5,
            int /*DWORD*/ dwSubAuthority6,
            int /*DWORD*/ dwSubAuthority7,
            PSIDByReference pSid);

    class SID_IDENTIFIER_AUTHORITY extends Structure {

        public static final List<String> FIELDS = createFieldsOrder("Value");

        public byte[] Value = new byte[6]; // the length of the value must be 6

        public SID_IDENTIFIER_AUTHORITY() {
        }

        public SID_IDENTIFIER_AUTHORITY(byte... values) {
            if (values.length != 6) {
                throw new IllegalArgumentException();
            }
            this.Value = values;
        }

        @Override
        @SuppressWarnings("ReturnOfCollectionOrArrayField")
        protected List<String> getFieldOrder() {
            return FIELDS;
        }

    }

    /**
     * @param tokenHandle
     * @param tokenInformationClass TOKEN_INFORMATION_CLASS
     * @param tokenInformation
     * @param tokenInformationLength
     * @return
     * @see WinNT.TOKEN_INFORMATION_CLASS
     * @see
     * <a href="https://msdn.microsoft.com/en-us/library/windows/desktop/aa379591(v=vs.85).aspx">SetTokenInformation</a>
     */
    boolean SetTokenInformation(
            HANDLE tokenHandle,
            int /*TOKEN_INFORMATION_CLASS*/ tokenInformationClass,
            Structure tokenInformation,
            int /*DWORD*/ tokenInformationLength
    );

    class TOKEN_MANDATORY_LABEL extends Structure {

        public static final List<String> FIELDS = createFieldsOrder("Label");

        public SID_AND_ATTRIBUTES Label;

        @Override
        @SuppressWarnings("ReturnOfCollectionOrArrayField")
        protected List<String> getFieldOrder() {
            return FIELDS;
        }

    }

    class SID_AND_ATTRIBUTES extends Structure {

        public static final List<String> FIELDS = createFieldsOrder("Sid", "Attributes");

        /**
         * Pointer to a SID structure.
         */
        public Pointer Sid;

        /**
         * Specifies attributes of the SID. This value contains up to 32 one-bit
         * flags. Its meaning depends on the definition and use of the SID.
         */
        public int Attributes;

        public SID_AND_ATTRIBUTES() {
            super();
        }

        public SID_AND_ATTRIBUTES(Pointer memory) {
            super(memory);
        }

        @Override
        @SuppressWarnings("ReturnOfCollectionOrArrayField")
        protected List<String> getFieldOrder() {
            return FIELDS;
        }

    }

    /*https://msdn.microsoft.com/en-us/library/windows/desktop/aa446631(v=vs.85).aspx*/
    Pointer FreeSid(PSID pSid);

    int SECURITY_MANDATORY_UNTRUSTED_RID = 0x00000000;
    int SECURITY_MANDATORY_LOW_RID = 0x00001000;
    int SECURITY_MANDATORY_MEDIUM_RID = 0x00002000;

    int SE_GROUP_INTEGRITY = 0x00000020;

    int DISABLE_MAX_PRIVILEGE = 1;
    int SANDBOX_INERT = 2;

}
