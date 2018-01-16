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
 * @see JOBOBJECTINFOCLASS#JobObjectBasicUIRestrictions
 * @see
 * <a href="https://msdn.microsoft.com/en-us/library/windows/desktop/ms684152(v=vs.85).aspx">JOBOBJECT_BASIC_UI_RESTRICTIONS</a>
 */
public class JOBOBJECT_BASIC_UI_RESTRICTIONS extends JOBOBJECT_INFORMATION {

    private final DWORD UIRestrictionsClass = new DWORD();

    public void setUIRestrictionsClass(int uiRestrictionsClass) {
        this.UIRestrictionsClass.set(uiRestrictionsClass);
    }

}
