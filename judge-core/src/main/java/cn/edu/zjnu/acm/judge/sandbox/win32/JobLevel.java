package cn.edu.zjnu.acm.judge.sandbox.win32;

/**
 * <table>
 * <caption>The Job level specifies a set of decreasing security profiles for
 * the Job object that the target process will be placed into.</caption>
 * <tr>
 * <th>JobLevel</th>
 * <th>General restrictions</th>
 * <th>Quota restrictions</th>
 * </tr>
 * <tr>
 * <td>JOB_NONE</td>
 * <td>No job is assigned to the sandboxed process.</td>
 * <td>None</td>
 * </tr>
 * <tr>
 * <td>JOB_UNPROTECTED</td>
 * <td>None</td>
 * <td>*Kill on Job close.</td>
 * </tr>
 * <tr>
 * <td>JOB_INTERACTIVE</td>
 * <td>*Forbid system-wide changes using SystemParametersInfo().<br>*Forbid the
 * creation/switch of Desktops.<br>*Forbids calls to ExitWindows().</td>
 * <td>*Kill on Job close.</td>
 * </tr>
 * <tr>
 * <td>JOB_LIMITED_USER</td>
 * <td>Same as INTERACTIVE_USER plus:<br>*Forbid changes to the display
 * settings.</td>
 * <td>*One active process limit.<br>*Kill on Job close.</td>
 * </tr>
 * <tr>
 * <td>JOB_RESTRICTED</td>
 * <td>Same as INTERACTIVE_USER plus:<br>* No read/write to the clipboard.<br>*
 * No access to User Handles that belong to other processes.<br>* Forbid message
 * broadcasts.<br>* Forbid setting global hooks.<br>* No access to the global
 * atoms table.</td>
 * <td>*One active process limit.<br>*Kill on Job close.</td>
 * </tr>
 * <tr>
 * <td>JOB_LOCKDOWN</td>
 * <td>Same as RESTRICTED</td>
 * <td>*One active process limit.<br>*Kill on Job close.<br>*Kill on unhandled
 * exception.</td>
 * </tr>
 * </table>
 */
public enum JobLevel {
    JOB_LOCKDOWN,
    JOB_RESTRICTED,
    JOB_LIMITED_USER,
    JOB_INTERACTIVE,
    JOB_UNPROTECTED,
    JOB_NONE
}
