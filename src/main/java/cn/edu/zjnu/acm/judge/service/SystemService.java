/*
 * Copyright 2019 ZJNU ACM.
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
package cn.edu.zjnu.acm.judge.service;

import cn.edu.zjnu.acm.judge.util.SpecialCall;
import javax.annotation.Nullable;

/**
 *
 * @author zhanhb
 */
@SpecialCall({"index", "manager", "layout/main", "fragment/ga"})
public interface SystemService {

    @Nullable
    @SpecialCall({"index", "manager", "layout/main"})
    String getAdminMail();

    @Nullable
    String getDataFilesPath();

    @Nullable
    String getDeleteTempFile();

    @Nullable
    @SpecialCall("fragment/ga")
    String getGa();

    @Nullable
    String getIndex();

    @Nullable
    String getResetPasswordTitle();

    @Nullable
    String getUploadPath();

    @Nullable
    String getWorkingPath();

    void setIndex(@Nullable String index);

}
