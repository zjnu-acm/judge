package cn.edu.zjnu.acm.judge.sandbox.win32;

import java.io.Closeable;
import java.io.IOException;
import jnc.platform.win32.Handle;
import jnc.platform.win32.JOBOBJECTINFOCLASS;
import jnc.platform.win32.JOBOBJECT_BASIC_LIMIT_INFORMATION;
import jnc.platform.win32.JOBOBJECT_BASIC_UI_RESTRICTIONS;
import jnc.platform.win32.JOBOBJECT_EXTENDED_LIMIT_INFORMATION;
import jnc.platform.win32.JobObjectInformation;
import jnc.platform.win32.Kernel32;
import jnc.platform.win32.Kernel32Util;
import jnc.platform.win32.WString;
import jnc.platform.win32.Win32Exception;

import static jnc.platform.win32.JOBOBJECTINFOCLASS.JobObjectBasicUIRestrictions;
import static jnc.platform.win32.JOBOBJECTINFOCLASS.JobObjectExtendedLimitInformation;
import static jnc.platform.win32.WinError.ERROR_BAD_ARGUMENTS;
import static jnc.platform.win32.WinNT.JOB_OBJECT_LIMIT_ACTIVE_PROCESS;
import static jnc.platform.win32.WinNT.JOB_OBJECT_LIMIT_DIE_ON_UNHANDLED_EXCEPTION;
import static jnc.platform.win32.WinNT.JOB_OBJECT_LIMIT_KILL_ON_JOB_CLOSE;
import static jnc.platform.win32.WinNT.JOB_OBJECT_LIMIT_PROCESS_MEMORY;
import static jnc.platform.win32.WinNT.JOB_OBJECT_UILIMIT_DESKTOP;
import static jnc.platform.win32.WinNT.JOB_OBJECT_UILIMIT_DISPLAYSETTINGS;
import static jnc.platform.win32.WinNT.JOB_OBJECT_UILIMIT_EXITWINDOWS;
import static jnc.platform.win32.WinNT.JOB_OBJECT_UILIMIT_GLOBALATOMS;
import static jnc.platform.win32.WinNT.JOB_OBJECT_UILIMIT_HANDLES;
import static jnc.platform.win32.WinNT.JOB_OBJECT_UILIMIT_READCLIPBOARD;
import static jnc.platform.win32.WinNT.JOB_OBJECT_UILIMIT_SYSTEMPARAMETERS;
import static jnc.platform.win32.WinNT.JOB_OBJECT_UILIMIT_WRITECLIPBOARD;

/**
 * @author zhanhb
 */
public class Job implements Closeable {

    private static void setInformationJobObject(long jobHandle, JOBOBJECTINFOCLASS jobobjectinfoclass, JobObjectInformation jobj) {
        Kernel32Util.assertTrue(Kernel32.INSTANCE.SetInformationJobObject(jobHandle, jobobjectinfoclass, jobj, jobj.size()));
    }

    private final long jobHandle;

    @SuppressWarnings({"fallthrough", "BroadCatchBlock", "TooBroadCatch", "UseSpecificCatch"})
    public Job(JobLevel securityLevel, String jobName, int uiExceptions, long memoryLimit) {
        long handle = Kernel32.INSTANCE.CreateJobObjectW(null, WString.toNative(jobName));
        Kernel32Util.assertTrue(handle != 0);

        try {
            JOBOBJECT_EXTENDED_LIMIT_INFORMATION jeli = new JOBOBJECT_EXTENDED_LIMIT_INFORMATION();

            int limitFlags = 0;
            int uiRestrictionsClass = 0;
            switch (securityLevel) {
                case JOB_LOCKDOWN:
                    limitFlags |= JOB_OBJECT_LIMIT_DIE_ON_UNHANDLED_EXCEPTION;
                case JOB_RESTRICTED:
                    uiRestrictionsClass |= JOB_OBJECT_UILIMIT_WRITECLIPBOARD;
                    uiRestrictionsClass |= JOB_OBJECT_UILIMIT_READCLIPBOARD;
                    uiRestrictionsClass |= JOB_OBJECT_UILIMIT_HANDLES;
                    uiRestrictionsClass |= JOB_OBJECT_UILIMIT_GLOBALATOMS;
                case JOB_LIMITED_USER:
                    uiRestrictionsClass |= JOB_OBJECT_UILIMIT_DISPLAYSETTINGS;
                    limitFlags |= JOB_OBJECT_LIMIT_ACTIVE_PROCESS;
                    jeli.getBasicLimitInformation().setActiveProcessLimit(1);
                case JOB_INTERACTIVE:
                    uiRestrictionsClass |= JOB_OBJECT_UILIMIT_SYSTEMPARAMETERS;
                    uiRestrictionsClass |= JOB_OBJECT_UILIMIT_DESKTOP;
                    uiRestrictionsClass |= JOB_OBJECT_UILIMIT_EXITWINDOWS;
                case JOB_UNPROTECTED:
                    if (memoryLimit != 0) {
                        limitFlags |= JOB_OBJECT_LIMIT_PROCESS_MEMORY;
                        jeli.setProcessMemoryLimit(memoryLimit);
                    }
                    limitFlags |= JOB_OBJECT_LIMIT_KILL_ON_JOB_CLOSE;
                    break;
                default:
                    throw new Win32Exception(ERROR_BAD_ARGUMENTS);
            }
            jeli.getBasicLimitInformation().setLimitFlags(limitFlags);
            setInformationJobObject(handle, JobObjectExtendedLimitInformation, jeli);

            JOBOBJECT_BASIC_UI_RESTRICTIONS jbur = new JOBOBJECT_BASIC_UI_RESTRICTIONS();
            jbur.setUiRestrictionsClass(uiRestrictionsClass & ~uiExceptions);
            setInformationJobObject(handle, JobObjectBasicUIRestrictions, jbur);
        } catch (Throwable t) {
            Handle.close(handle);
            throw t;
        }
        this.jobHandle = handle;
    }

    public void assignProcessToJob(long /*HANDLE*/ hProcess) {
        Kernel32Util.assertTrue(Kernel32.INSTANCE.AssignProcessToJobObject(jobHandle, hProcess));
    }

    @Override
    public void close() throws IOException {
        Handle.close(jobHandle);
    }
}
