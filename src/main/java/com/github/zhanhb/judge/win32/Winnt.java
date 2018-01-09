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
 *
 * @author zhanhb
 */
public interface Winnt {

    int GENERIC_READ = 0x80000000;
    int GENERIC_WRITE = 0x40000000;
    int GENERIC_EXECUTE = 0x20000000;
    int GENERIC_ALL = 0x10000000;
    int FILE_SHARE_READ = 0x00000001;
    int FILE_SHARE_WRITE = 0x00000002;
    int FILE_SHARE_DELETE = 0x00000004;

    int CREATE_NEW = 1;
    int CREATE_ALWAYS = 2;
    int OPEN_EXISTING = 3;
    int OPEN_ALWAYS = 4;
    int TRUNCATE_EXISTING = 5;

    int FILE_FLAG_WRITE_THROUGH = 0x80000000;
    int FILE_FLAG_OVERLAPPED = 0x40000000;
    int FILE_FLAG_NO_BUFFERING = 0x20000000;
    int FILE_FLAG_RANDOM_ACCESS = 0x10000000;
    int FILE_FLAG_SEQUENTIAL_SCAN = 0x08000000;
    int FILE_FLAG_DELETE_ON_CLOSE = 0x04000000;
    int FILE_FLAG_BACKUP_SEMANTICS = 0x02000000;
    int FILE_FLAG_POSIX_SEMANTICS = 0x01000000;
    int FILE_FLAG_OPEN_REPARSE_POINT = 0x00200000;
    int FILE_FLAG_OPEN_NO_RECALL = 0x00100000;

    int FILE_ATTRIBUTE_READONLY = 0x00000001;
    int FILE_ATTRIBUTE_HIDDEN = 0x00000002;
    int FILE_ATTRIBUTE_SYSTEM = 0x00000004;
    int FILE_ATTRIBUTE_DIRECTORY = 0x00000010;
    int FILE_ATTRIBUTE_ARCHIVE = 0x00000020;
    int FILE_ATTRIBUTE_DEVICE = 0x00000040;
    int FILE_ATTRIBUTE_NORMAL = 0x00000080;
    int FILE_ATTRIBUTE_TEMPORARY = 0x00000100;
    int FILE_ATTRIBUTE_SPARSE_FILE = 0x00000200;
    int FILE_ATTRIBUTE_REPARSE_POINT = 0x00000400;
    int FILE_ATTRIBUTE_COMPRESSED = 0x00000800;
    int FILE_ATTRIBUTE_OFFLINE = 0x00001000;
    int FILE_ATTRIBUTE_NOT_CONTENT_INDEXED = 0x00002000;
    int FILE_ATTRIBUTE_ENCRYPTED = 0x00004000;
    int FILE_ATTRIBUTE_VIRTUAL = 0x00010000;

    int SID_MAX_SUB_AUTHORITIES = 15;
    int SECURITY_MAX_SID_SIZE = 68;

    int ANYSIZE_ARRAY = 1;

    int DUPLICATE_CLOSE_SOURCE = 0x00000001;
    int DUPLICATE_SAME_ACCESS = 0x00000002;
    int TOKEN_ALL_ACCESS = 0x000f01ff;
    int TOKEN_EXECUTE = 0x00020000;
    int TOKEN_READ = 0x00020008;
    int TOKEN_IMPERSONATE = 0x00000004;

    int PIPE_ACCESS_INBOUND = 0x00000001;
    int PIPE_ACCESS_OUTBOUND = 0x00000002;
    int PIPE_ACCESS_DUPLEX = 0x00000003;

    String SE_CHANGE_NOTIFY_NAME = "SeChangeNotifyPrivilege";

}
