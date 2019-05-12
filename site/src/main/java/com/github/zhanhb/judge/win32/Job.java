package com.github.zhanhb.judge.win32;

import com.github.zhanhb.jnc.platform.win32.JOBOBJECTINFOCLASS;
import com.github.zhanhb.jnc.platform.win32.JOBOBJECT_BASIC_LIMIT_INFORMATION;
import com.github.zhanhb.jnc.platform.win32.JOBOBJECT_BASIC_UI_RESTRICTIONS;
import com.github.zhanhb.jnc.platform.win32.JOBOBJECT_INFORMATION;
import com.github.zhanhb.jnc.platform.win32.Kernel32;
import com.github.zhanhb.jnc.platform.win32.Kernel32Util;
import java.io.Closeable;

import static com.github.zhanhb.jnc.platform.win32.JOBOBJECTINFOCLASS.JobObjectBasicLimitInformation;
import static com.github.zhanhb.jnc.platform.win32.JOBOBJECTINFOCLASS.JobObjectBasicUIRestrictions;
import static com.github.zhanhb.jnc.platform.win32.WinNT.JOB_OBJECT_LIMIT_ACTIVE_PROCESS;
import static com.github.zhanhb.jnc.platform.win32.WinNT.JOB_OBJECT_UILIMIT_DESKTOP;
import static com.github.zhanhb.jnc.platform.win32.WinNT.JOB_OBJECT_UILIMIT_DISPLAYSETTINGS;
import static com.github.zhanhb.jnc.platform.win32.WinNT.JOB_OBJECT_UILIMIT_EXITWINDOWS;
import static com.github.zhanhb.jnc.platform.win32.WinNT.JOB_OBJECT_UILIMIT_GLOBALATOMS;
import static com.github.zhanhb.jnc.platform.win32.WinNT.JOB_OBJECT_UILIMIT_HANDLES;
import static com.github.zhanhb.jnc.platform.win32.WinNT.JOB_OBJECT_UILIMIT_READCLIPBOARD;
import static com.github.zhanhb.jnc.platform.win32.WinNT.JOB_OBJECT_UILIMIT_SYSTEMPARAMETERS;
import static com.github.zhanhb.jnc.platform.win32.WinNT.JOB_OBJECT_UILIMIT_WRITECLIPBOARD;

public class Job implements Closeable {

    private final long /*HANDLE*/ hJob;

    public Job() {
        long handle = Kernel32.INSTANCE.CreateJobObjectW(null, null);
        Kernel32Util.assertTrue(handle != 0);
        this.hJob = handle;
    }

    private void setInformationJobObject(JOBOBJECTINFOCLASS jobobjectinfoclass, JOBOBJECT_INFORMATION jobj) {
        Kernel32Util.assertTrue(Kernel32.INSTANCE.SetInformationJobObject(hJob, jobobjectinfoclass.value(), jobj, jobj.size()));
    }

    public void init() {
        JOBOBJECT_BASIC_LIMIT_INFORMATION jobli = new JOBOBJECT_BASIC_LIMIT_INFORMATION();
        jobli.setActiveProcessLimit(1);
        // These are the only 1 restrictions I want placed on the job (process).
        jobli.setLimitFlags(JOB_OBJECT_LIMIT_ACTIVE_PROCESS);
        setInformationJobObject(JobObjectBasicLimitInformation, jobli);

        // Second, set some UI restrictions.
        JOBOBJECT_BASIC_UI_RESTRICTIONS jbur = new JOBOBJECT_BASIC_UI_RESTRICTIONS();
        jbur.setUIRestrictionsClass(
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
        Kernel32Util.assertTrue(Kernel32.INSTANCE.AssignProcessToJobObject(hJob, hProcess));
    }

    @Override
    public void close() {
        Handle.close(hJob);
    }

}
