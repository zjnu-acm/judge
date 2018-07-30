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
package cn.edu.zjnu.acm.judge.config;

/**
 *
 * @author zhanhb
 */
@SuppressWarnings("PublicInnerClass")
public interface Constants {

    interface Cache {

        String LANGUAGE = "languages";
        String LOCALE = "locales";
        String MAIL = "mails";
        String SYSTEM = "system";
        String CONTEST_ONLY = "contest-only";

    }

    interface SystemKey {

        String ADMIN_MAIL = "admin_mail";
        String DATA_FILES_PATH = "data_files_path";
        String DELETE_TEMP_FILE = "delete_temp_file";
        String RESETPASSWORD_TITLE = "resetpassword_title";
        String PAGE_INDEX = "page_index";
        String UPLOAD_PATH = "upload_path";
        String WORKING_PATH = "working_path";
        String GA = "ga";

    }

    interface MineTypes {

        String XLS = "application/vnd.ms-excel";
        String XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    }

}
