/*
 * Copyright 2017-2019 ZJNU ACM.
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
package cn.edu.zjnu.acm.judge.service.impl;

import cn.edu.zjnu.acm.judge.config.Constants;
import cn.edu.zjnu.acm.judge.data.form.SystemInfoForm;
import cn.edu.zjnu.acm.judge.mapper.SystemMapper;
import cn.edu.zjnu.acm.judge.service.SystemService;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.stereotype.Service;

/**
 *
 * @author zhanhb
 */
@RequiredArgsConstructor
@Service("systemService")
public class SystemServiceImpl implements SystemService {

    private static final String VALIDATE_FILE_NAME = "compare.exe";

    private final SystemMapper systemMapper;
    private volatile SystemInfoForm systemInfo;

    @Autowired // ensure flyway initialize before this service
    public void setFlywayMigrationInitializer(FlywayMigrationInitializer flywayMigrationInitializer) {
    }

    @Nullable
    @Override
    public String getAdminMail() {
        return systemMapper.getValueByName(Constants.SystemKey.ADMIN_MAIL);
    }

    @Nullable
    @Override
    public String getGa() {
        return systemMapper.getValueByName(Constants.SystemKey.GA);
    }

    @Nullable
    @Override
    public String getResetPasswordTitle() {
        return systemMapper.getValueByName(Constants.SystemKey.RESETPASSWORD_TITLE);
    }

    @Nullable
    @Override
    public String getIndex() {
        return systemMapper.getValueByName(Constants.SystemKey.PAGE_INDEX);
    }

    @Override
    public void setIndex(@Nullable String index) {
        systemMapper.updateValueByName(Constants.SystemKey.PAGE_INDEX, index);
    }

    @Override
    public Path getUploadDirectory() {
        String path = systemMapper.getValueByName(Constants.SystemKey.UPLOAD_PATH);
        return Paths.get(Objects.requireNonNull(path, "upload directory absent"));
    }

    @Override
    public boolean isDeleteTempFile() {
        return Boolean.parseBoolean(systemMapper.getValueByName(Constants.SystemKey.DELETE_TEMP_FILE));
    }

    @Override
    public Path getDataDirectory(long problemId) {
        String path = systemMapper.getValueByName(Constants.SystemKey.DATA_FILES_PATH);
        return Paths.get(Objects.requireNonNull(path, "data directory absent"), String.valueOf(problemId));
    }

    @Override
    public Path getWorkDirectory(long submissionId) {
        String path = systemMapper.getValueByName(Constants.SystemKey.WORKING_PATH);
        return Paths.get(Objects.requireNonNull(path, "working path absent"), String.valueOf(submissionId));
    }

    @Override
    public SystemInfoForm getSystemInfo() {
        return systemInfo;
    }

    @Override
    public void setSystemInfo(SystemInfoForm systemInfoForm) {
        systemInfo = systemInfoForm;
    }

    @Override
    public Path getSpecialJudgeExecutable(long problemId) {
        return getDataDirectory(problemId).resolve(VALIDATE_FILE_NAME);
    }

    @Override
    public boolean isSpecialJudge(long problemId) {
        return Files.isExecutable(getSpecialJudgeExecutable(problemId));
    }

}
