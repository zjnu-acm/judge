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
package jnc.platform.win32;

/**
 * @see
 * <a href="https://msdn.microsoft.com/en-us/library/windows/desktop/ms686216(v=vs.85).aspx">SetInformationJobObject</a>
 * @author zhanhb
 */
public enum JOBOBJECTINFOCLASS {

    /**
     * @see JOBOBJECT_BASIC_ACCOUNTING_INFORMATION
     * @see Kernel32#QueryInformationJobObject
     */
    JobObjectBasicAccountingInformation,
    /**
     * @see JOBOBJECT_BASIC_LIMIT_INFORMATION
     * @see Kernel32#QueryInformationJobObject
     * @see Kernel32#SetInformationJobObject
     */
    JobObjectBasicLimitInformation,
    /**
     * @see JOBOBJECT_BASIC_PROCESS_ID_LIST
     * @see Kernel32#QueryInformationJobObject
     */
    JobObjectBasicProcessIdList,
    /**
     * @see JOBOBJECT_BASIC_UI_RESTRICTIONS
     * @see Kernel32#QueryInformationJobObject
     * @see Kernel32#SetInformationJobObject
     */
    JobObjectBasicUIRestrictions,
    /**
     * @deprecated Only support on windows XP
     * @see JOBOBJECT_SECURITY_LIMIT_INFORMATION
     * @see Kernel32#QueryInformationJobObject
     * @see Kernel32#SetInformationJobObject
     */
    JobObjectSecurityLimitInformation,
    /**
     * @see Kernel32#QueryInformationJobObject
     * @see Kernel32#SetInformationJobObject
     */
    JobObjectEndOfJobTimeInformation,
    /**
     * @see Kernel32#SetInformationJobObject
     */
    JobObjectAssociateCompletionPortInformation,
    /**
     * @see JOBOBJECT_BASIC_AND_IO_ACCOUNTING_INFORMATION
     * @see Kernel32#QueryInformationJobObject
     */
    JobObjectBasicAndIoAccountingInformation,
    /**
     * @see JOBOBJECT_EXTENDED_LIMIT_INFORMATION
     * @see Kernel32#QueryInformationJobObject
     * @see Kernel32#SetInformationJobObject
     */
    JobObjectExtendedLimitInformation,
    JobObjectJobSetInformation,
    /**
     * @see Kernel32#QueryInformationJobObject
     * @see Kernel32#SetInformationJobObject
     */
    JobObjectGroupInformation,
    /**
     * @see Kernel32#QueryInformationJobObject
     * @see Kernel32#SetInformationJobObject
     */
    JobObjectNotificationLimitInformation,
    /**
     * @see JOBOBJECT_LIMIT_VIOLATION_INFORMATION
     * @see Kernel32#QueryInformationJobObject
     */
    JobObjectLimitViolationInformation,
    JobObjectGroupInformationEx,
    JobObjectCpuRateControlInformation,
    JobObjectCompletionFilter,
    JobObjectCompletionCounter;

    public int value() {
        return ordinal() + 1;
    }

}
