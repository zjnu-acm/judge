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
public interface SeGroup {

    int SE_GROUP_MANDATORY = 0x00000001;
    int SE_GROUP_ENABLED_BY_DEFAULT = 0x00000002;
    int SE_GROUP_ENABLED = 0x00000004;
    int SE_GROUP_OWNER = 0x00000008;
    int SE_GROUP_USE_FOR_DENY_ONLY = 0x00000010;
    int SE_GROUP_INTEGRITY = 0x00000020;
    int SE_GROUP_INTEGRITY_ENABLED = 0x00000040;
    int SE_GROUP_LOGON_ID = 0xC0000000;
    int SE_GROUP_RESOURCE = 0x20000000;

    int SE_GROUP_VALID_ATTRIBUTES = SE_GROUP_MANDATORY | SE_GROUP_ENABLED_BY_DEFAULT | SE_GROUP_ENABLED | SE_GROUP_OWNER | SE_GROUP_USE_FOR_DENY_ONLY | SE_GROUP_LOGON_ID | SE_GROUP_RESOURCE | SE_GROUP_INTEGRITY | SE_GROUP_INTEGRITY_ENABLED;

}
