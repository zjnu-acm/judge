package cn.edu.zjnu.acm.judge.sandbox.win32;

import jnc.platform.win32.Handle;
import jnc.platform.win32.JOBOBJECTINFOCLASS;
import jnc.platform.win32.JOBOBJECT_BASIC_LIMIT_INFORMATION;
import jnc.platform.win32.JOBOBJECT_BASIC_UI_RESTRICTIONS;
import jnc.platform.win32.JobObjectInformation;
import jnc.platform.win32.Kernel32;
import jnc.platform.win32.Kernel32Util;

import static jnc.platform.win32.JOBOBJECTINFOCLASS.JobObjectBasicLimitInformation;
import static jnc.platform.win32.JOBOBJECTINFOCLASS.JobObjectBasicUIRestrictions;
import static jnc.platform.win32.WinNT.JOB_OBJECT_LIMIT_ACTIVE_PROCESS;
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
public class Job extends Handle {

    private static long assertNotZero(long value) {
        Kernel32Util.assertTrue(value != 0);
        return value;
    }

    public Job() {
        super(assertNotZero(Kernel32.INSTANCE.CreateJobObjectW(null, null)));
    }

    private void setInformationJobObject(JOBOBJECTINFOCLASS jobobjectinfoclass, JobObjectInformation jobj) {
        Kernel32Util.assertTrue(Kernel32.INSTANCE.SetInformationJobObject(getValue(), jobobjectinfoclass, jobj, jobj.size()));
    }

    public void init() {
        JOBOBJECT_BASIC_LIMIT_INFORMATION jobli = new JOBOBJECT_BASIC_LIMIT_INFORMATION();
        jobli.setActiveProcessLimit(1);
        // These are the only 1 restrictions I want placed on the job (process).
        jobli.setLimitFlags(JOB_OBJECT_LIMIT_ACTIVE_PROCESS);
        setInformationJobObject(JobObjectBasicLimitInformation, jobli);

        // Second, set some UI restrictions.
        JOBOBJECT_BASIC_UI_RESTRICTIONS jbur = new JOBOBJECT_BASIC_UI_RESTRICTIONS();
        jbur.setUiRestrictionsClass(
                // The process can't access USER objects (such as other windows)
                // in the system.
                JOB_OBJECT_UILIMIT_HANDLES
                | JOB_OBJECT_UILIMIT_READCLIPBOARD
                | JOB_OBJECT_UILIMIT_WRITECLIPBOARD
                | JOB_OBJECT_UILIMIT_SYSTEMPARAMETERS
                | JOB_OBJECT_UILIMIT_DISPLAYSETTINGS
                | JOB_OBJECT_UILIMIT_GLOBALATOMS
                | // Prevents processes associated with the job from creating desktops
                // and switching desktops using the CreateDesktop and SwitchDesktop functions.
                JOB_OBJECT_UILIMIT_DESKTOP
                | // The process can't log off the system.
                JOB_OBJECT_UILIMIT_EXITWINDOWS);
        setInformationJobObject(JobObjectBasicUIRestrictions, jbur);
    }

    public void assignProcess(long /*HANDLE*/ hProcess) {
        Kernel32Util.assertTrue(Kernel32.INSTANCE.AssignProcessToJobObject(getValue(), hProcess));
    }

}
