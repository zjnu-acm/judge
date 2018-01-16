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
 * @see
 * <a href="https://msdn.microsoft.com/en-us/library/windows/desktop/ms686216(v=vs.85).aspx">SetInformationJobObject</a>
 */
public enum JOBOBJECTINFOCLASS {

    JobObjectBasicAccountingInformation,
    /**
     * @see JOBOBJECT_BASIC_LIMIT_INFORMATION
     */
    JobObjectBasicLimitInformation,
    JobObjectBasicProcessIdList,
    /**
     * @see JOBOBJECT_BASIC_UI_RESTRICTIONS
     */
    JobObjectBasicUIRestrictions,
    JobObjectSecurityLimitInformation,
    JobObjectEndOfJobTimeInformation,
    JobObjectAssociateCompletionPortInformation,
    JobObjectBasicAndIoAccountingInformation,
    JobObjectExtendedLimitInformation,
    JobObjectJobSetInformation,
    JobObjectGroupInformation,
    JobObjectNotificationLimitInformation,
    JobObjectLimitViolationInformation,
    JobObjectGroupInformationEx,
    JobObjectCpuRateControlInformation;

    public int value() {
        return ordinal() + 1;
    }

}
