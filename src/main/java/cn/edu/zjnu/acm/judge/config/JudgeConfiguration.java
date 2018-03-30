/*
 * Copyright 2014 zhanhb.
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
package cn.edu.zjnu.acm.judge.config;

import cn.edu.zjnu.acm.judge.data.form.SystemInfoForm;
import cn.edu.zjnu.acm.judge.service.SystemService;
import cn.edu.zjnu.acm.judge.util.SpecialCall;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SpecialCall("fragment/notice")
public class JudgeConfiguration {

    public static final String VALIDATE_FILE_NAME = "compare.exe";

    @Autowired
    private SystemService systemService;
    private volatile SystemInfoForm systemInfo;

    public Path getUploadDirectory() {
        return Paths.get(systemService.getUploadPath());
    }

    public boolean isDeleteTempFile() {
        return Boolean.parseBoolean(systemService.getDeleteTempFile());
    }

    private Path getDataFilesPath() {
        return Paths.get(systemService.getDataFilesPath());
    }

    public Path getDataDirectory(long problemId) {
        return getDataFilesPath().resolve(String.valueOf(problemId));
    }

    private Path getWorkingPath() {
        return Paths.get(systemService.getWorkingPath());
    }

    public Path getWorkDirectory(long submissionId) {
        return getWorkingPath().resolve(String.valueOf(submissionId));
    }

    public String getIndex() {
        return systemService.getIndex();
    }

    public void setIndex(String index) {
        systemService.setIndex(index);
    }

    @SpecialCall("fragment/notice")
    public SystemInfoForm getSystemInfo() {
        return systemInfo;
    }

    public void setSystemInfo(SystemInfoForm systemInfoForm) {
        systemInfo = systemInfoForm;
    }

}
