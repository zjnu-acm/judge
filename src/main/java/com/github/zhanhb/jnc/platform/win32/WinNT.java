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
package com.github.zhanhb.jnc.platform.win32;

/**
 *
 * @author zhanhb
 */
public interface WinNT {

    int ANYSIZE_ARRAY = 1;

    int READ_CONTROL = 1 << 17;
    int STANDARD_RIGHTS_REQUIRED = 15 << 16;
    int STANDARD_RIGHTS_READ = READ_CONTROL;
    int STANDARD_RIGHTS_WRITE = READ_CONTROL;
    int STANDARD_RIGHTS_EXECUTE = READ_CONTROL;

    int GENERIC_READ = 1 << 31;
    int GENERIC_WRITE = 1 << 30;
    int GENERIC_EXECUTE = 1 << 29;
    int GENERIC_ALL = 1 << 28;

    int SID_REVISION = 1;
    int SID_MAX_SUB_AUTHORITIES = 15;
    int SID_RECOMMENDED_SUB_AUTHORITIES = 1;
    int SECURITY_MAX_SID_SIZE = 68;
    int SID_HASH_SIZE = 32;

    int SE_GROUP_MANDATORY = 1;
    int SE_GROUP_ENABLED_BY_DEFAULT = 1 << 1;
    int SE_GROUP_ENABLED = 1 << 2;
    int SE_GROUP_OWNER = 1 << 3;
    int SE_GROUP_USE_FOR_DENY_ONLY = 1 << 4;
    int SE_GROUP_INTEGRITY = 1 << 5;
    int SE_GROUP_INTEGRITY_ENABLED = 1 << 6;
    int SE_GROUP_LOGON_ID = 1 << 30 | 1 << 31;
    int SE_GROUP_RESOURCE = 1 << 29;
    int SE_GROUP_VALID_ATTRIBUTES = SE_GROUP_MANDATORY | SE_GROUP_ENABLED_BY_DEFAULT | SE_GROUP_ENABLED | SE_GROUP_OWNER | SE_GROUP_USE_FOR_DENY_ONLY | SE_GROUP_LOGON_ID | SE_GROUP_RESOURCE | SE_GROUP_INTEGRITY | SE_GROUP_INTEGRITY_ENABLED;

    String SE_CREATE_TOKEN_NAME = "SeCreateTokenPrivilege";
    String SE_ASSIGNPRIMARYTOKEN_NAME = "SeAssignPrimaryTokenPrivilege";
    String SE_LOCK_MEMORY_NAME = "SeLockMemoryPrivilege";
    String SE_INCREASE_QUOTA_NAME = "SeIncreaseQuotaPrivilege";
    String SE_UNSOLICITED_INPUT_NAME = "SeUnsolicitedInputPrivilege";
    String SE_MACHINE_ACCOUNT_NAME = "SeMachineAccountPrivilege";
    String SE_TCB_NAME = "SeTcbPrivilege";
    String SE_SECURITY_NAME = "SeSecurityPrivilege";
    String SE_TAKE_OWNERSHIP_NAME = "SeTakeOwnershipPrivilege";
    String SE_LOAD_DRIVER_NAME = "SeLoadDriverPrivilege";
    String SE_SYSTEM_PROFILE_NAME = "SeSystemProfilePrivilege";
    String SE_SYSTEMTIME_NAME = "SeSystemtimePrivilege";
    String SE_PROF_SINGLE_PROCESS_NAME = "SeProfileSingleProcessPrivilege";
    String SE_INC_BASE_PRIORITY_NAME = "SeIncreaseBasePriorityPrivilege";
    String SE_CREATE_PAGEFILE_NAME = "SeCreatePagefilePrivilege";
    String SE_CREATE_PERMANENT_NAME = "SeCreatePermanentPrivilege";
    String SE_BACKUP_NAME = "SeBackupPrivilege";
    String SE_RESTORE_NAME = "SeRestorePrivilege";
    String SE_SHUTDOWN_NAME = "SeShutdownPrivilege";
    String SE_DEBUG_NAME = "SeDebugPrivilege";
    String SE_AUDIT_NAME = "SeAuditPrivilege";
    String SE_SYSTEM_ENVIRONMENT_NAME = "SeSystemEnvironmentPrivilege";
    String SE_CHANGE_NOTIFY_NAME = "SeChangeNotifyPrivilege";
    String SE_REMOTE_SHUTDOWN_NAME = "SeRemoteShutdownPrivilege";
    String SE_UNDOCK_NAME = "SeUndockPrivilege";
    String SE_SYNC_AGENT_NAME = "SeSyncAgentPrivilege";
    String SE_ENABLE_DELEGATION_NAME = "SeEnableDelegationPrivilege";
    String SE_MANAGE_VOLUME_NAME = "SeManageVolumePrivilege";
    String SE_IMPERSONATE_NAME = "SeImpersonatePrivilege";
    String SE_CREATE_GLOBAL_NAME = "SeCreateGlobalPrivilege";
    String SE_TRUSTED_CREDMAN_ACCESS_NAME = "SeTrustedCredManAccessPrivilege";
    String SE_RELABEL_NAME = "SeRelabelPrivilege";
    String SE_INC_WORKING_SET_NAME = "SeIncreaseWorkingSetPrivilege";
    String SE_TIME_ZONE_NAME = "SeTimeZonePrivilege";
    String SE_CREATE_SYMBOLIC_LINK_NAME = "SeCreateSymbolicLinkPrivilege";

    /**
     * Required to attach a primary token to a process. The
     * SE_ASSIGNPRIMARYTOKEN_NAME privilege is also required to accomplish this
     * task.
     */
    int TOKEN_ASSIGN_PRIMARY = 1;
    /**
     * Required to duplicate an access token.
     */
    int TOKEN_DUPLICATE = 1 << 1;
    /**
     * Required to attach an impersonation access token to a process.
     */
    int TOKEN_IMPERSONATE = 1 << 2;
    /**
     * Required to query an access token.
     */
    int TOKEN_QUERY = 1 << 3;
    /**
     * Required to query the source of an access token.
     */
    int TOKEN_QUERY_SOURCE = 1 << 4;
    /**
     * Required to enable or disable the privileges in an access token.
     */
    int TOKEN_ADJUST_PRIVILEGES = 1 << 5;
    /**
     * Required to adjust the attributes of the groups in an access token.
     */
    int TOKEN_ADJUST_GROUPS = 1 << 6;
    /**
     * Required to change the default owner, primary group, or DACL of an access
     * token.
     */
    int TOKEN_ADJUST_DEFAULT = 1 << 7;
    /**
     * Required to adjust the session ID of an access token. The SE_TCB_NAME
     * privilege is required.
     */
    int TOKEN_ADJUST_SESSIONID = 1 << 8;

    int DISABLE_MAX_PRIVILEGE = 1;
    int SANDBOX_INERT = 1 << 1;
    int LUA_TOKEN = 1 << 2;
    int WRITE_RESTRICTED = 1 << 3;

    int TOKEN_ALL_ACCESS_P = (STANDARD_RIGHTS_REQUIRED | TOKEN_ASSIGN_PRIMARY | TOKEN_DUPLICATE | TOKEN_IMPERSONATE | TOKEN_QUERY | TOKEN_QUERY_SOURCE | TOKEN_ADJUST_PRIVILEGES | TOKEN_ADJUST_GROUPS | TOKEN_ADJUST_DEFAULT);
    int TOKEN_ALL_ACCESS = (TOKEN_ALL_ACCESS_P | TOKEN_ADJUST_SESSIONID);
    int TOKEN_READ = (STANDARD_RIGHTS_READ | TOKEN_QUERY);
    int TOKEN_EXECUTE = STANDARD_RIGHTS_EXECUTE;

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

    int JOB_OBJECT_LIMIT_WORKINGSET = 1;
    int JOB_OBJECT_LIMIT_PROCESS_TIME = 1 << 1;
    int JOB_OBJECT_LIMIT_JOB_TIME = 1 << 2;
    int JOB_OBJECT_LIMIT_ACTIVE_PROCESS = 1 << 3;
    int JOB_OBJECT_LIMIT_AFFINITY = 1 << 4;
    int JOB_OBJECT_LIMIT_PRIORITY_CLASS = 1 << 5;
    int JOB_OBJECT_LIMIT_PRESERVE_JOB_TIME = 1 << 6;
    int JOB_OBJECT_LIMIT_SCHEDULING_CLASS = 1 << 7;
    int JOB_OBJECT_LIMIT_PROCESS_MEMORY = 1 << 8;
    int JOB_OBJECT_LIMIT_JOB_MEMORY = 1 << 9;
    int JOB_OBJECT_LIMIT_DIE_ON_UNHANDLED_EXCEPTION = 1 << 10;
    int JOB_OBJECT_LIMIT_BREAKAWAY_OK = 1 << 11;
    int JOB_OBJECT_LIMIT_SILENT_BREAKAWAY_OK = 1 << 12;
    int JOB_OBJECT_LIMIT_KILL_ON_JOB_CLOSE = 1 << 13;
    int JOB_OBJECT_LIMIT_SUBSET_AFFINITY = 1 << 14;

    /* JOBOBJECT_BASIC_UI_RESTRICTIONS.UIRestrictionsClass constants */
    int JOB_OBJECT_UILIMIT_HANDLES = 1;
    int JOB_OBJECT_UILIMIT_READCLIPBOARD = 1 << 1;
    int JOB_OBJECT_UILIMIT_WRITECLIPBOARD = 1 << 2;
    int JOB_OBJECT_UILIMIT_SYSTEMPARAMETERS = 1 << 3;
    int JOB_OBJECT_UILIMIT_DISPLAYSETTINGS = 1 << 4;
    int JOB_OBJECT_UILIMIT_GLOBALATOMS = 1 << 5;
    int JOB_OBJECT_UILIMIT_DESKTOP = 1 << 6;
    int JOB_OBJECT_UILIMIT_EXITWINDOWS = 1 << 7;

    /* JOBOBJECT_SECURITY_LIMIT_INFORMATION.SecurityLimitFlags constants */
    int JOB_OBJECT_SECURITY_NO_ADMIN = 1;
    int JOB_OBJECT_SECURITY_RESTRICTED_TOKEN = 1 << 1;
    int JOB_OBJECT_SECURITY_ONLY_TOKEN = 1 << 2;
    int JOB_OBJECT_SECURITY_FILTER_TOKENS = 1 << 3;

    int FILE_SHARE_READ = 1;
    int FILE_SHARE_WRITE = 1 << 1;
    int FILE_SHARE_DELETE = 1 << 2;
    int FILE_SHARE_VALID_FLAGS = (FILE_SHARE_READ | FILE_SHARE_WRITE | FILE_SHARE_DELETE);

    int FILE_ATTRIBUTE_READONLY = 1;
    int FILE_ATTRIBUTE_HIDDEN = 1 << 1;
    int FILE_ATTRIBUTE_SYSTEM = 1 << 2;
    int FILE_ATTRIBUTE_DIRECTORY = 1 << 4;
    int FILE_ATTRIBUTE_ARCHIVE = 1 << 5;
    int FILE_ATTRIBUTE_DEVICE = 1 << 6;
    int FILE_ATTRIBUTE_NORMAL = 1 << 7;
    int FILE_ATTRIBUTE_TEMPORARY = 1 << 8;
    int FILE_ATTRIBUTE_SPARSE_FILE = 1 << 9;
    int FILE_ATTRIBUTE_REPARSE_POINT = 1 << 10;
    int FILE_ATTRIBUTE_COMPRESSED = 1 << 11;
    int FILE_ATTRIBUTE_OFFLINE = 1 << 12;
    int FILE_ATTRIBUTE_NOT_CONTENT_INDEXED = 1 << 13;
    int FILE_ATTRIBUTE_ENCRYPTED = 1 << 14;
    int FILE_ATTRIBUTE_VIRTUAL = 1 << 16;

    int DUPLICATE_CLOSE_SOURCE = 1;
    int DUPLICATE_SAME_ACCESS = 2;

}
