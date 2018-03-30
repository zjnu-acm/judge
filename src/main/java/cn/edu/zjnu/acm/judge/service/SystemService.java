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
package cn.edu.zjnu.acm.judge.service;

import cn.edu.zjnu.acm.judge.config.Constants;
import cn.edu.zjnu.acm.judge.mapper.SystemMapper;
import cn.edu.zjnu.acm.judge.util.SpecialCall;
import javax.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 *
 * @author zhanhb
 */
@Service
@SpecialCall({"index", "manager", "layout/main", "fragment/ga"})
public class SystemService {

    @Autowired
    private SystemMapper systemMapper;

    @Autowired // ensure flyway initialize before this service
    public void setFlywayMigrationInitializer(FlywayMigrationInitializer flywayMigrationInitializer) {
    }

    @Nullable
    public String getUploadPath() {
        return systemMapper.getValueByName(Constants.SystemKey.UPLOAD_PATH);
    }

    @Nullable
    @Cacheable(value = Constants.Cache.SYSTEM, key = "'admin-mail'")
    @SpecialCall({"index", "manager", "layout/main"})
    public String getAdminMail() {
        return systemMapper.getValueByName(Constants.SystemKey.ADMIN_MAIL);
    }

    @Nullable
    @Cacheable(value = Constants.Cache.SYSTEM, key = "'ga'")
    @SpecialCall("fragment/ga")
    public String getGa() {
        return systemMapper.getValueByName(Constants.SystemKey.GA);
    }

    @Nullable
    @Cacheable(value = Constants.Cache.SYSTEM, key = "'data-path'")
    public String getDataFilesPath() {
        return systemMapper.getValueByName(Constants.SystemKey.DATA_FILES_PATH);
    }

    @Nullable
    public String getDeleteTempFile() {
        return systemMapper.getValueByName(Constants.SystemKey.DELETE_TEMP_FILE);
    }

    @Nullable
    public String getWorkingPath() {
        return systemMapper.getValueByName(Constants.SystemKey.WORKING_PATH);
    }

    @Nullable
    public String getResetPasswordTitle() {
        return systemMapper.getValueByName(Constants.SystemKey.RESETPASSWORD_TITLE);
    }

    @Cacheable(value = Constants.Cache.SYSTEM, key = "'page-index'")
    public String getIndex() {
        return systemMapper.getValueByName(Constants.SystemKey.PAGE_INDEX);
    }

    @CacheEvict(value = Constants.Cache.SYSTEM, key = "'page-index'")
    public void setIndex(String index) {
        systemMapper.updateValueByName(Constants.SystemKey.PAGE_INDEX, index);
    }

}
