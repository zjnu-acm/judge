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
 * @see JOBOBJECTINFOCLASS#JobObjectBasicUIRestrictions
 * @see
 * <a href="https://msdn.microsoft.com/en-us/library/windows/desktop/ms684152">JOBOBJECT_BASIC_UI_RESTRICTIONS</a>
 */
@SuppressWarnings("unused")
public final class JOBOBJECT_BASIC_UI_RESTRICTIONS extends JobObjectInformation {

    private final DWORD UIRestrictionsClass = new DWORD();

    public final int getUiRestrictionsClass() {
        return this.UIRestrictionsClass.intValue();
    }

    public final void setUiRestrictionsClass(int value) {
        this.UIRestrictionsClass.set(value);
    }
}
