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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author zhanhb
 */
@SuppressWarnings("UtilityClassWithoutPrivateConstructor")
public class ExcelUtil {

    @SneakyThrows(IOException.class)
    public static <T> ResponseEntity<?> toResponse(Class<T> type, Collection<T> content, Locale locale, Type resultType) {
        try (Workbook workbook = createWorkBook(resultType)) {
            buildExcelDocument(type, content.stream(), locale, workbook);
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                workbook.write(out);
                return ResponseEntity.ok().contentType(resultType.getMediaType())
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment")
                        .body(out.toByteArray());
            }
        }
    }

    private static Workbook createWorkBook(Type resultType) {
        switch (resultType) {
            case XLS:
                return new HSSFWorkbook();
            case XLSX:
                return new XSSFWorkbook();
            default:
                throw new IllegalArgumentException();
        }
    }

    private static <T> void buildExcelDocument(Class<T> elementType, Stream<T> content, Locale locale, Workbook workbook) {
        Metainfo<T> meta = Metainfo.forType(elementType, locale);
        Sheet sheet = workbook.createSheet(elementType.getSimpleName());
        create(meta.getHead(), sheet.createRow(0));
        AtomicInteger counter = new AtomicInteger();
        content.forEach(entity -> create(meta.getFields().map(field -> fieldGet(field, entity)), sheet.createRow(counter.incrementAndGet())));
    }

    private static Object fieldGet(Field field, Object target) {
        try {
            return field.get(target);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw lombok.Lombok.sneakyThrow(ex);
        }
    }

    private static void create(Stream<?> stream, Row row) {
        AtomicInteger counter = new AtomicInteger();
        stream.forEach(value -> {
            if (value != null) {
                if (value instanceof String) {
                    row.createCell(counter.getAndIncrement(), CellType.STRING).setCellValue((String) value);
                } else if (value instanceof Number) {
                    row.createCell(counter.getAndIncrement(), CellType.NUMERIC).setCellValue(((Number) value).doubleValue());
                } else if (value instanceof Boolean) {
                    row.createCell(counter.getAndIncrement(), CellType.BOOLEAN).setCellValue((Boolean) value);
                } else if (value instanceof Date) {
                    row.createCell(counter.getAndIncrement(), CellType.NUMERIC).setCellValue((Date) value);
                } else if (value instanceof Calendar) {
                    row.createCell(counter.getAndIncrement(), CellType.NUMERIC).setCellValue((Calendar) value);
                } else {
                    row.createCell(counter.getAndIncrement(), CellType.ERROR);
                }
            } else {
                row.createCell(counter.getAndIncrement(), CellType.BLANK);
            }
        });
    }

}
