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
package com.github.zhanhb.judge.win32.struct;

/**
 *
 * @author zhanhb
 */
public interface AccCtrl {

    int NO_INHERITANCE = 0x0;
    int SUB_OBJECTS_ONLY_INHERIT = 0x1;
    int SUB_CONTAINERS_ONLY_INHERIT = 0x2;
    int SUB_CONTAINERS_AND_OBJECTS_INHERIT = 0x3;
    int INHERIT_NO_PROPAGATE = 0x4;
    int INHERIT_ONLY = 0x8;
    int INHERITED_ACCESS_ENTRY = 0x10;

}
