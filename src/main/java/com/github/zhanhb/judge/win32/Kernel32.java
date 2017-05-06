package com.github.zhanhb.judge.win32;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.win32.W32APIOptions;
import java.util.List;
import javax.annotation.Nullable;

@SuppressWarnings({"PublicInnerClass", "PublicField"})
public interface Kernel32 extends com.sun.jna.platform.win32.Kernel32 {

    @SuppressWarnings("FieldNameHidesFieldInSuperclass")
    Kernel32 INSTANCE = Native.loadLibrary("kernel32", Kernel32.class, W32APIOptions.UNICODE_OPTIONS);

    boolean AssignProcessToJobObject(HANDLE hJob, HANDLE hProcess);

    /**
     * Controls whether the system will handle the specified types of serious
     * errors or whether the process will handle them.
     *
     *
     * @param uMode The process error mode. This parameter can be one or more of
     * the following values.
     * @return The return value is the previous state of the error-mode bit
     * flags.
     * @see
     * <a href="https://msdn.microsoft.com/en-us/library/windows/desktop/ms680621(v=vs.85).aspx">SetErrorMode</a>
     * @see #SEM_FAILCRITICALERRORS
     * @see #SEM_NOALIGNMENTFAULTEXCEPT
     * @see #SEM_NOGPFAULTERRORBOX
     * @see #SEM_NOOPENFILEERRORBOX
     */
    int SetErrorMode(int /* UINT */ uMode);

    /**
     * The system does not display the critical-error-handler message box.
     * Instead, the system sends the error to the calling process.
     */
    int SEM_FAILCRITICALERRORS = 0x0001;
    /**
     * The system automatically fixes memory alignment faults and makes them
     * invisible to the application. It does this for the calling process and
     * any descendant processes. This feature is only supported by certain
     * processor architectures. For more information, see the Remarks section.
     */
    int SEM_NOALIGNMENTFAULTEXCEPT = 0x0004;
    /**
     * The system does not display the Windows Error Reporting dialog.
     */
    int SEM_NOGPFAULTERRORBOX = 0x0002;
    /**
     * The system does not display a message box when it fails to find a file.
     * Instead, the error is returned to the calling process.
     */
    int SEM_NOOPENFILEERRORBOX = 0x8000;

    /**
     *
     * @param hThread A handle to the thread to be restarted.
     * @return If the function succeeds, the return value is the thread's
     * previous suspend count. If the function fails, the return value is
     * (DWORD) -1. To get extended error information, call GetLastError.
     */
    int ResumeThread(HANDLE hThread);

    /**
     *
     * @param hProcess A handle to the process whose timing information is
     * sought.
     * @param lpCreationTime A pointer to a FILETIME structure that receives the
     * creation time of the process.
     * @param lpExitTime A pointer to a FILETIME structure that receives the
     * exit time of the process. If the process has not exited, the content of
     * this structure is undefined.
     * @param lpKernelTime A pointer to a FILETIME structure that receives the
     * amount of time that the process has executed in kernel mode. The time
     * that each of the threads of the process has executed in kernel mode is
     * determined, and then all of those times are summed together to obtain
     * this value.
     * @param lpUserTime A pointer to a FILETIME structure that receives the
     * amount of time that the process has executed in user mode. The time that
     * each of the threads of the process has executed in user mode is
     * determined, and then all of those times are summed together to obtain
     * this value. Note that this value can exceed the amount of real time
     * elapsed (between lpCreationTime and lpExitTime) if the process executes
     * across multiple CPU cores.
     * @return If the function succeeds, the return value is nonzero. If the
     * function fails, the return value is zero. To get extended error
     * information, call GetLastError.
     * @see
     * <a href="https://msdn.microsoft.com/en-us/library/windows/desktop/ms683223(v=vs.85).aspx">GetProcessTimes</a>
     */
    boolean GetProcessTimes(HANDLE hProcess, WinBase.FILETIME lpCreationTime, WinBase.FILETIME lpExitTime, WinBase.FILETIME lpKernelTime, WinBase.FILETIME lpUserTime);

    @Nullable
    HANDLE CreateJobObject(SECURITY_ATTRIBUTES lpJobAttributes, String lpName);

    abstract class JOBOBJECT_INFORMATION extends Structure {
    }

    /**
     *
     * @see JOBOBJECTINFOCLASS#JobObjectBasicLimitInformation
     * @see
     * <a href="https://msdn.microsoft.com/en-us/library/windows/desktop/ms684147(v=vs.85).aspx">JOBOBJECT_BASIC_LIMIT_INFORMATION
     * </a>
     */
    class JOBOBJECT_BASIC_LIMIT_INFORMATION extends JOBOBJECT_INFORMATION {

        public static final List<String> FIELDS = createFieldsOrder("PerProcessUserTimeLimit",
                "PerJobUserTimeLimit",
                "LimitFlags",
                "MinimumWorkingSetSize",
                "MaximumWorkingSetSize",
                "ActiveProcessLimit",
                "Affinity",
                "PriorityClass",
                "SchedulingClass");

        public long PerProcessUserTimeLimit;
        public long PerJobUserTimeLimit;
        public int LimitFlags;
        public SIZE_T MinimumWorkingSetSize;
        public SIZE_T MaximumWorkingSetSize;
        public int ActiveProcessLimit;
        public ULONG_PTR Affinity;
        public int PriorityClass;
        public int SchedulingClass;

        @Override
        @SuppressWarnings("ReturnOfCollectionOrArrayField")
        protected List<String> getFieldOrder() {
            return FIELDS;
        }

    }

    int JOB_OBJECT_LIMIT_WORKINGSET = 0x0001;
    int JOB_OBJECT_LIMIT_PROCESS_TIME = 0x0002;
    int JOB_OBJECT_LIMIT_JOB_TIME = 0x0004;
    int JOB_OBJECT_LIMIT_ACTIVE_PROCESS = 0x0008;
    int JOB_OBJECT_LIMIT_AFFINITY = 0x0010;
    int JOB_OBJECT_LIMIT_PRIORITY_CLASS = 0x0020;
    int JOB_OBJECT_LIMIT_PRESERVE_JOB_TIME = 0x0040;
    int JOB_OBJECT_LIMIT_SCHEDULING_CLASS = 0x0080;
    int JOB_OBJECT_LIMIT_PROCESS_MEMORY = 0x0100;
    int JOB_OBJECT_LIMIT_JOB_MEMORY = 0x0200;
    int JOB_OBJECT_LIMIT_DIE_ON_UNHANDLED_EXCEPTION = 0x0400;
    int JOB_OBJECT_BREAKAWAY_OK = 0x0800;
    int JOB_OBJECT_SILENT_BREAKAWAY = 0x1000;

    int JOB_OBJECT_LIMIT_BREAKAWAY_OK = 0x00000800;
    int JOB_OBJECT_LIMIT_SILENT_BREAKAWAY_OK = 0x00001000;
    int JOB_OBJECT_LIMIT_KILL_ON_JOB_CLOSE = 0x00002000;
    int JOB_OBJECT_LIMIT_SUBSET_AFFINITY = 0x00004000;

    /* JOBOBJECT_BASIC_UI_RESTRICTIONS.UIRestrictionsClass constants */
    int JOB_OBJECT_UILIMIT_HANDLES = 0x0001;
    int JOB_OBJECT_UILIMIT_READCLIPBOARD = 0x0002;
    int JOB_OBJECT_UILIMIT_WRITECLIPBOARD = 0x0004;
    int JOB_OBJECT_UILIMIT_SYSTEMPARAMETERS = 0x0008;
    int JOB_OBJECT_UILIMIT_DISPLAYSETTINGS = 0x0010;
    int JOB_OBJECT_UILIMIT_GLOBALATOMS = 0x0020;
    int JOB_OBJECT_UILIMIT_DESKTOP = 0x0040;
    int JOB_OBJECT_UILIMIT_EXITWINDOWS = 0x0080;

    /* JOBOBJECT_SECURITY_LIMIT_INFORMATION.SecurityLimitFlags constants */
    int JOB_OBJECT_SECURITY_NO_ADMIN = 0x0001;
    int JOB_OBJECT_SECURITY_RESTRICTED_TOKEN = 0x0002;
    int JOB_OBJECT_SECURITY_ONLY_TOKEN = 0x0004;
    int JOB_OBJECT_SECURITY_FILTER_TOKENS = 0x0008;

    /* JOBOBJECT_END_OF_JOB_TIME_INFORMATION.EndOfJobTimeAction constants */
    int JOB_OBJECT_TERMINATE_AT_END_OF_JOB = 0;
    int JOB_OBJECT_POST_AT_END_OF_JOB = 1;

    int JOB_OBJECT_MSG_END_OF_JOB_TIME = 1;
    int JOB_OBJECT_MSG_END_OF_PROCESS_TIME = 2;
    int JOB_OBJECT_MSG_ACTIVE_PROCESS_LIMIT = 3;
    int JOB_OBJECT_MSG_ACTIVE_PROCESS_ZERO = 4;
    int JOB_OBJECT_MSG_NEW_PROCESS = 6;
    int JOB_OBJECT_MSG_EXIT_PROCESS = 7;
    int JOB_OBJECT_MSG_ABNORMAL_EXIT_PROCESS = 8;
    int JOB_OBJECT_MSG_PROCESS_MEMORY_LIMIT = 9;
    int JOB_OBJECT_MSG_JOB_MEMORY_LIMIT = 10;

    interface JOBOBJECTINFOCLASS {

        int JobObjectBasicAccountingInformation = 1;
        /**
         * @see JOBOBJECT_BASIC_LIMIT_INFORMATION
         */
        int JobObjectBasicLimitInformation = 2;
        int JobObjectBasicProcessIdList = 3;
        /**
         * @see JOBOBJECT_BASIC_UI_RESTRICTIONS
         */
        int JobObjectBasicUIRestrictions = 4;
        int JobObjectSecurityLimitInformation = 5;
        int JobObjectEndOfJobTimeInformation = 6;
        int JobObjectAssociateCompletionPortInformation = 7;
        int JobObjectBasicAndIoAccountingInformation = 8;
        int JobObjectExtendedLimitInformation = 9;
        int JobObjectJobSetInformation = 10;
        int JobObjectGroupInformation = 11;
        int JobObjectNotificationLimitInformation = 12;
        int JobObjectLimitViolationInformation = 13;
        int JobObjectGroupInformationEx = 14;
        int JobObjectCpuRateControlInformation = 15;
    }

    /**
     *
     * @param hJob
     * @param JobObjectInfoClass
     * @param lpJobObjectInfo
     * @param cbJobObjectInfoLength
     * @return If the function succeeds, the return value is nonzero. If the
     * function fails, the return value is zero. To get extended error
     * information, call GetLastError.
     * @see
     * <a href="https://msdn.microsoft.com/en-us/library/windows/desktop/ms686216(v=vs.85).aspx">SetInformationJobObject</a>
     */
    boolean SetInformationJobObject(HANDLE hJob, int JobObjectInfoClass, JOBOBJECT_INFORMATION lpJobObjectInfo, int cbJobObjectInfoLength);

    /**
     *
     * @see JOBOBJECTINFOCLASS#JobObjectBasicUIRestrictions
     * @see
     * <a href="https://msdn.microsoft.com/en-us/library/windows/desktop/ms684152(v=vs.85).aspx">JOBOBJECT_BASIC_UI_RESTRICTIONS</a>
     */
    class JOBOBJECT_BASIC_UI_RESTRICTIONS extends JOBOBJECT_INFORMATION {

        public static final List<String> FIELDS = createFieldsOrder("UIRestrictionsClass");

        public int /*DWORD*/ UIRestrictionsClass;

        @Override
        @SuppressWarnings("ReturnOfCollectionOrArrayField")
        protected List<String> getFieldOrder() {
            return FIELDS;
        }

    };

    int NORMAL_PRIORITY_CLASS = 0x00000020;
    int IDLE_PRIORITY_CLASS = 0x00000040;
    int HIGH_PRIORITY_CLASS = 0x00000080;

    boolean CreateProcessAsUser(
            HANDLE hToken,
            String lpApplicationName, String lpCommandLine,
            WinBase.SECURITY_ATTRIBUTES lpProcessAttributes,
            WinBase.SECURITY_ATTRIBUTES lpThreadAttributes,
            boolean bInheritHandles, WinDef.DWORD dwCreationFlags,
            Pointer lpEnvironment, String lpCurrentDirectory,
            WinBase.STARTUPINFO lpStartupInfo,
            WinBase.PROCESS_INFORMATION lpProcessInformation);

}
