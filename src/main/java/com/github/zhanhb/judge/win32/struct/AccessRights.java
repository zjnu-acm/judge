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
package com.github.zhanhb.judge.win32.struct;

/**
 *
 * @author zhanhb
 */
public interface AccessRights {

    /**
     * Required to attach a primary token to a process. The
     * SE_ASSIGNPRIMARYTOKEN_NAME privilege is also required to accomplish this
     * task.
     */
    int TOKEN_ASSIGN_PRIMARY = 1;
    /**
     * Required to duplicate an access token.
     */
    int TOKEN_DUPLICATE = 2;
    /**
     * Required to attach an impersonation access token to a process.
     */
    int TOKEN_IMPERSONATE = 4;
    /**
     * Required to query an access token.
     */
    int TOKEN_QUERY = 8;
    /**
     * Required to query the source of an access token.
     */
    int TOKEN_QUERY_SOURCE = 16;
    /**
     * Required to enable or disable the privileges in an access token.
     */
    int TOKEN_ADJUST_PRIVILEGES = 32;
    /**
     * Required to adjust the attributes of the groups in an access token.
     */
    int TOKEN_ADJUST_GROUPS = 64;
    /**
     * Required to change the default owner, primary group, or DACL of an access
     * token.
     */
    int TOKEN_ADJUST_DEFAULT = 128;
    /**
     * Required to adjust the session ID of an access token. The SE_TCB_NAME
     * privilege is required.
     */
    int TOKEN_ADJUST_SESSIONID = 256;

}
