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
public interface AccCtrl {

    int NO_INHERITANCE = 0;
    int SUB_OBJECTS_ONLY_INHERIT = 1;
    int SUB_CONTAINERS_ONLY_INHERIT = 1 << 1;
    int SUB_CONTAINERS_AND_OBJECTS_INHERIT = (SUB_OBJECTS_ONLY_INHERIT | SUB_CONTAINERS_ONLY_INHERIT);
    int INHERIT_NO_PROPAGATE = 1 << 2;
    int INHERIT_ONLY = 1 << 3;
    int INHERITED_ACCESS_ENTRY = 1 << 4;

}
