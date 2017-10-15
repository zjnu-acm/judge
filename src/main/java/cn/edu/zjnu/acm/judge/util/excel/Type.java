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
package cn.edu.zjnu.acm.judge.util.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.http.MediaType;

/**
 *
 * @author zhanhb
 */
public enum Type {

    XLS(new MediaType("application", "vnd.ms-excel")) {
        @Override
        Workbook createWorkBook() {
            return new HSSFWorkbook();
        }
    },
    XLSX(new MediaType("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
        @Override
        Workbook createWorkBook() {
            return new SXSSFWorkbook();
        }
    };

    private final MediaType mediaType;

    Type(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public String getExtension() {
        return name().toLowerCase();
    }

    abstract Workbook createWorkBook();

}
