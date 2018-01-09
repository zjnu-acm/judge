/*
 * Copyright 2018 ZJNU ACM.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.zhanhb.judge.win32;

/**
 * <table><caption><b>The Token level specifies a set of security profiles
 * designed to provide the bulk of the security of
 * sandbox.</b></caption><tr><td>TokenLevel</td><td>Restricting
 * Sids</td><td>Deny
 * Only</td><td>Privileges</td></tr><tr><td>USER_LOCKDOWN</td><td>Null
 * Sid</td><td>All</td><td>None</td></tr>
 * <tr><td>USER_RESTRICTED</td><td>RESTRICTED</td><td>All</td><td>Traverse</td></tr>
 * <tr><td>USER_LIMITED</td><td>Users, Everyone, RESTRICTED</td><td>All except:
 * Users, Everyone, Interactive</td><td>Traverse</td></tr>
 * <tr><td>USER_INTERACTIVE</td><td>Users, Everyone, RESTRICTED,
 * Owner</td><td>All except: Users, Everyone, Interactive, Local, Authent-users,
 * User</td><td>Traverse</td></tr>
 * <tr><td>USER_NON_ADMIN</td><td>None</td><td>All except: Users, Everyone,
 * Interactive, Local, Authent-users, User</td><td>Traverse</td></tr>
 * <tr><td>USER_RESTRICTED_SAME_ACCESS</td><td>All</td><td>None</td><td>All</td></tr>
 * <tr><td>USER_UNPROTECTED</td><td>None</td><td>None</td><td>All</td></tr>
 * </table>
 * <p>
 * The above restrictions are actually a transformation that is applied to the
 * existing broker process token. The resulting token that will be applied to
 * the target process depends both on the token level selected and on the broker
 * token itself.</p>
 * <p>
 * The LOCKDOWN and RESTRICTED are designed to allow access to almost nothing
 * that has security associated with and they are the recommended levels to run
 * sandboxed code specially if there is a chance that the broker is process
 * might be started by a user that belongs to the Admins or power users
 * groups.</p>
 */
public enum TokenLevel {
    USER_LOCKDOWN,
    USER_RESTRICTED,
    USER_LIMITED,
    USER_INTERACTIVE,
    USER_NON_ADMIN,
    USER_RESTRICTED_SAME_ACCESS,
    USER_UNPROTECTED,
    USER_LAST
};
