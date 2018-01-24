/*
 * Copyright 2017 ZJNU ACM.
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
public interface WinBase {

    int WAIT_FAILED = -1;
    int WAIT_OBJECT_0 = 0;
    int WAIT_ABANDONED = 1 << 7;
    int WAIT_ABANDONED_0 = 1 << 7;

    int FILE_FLAG_WRITE_THROUGH = 1 << 31;
    int FILE_FLAG_OVERLAPPED = 1 << 30;
    int FILE_FLAG_NO_BUFFERING = 1 << 29;
    int FILE_FLAG_RANDOM_ACCESS = 1 << 28;
    int FILE_FLAG_SEQUENTIAL_SCAN = 1 << 27;
    int FILE_FLAG_DELETE_ON_CLOSE = 1 << 26;
    int FILE_FLAG_BACKUP_SEMANTICS = 1 << 25;
    int FILE_FLAG_POSIX_SEMANTICS = 1 << 24;
    int FILE_FLAG_SESSION_AWARE = 1 << 23;
    int FILE_FLAG_OPEN_REPARSE_POINT = 1 << 21;
    int FILE_FLAG_OPEN_NO_RECALL = 1 << 20;
    int FILE_FLAG_FIRST_PIPE_INSTANCE = 1 << 19;
    int FILE_FLAG_OPEN_REQUIRING_OPLOCK = 1 << 18;

    int PIPE_ACCESS_INBOUND = 1;
    int PIPE_ACCESS_OUTBOUND = 2;
    int PIPE_ACCESS_DUPLEX = 3;

    // Process Creation flags
    int DEBUG_PROCESS = 1;
    int DEBUG_ONLY_THIS_PROCESS = 1 << 1;
    int CREATE_SUSPENDED = 1 << 2;
    int DETACHED_PROCESS = 1 << 3;
    int CREATE_NEW_CONSOLE = 1 << 4;
    int NORMAL_PRIORITY_CLASS = 1 << 5;
    int IDLE_PRIORITY_CLASS = 1 << 6;
    int HIGH_PRIORITY_CLASS = 1 << 7;
    int REALTIME_PRIORITY_CLASS = 1 << 8;
    int CREATE_NEW_PROCESS_GROUP = 1 << 9;
    int CREATE_UNICODE_ENVIRONMENT = 1 << 10;
    int CREATE_SEPARATE_WOW_VDM = 1 << 11;
    int CREATE_SHARED_WOW_VDM = 1 << 12;
    int CREATE_FORCEDOS = 1 << 13;
    int BELOW_NORMAL_PRIORITY_CLASS = 1 << 14;
    int ABOVE_NORMAL_PRIORITY_CLASS = 1 << 15;
    int INHERIT_PARENT_AFFINITY = 1 << 16;
    int INHERIT_CALLER_PRIORITY = 1 << 17;
    int CREATE_PROTECTED_PROCESS = 1 << 18;
    int EXTENDED_STARTUPINFO_PRESENT = 1 << 19;
    int PROCESS_MODE_BACKGROUND_BEGIN = 1 << 20;
    int PROCESS_MODE_BACKGROUND_END = 1 << 21;
    int CREATE_BREAKAWAY_FROM_JOB = 1 << 24;
    int CREATE_PRESERVE_CODE_AUTHZ_LEVEL = 1 << 25;
    int CREATE_DEFAULT_ERROR_MODE = 1 << 26;
    int CREATE_NO_WINDOW = 1 << 27;
    int PROFILE_USER = 1 << 28;
    int PROFILE_KERNEL = 1 << 29;
    int PROFILE_SERVER = 1 << 30;
    int CREATE_IGNORE_SYSTEM_DEFAULT = 1 << 31;

    int HANDLE_FLAG_INHERIT = 1;

    /**
     * The system does not display the critical-error-handler message box.
     * Instead, the system sends the error to the calling process.
     */
    int SEM_FAILCRITICALERRORS = 1;
    /**
     * The system does not display the Windows Error Reporting dialog.
     */
    int SEM_NOGPFAULTERRORBOX = 1 << 1;
    /**
     * The system automatically fixes memory alignment faults and makes them
     * invisible to the application. It does this for the calling process and
     * any descendant processes. This feature is only supported by certain
     * processor architectures. For more information, see the Remarks section.
     */
    int SEM_NOALIGNMENTFAULTEXCEPT = 1 << 2;
    /**
     * The system does not display a message box when it fails to find a file.
     * Instead, the error is returned to the calling process.
     */
    int SEM_NOOPENFILEERRORBOX = 1 << 15;

    /**
     * The lpBuffer parameter is a pointer to a PVOID pointer, and that the
     * nSize parameter specifies the minimum number of TCHARs to allocate for an
     * output message buffer. The function allocates a buffer large enough to
     * hold the formatted message, and places a pointer to the allocated buffer
     * at the address specified by lpBuffer. The caller should use the LocalFree
     * function to free the buffer when it is no longer needed.
     */
    int FORMAT_MESSAGE_ALLOCATE_BUFFER = 1 << 8;
    /**
     * Insert sequences in the message definition are to be ignored and passed
     * through to the output buffer unchanged. This flag is useful for fetching
     * a message for later formatting. If this flag is set, the Arguments
     * parameter is ignored.
     */
    int FORMAT_MESSAGE_IGNORE_INSERTS = 1 << 9;
    /**
     * The lpSource parameter is a pointer to a null-terminated message
     * definition. The message definition may contain insert sequences, just as
     * the message text in a message table resource may. Cannot be used with
     * FORMAT_MESSAGE_FROM_HMODULE or FORMAT_MESSAGE_FROM_SYSTEM.
     */
    int FORMAT_MESSAGE_FROM_STRING = 1 << 10;
    /**
     * The lpSource parameter is a module handle containing the message-table
     * resource(s) to search. If this lpSource handle is NULL, the current
     * process's application image file will be searched. Cannot be used with
     * FORMAT_MESSAGE_FROM_STRING.
     */
    int FORMAT_MESSAGE_FROM_HMODULE = 1 << 11;
    /**
     * The function should search the system message-table resource(s) for the
     * requested message. If this flag is specified with
     * FORMAT_MESSAGE_FROM_HMODULE, the function searches the system message
     * table if the message is not found in the module specified by lpSource.
     * Cannot be used with FORMAT_MESSAGE_FROM_STRING. If this flag is
     * specified, an application can pass the result of the GetLastError
     * function to retrieve the message text for a system-defined error.
     */
    int FORMAT_MESSAGE_FROM_SYSTEM = 1 << 12;
    /**
     * The Arguments parameter is not a va_list structure, but is a pointer to
     * an array of values that represent the arguments. This flag cannot be used
     * with 64-bit argument values. If you are using 64-bit values, you must use
     * the va_list structure.
     */
    int FORMAT_MESSAGE_ARGUMENT_ARRAY = 1 << 13;

    // STARTUPINFO flags
    int STARTF_USESHOWWINDOW = 1;
    int STARTF_USESIZE = 1 << 1;
    int STARTF_USEPOSITION = 1 << 2;
    int STARTF_USECOUNTCHARS = 1 << 3;
    int STARTF_USEFILLATTRIBUTE = 1 << 4;
    int STARTF_RUNFULLSCREEN = 1 << 5;
    int STARTF_FORCEONFEEDBACK = 1 << 6;
    int STARTF_FORCEOFFFEEDBACK = 1 << 7;
    int STARTF_USESTDHANDLES = 1 << 8;

}
