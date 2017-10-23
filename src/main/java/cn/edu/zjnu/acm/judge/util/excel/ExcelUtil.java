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

import cn.edu.zjnu.acm.judge.exception.BusinessCode;
import cn.edu.zjnu.acm.judge.exception.BusinessException;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import lombok.SneakyThrows;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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
        try (Workbook workbook = resultType.createWorkBook()) {
            buildExcelDocument(type, content.stream(), locale, workbook);
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                workbook.write(out);
                return ResponseEntity.ok().contentType(resultType.getMediaType())
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment")
                        .body(out.toByteArray());
            }
        }
    }

    private static <T> void buildExcelDocument(Class<T> elementType, Stream<T> content, Locale locale, Workbook workbook) {
        Metainfo<T> meta = Metainfo.forType(elementType, locale);
        Sheet sheet = workbook.createSheet(elementType.getSimpleName());
        create(meta.getHeaderAsStream(), sheet.createRow(0));
        AtomicInteger counter = new AtomicInteger();
        content.forEach(entity -> create(meta.getFieldsAsStream().map(field -> fieldGet(field, entity)), sheet.createRow(counter.incrementAndGet())));
    }

    @SneakyThrows(IllegalAccessException.class)
    private static Object fieldGet(Field field, Object target) {
        return field.get(target);
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

    public static <T> List<T> parse(InputStream inputStream, Class<T> type, @Nullable Locale locale) throws IOException {
        Workbook workbook;
        try {
            workbook = WorkbookFactory.create(inputStream);
        } catch (InvalidFormatException ex) {
            throw new BusinessException(BusinessCode.INVALID_EXCEL);
        }
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        return parse(workbook, evaluator, type, locale);
    }

    private static <T> List<T> parse(Workbook workbook, FormulaEvaluator evaluator, Class<T> type, @Nullable Locale locale) {
        Metainfo<T> metainfo = Metainfo.forType(type, locale);
        Sheet sheet = workbook.getSheetAt(workbook.getActiveSheetIndex());
        Iterator<Row> rows = sheet.rowIterator();
        if (!rows.hasNext()) {
            return Collections.emptyList();
        }
        Row firstRow = rows.next();
        Map<Integer, String> columnIndexToFieldName = Maps.newHashMapWithExpectedSize(metainfo.size());
        for (Iterator<Cell> it = firstRow.cellIterator(); it.hasNext();) {
            Cell cell = it.next();
            JsonElement jsonElement = parseAsJsonElement(cell, evaluator);
            if (jsonElement != null) {
                Field field = metainfo.getField(jsonElement.getAsString());
                if (field != null) {
                    String name = field.getName();
                    int index = cell.getColumnIndex();
                    columnIndexToFieldName.put(index, name);
                }
            }
        }
        if (columnIndexToFieldName.isEmpty()) {
            return Collections.emptyList();
        }
        List<T> result = new ArrayList<>(sheet.getLastRowNum() - sheet.getFirstRowNum());
        while (rows.hasNext()) {
            result.add(parseRow(evaluator, rows.next(), columnIndexToFieldName, type));
        }
        return result;
    }

    private static JsonElement parseAsJsonElement(Cell cell, FormulaEvaluator evaluator) {
        switch (cell.getCellTypeEnum()) {
            case NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    return new JsonPrimitive(DateFormatterHolder.formatter.format(cell.getDateCellValue().toInstant()));
                } else {
                    return new JsonPrimitive(cell.getNumericCellValue());
                }
            case STRING:
                return new JsonPrimitive(cell.getStringCellValue());
            case FORMULA:
                CellValue cellValue = evaluator.evaluate(cell);
                switch (cellValue.getCellTypeEnum()) {
                    case NUMERIC:
                        return new JsonPrimitive(cellValue.getNumberValue());
                    case STRING:
                        return new JsonPrimitive(cellValue.getStringValue());
                    case BLANK:
                        return new JsonPrimitive("");
                    case BOOLEAN:
                        return new JsonPrimitive(cellValue.getBooleanValue());
                    case ERROR:
                    default:
                        return null;
                }
            case BLANK:
                return new JsonPrimitive("");
            case BOOLEAN:
                return new JsonPrimitive(cell.getBooleanCellValue());
            case ERROR:
            default:
                return null;
        }
    }

    private static <T> T parseRow(FormulaEvaluator evaluator, Row row, Map<Integer, String> fields, Class<T> type) {
        JsonObject jsonObject = new JsonObject();
        for (Iterator<Cell> it = row.cellIterator(); it.hasNext();) {
            Cell cell = it.next();
            String name = fields.get(cell.getColumnIndex());
            if (name != null) {
                JsonElement cellValue = parseAsJsonElement(cell, evaluator);
                if (cellValue != null) {
                    jsonObject.add(name, cellValue);
                }
            }
        }
        return GsonHolder.GSON.fromJson(jsonObject, type);
    }

    private static interface GsonHolder {

        static Gson GSON = new GsonBuilder().setDateFormat("yyyy-M-d H:mm:ss").create();

    }

    private static interface DateFormatterHolder {

        static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d H:mm:ss", Locale.US)
                .withZone(ZoneId.systemDefault());

    }

}
