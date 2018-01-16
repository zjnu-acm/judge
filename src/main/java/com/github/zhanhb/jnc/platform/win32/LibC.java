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

import jnc.foreign.LibraryLoader;
import jnc.foreign.Pointer;
import jnc.foreign.typedef.size_t;

/**
 *
 * @author zhanhb
 */
public interface LibC {

    LibC INSTANCE = LibraryLoader.create(LibC.class).load(jnc.foreign.Platform.getNativePlatform().getLibcName());

    @size_t
    int wcslen(Pointer pointer);

}
