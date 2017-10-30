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
package com.github.zhanhb.judge.win32;

/**
 *
 * @author zhanhb
 */
public interface WinBase {

    // STARTUPINFO flags
    int STARTF_USESHOWWINDOW = 0x001;
    int STARTF_USESIZE = 0x002;
    int STARTF_USEPOSITION = 0x004;
    int STARTF_USECOUNTCHARS = 0x008;
    int STARTF_USEFILLATTRIBUTE = 0x010;
    int STARTF_RUNFULLSCREEN = 0x020;
    int STARTF_FORCEONFEEDBACK = 0x040;
    int STARTF_FORCEOFFFEEDBACK = 0x080;
    int STARTF_USESTDHANDLES = 0x100;

    // Process Creation flags
    int DEBUG_PROCESS = 0x00000001;
    int DEBUG_ONLY_THIS_PROCESS = 0x00000002;
    int CREATE_SUSPENDED = 0x00000004;
    int DETACHED_PROCESS = 0x00000008;
    int CREATE_NEW_CONSOLE = 0x00000010;
    int CREATE_NEW_PROCESS_GROUP = 0x00000200;
    int CREATE_UNICODE_ENVIRONMENT = 0x00000400;
    int CREATE_SEPARATE_WOW_VDM = 0x00000800;
    int CREATE_SHARED_WOW_VDM = 0x00001000;
    int CREATE_FORCEDOS = 0x00002000;
    int INHERIT_PARENT_AFFINITY = 0x00010000;
    int CREATE_PROTECTED_PROCESS = 0x00040000;
    int EXTENDED_STARTUPINFO_PRESENT = 0x00080000;
    int CREATE_BREAKAWAY_FROM_JOB = 0x01000000;
    int CREATE_PRESERVE_CODE_AUTHZ_LEVEL = 0x02000000;
    int CREATE_DEFAULT_ERROR_MODE = 0x04000000;
    int CREATE_NO_WINDOW = 0x08000000;

    /**
     * The lpBuffer parameter is a pointer to a PVOID pointer, and that the
     * nSize parameter specifies the minimum number of TCHARs to allocate for an
     * output message buffer. The function allocates a buffer large enough to
     * hold the formatted message, and places a pointer to the allocated buffer
     * at the address specified by lpBuffer. The caller should use the LocalFree
     * function to free the buffer when it is no longer needed.
     */
    int FORMAT_MESSAGE_ALLOCATE_BUFFER = 0x00000100;
    /**
     * Insert sequences in the message definition are to be ignored and passed
     * through to the output buffer unchanged. This flag is useful for fetching
     * a message for later formatting. If this flag is set, the Arguments
     * parameter is ignored.
     */
    int FORMAT_MESSAGE_IGNORE_INSERTS = 0x00000200;
    /**
     * The lpSource parameter is a pointer to a null-terminated message
     * definition. The message definition may contain insert sequences, just as
     * the message text in a message table resource may. Cannot be used with
     * FORMAT_MESSAGE_FROM_HMODULE or FORMAT_MESSAGE_FROM_SYSTEM.
     */
    int FORMAT_MESSAGE_FROM_STRING = 0x00000400;
    /**
     * The lpSource parameter is a module handle containing the message-table
     * resource(s) to search. If this lpSource handle is NULL, the current
     * process's application image file will be searched. Cannot be used with
     * FORMAT_MESSAGE_FROM_STRING.
     */
    int FORMAT_MESSAGE_FROM_HMODULE = 0x00000800;
    /**
     * The function should search the system message-table resource(s) for the
     * requested message. If this flag is specified with
     * FORMAT_MESSAGE_FROM_HMODULE, the function searches the system message
     * table if the message is not found in the module specified by lpSource.
     * Cannot be used with FORMAT_MESSAGE_FROM_STRING. If this flag is
     * specified, an application can pass the result of the GetLastError
     * function to retrieve the message text for a system-defined error.
     */
    int FORMAT_MESSAGE_FROM_SYSTEM = 0x00001000;
    /**
     * The Arguments parameter is not a va_list structure, but is a pointer to
     * an array of values that represent the arguments. This flag cannot be used
     * with 64-bit argument values. If you are using 64-bit values, you must use
     * the va_list structure.
     */
    int FORMAT_MESSAGE_ARGUMENT_ARRAY = 0x00002000;

    int WAIT_FAILED = 0xFFFFFFFF;
    int WAIT_OBJECT_0 = 0x0000000;
    int WAIT_ABANDONED = 0x00000080;
    int WAIT_ABANDONED_0 = 0x00000080;

    int WAIT_TIMEOUT = 258; // dderror

}
