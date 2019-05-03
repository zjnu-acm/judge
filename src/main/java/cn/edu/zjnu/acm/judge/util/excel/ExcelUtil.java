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
package cn.edu.zjnu.acm.judge.util.excel;

import cn.edu.zjnu.acm.judge.exception.BusinessCode;
import cn.edu.zjnu.acm.judge.exception.BusinessException;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
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
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

/**
 *
 * @author zhanhb
 */
@SuppressWarnings("UtilityClassWithoutPrivateConstructor")
public class ExcelUtil {

    public static <T> void toResponse(Class<T> type, Collection<T> content,
            @Nonnull Locale locale, Type resultType, String name,
            HttpServletResponse response) throws IOException {
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(locale, "locale");
        try (Workbook workbook = resultType.createWorkBook()) {
            buildExcelDocument(type, content.stream(), locale, workbook);
            if (StringUtils.hasText(name)) {
                String actual = name + "." + resultType.getExtension();
                response.setHeader(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.builder("attachment").filename(actual, StandardCharsets.UTF_8).build().toString());
            } else {
                response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment");
            }
            response.setContentType(resultType.getMediaType().toString());
            try (OutputStream out = response.getOutputStream()) {
                workbook.write(out);
            }
        }
    }

    private static <T> void buildExcelDocument(Class<T> elementType, Stream<T> content, @Nonnull Locale locale, Workbook workbook) {
        MetaInfo meta = MetaInfo.forType(elementType, locale);
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

    public static <T> List<T> parse(InputStream inputStream, Class<T> type, @Nonnull Locale locale) {
        Objects.requireNonNull(locale, "locale");
        boolean support = inputStream.markSupported();
        InputStream is = support ? inputStream : new BufferedInputStream(inputStream);
        try (Workbook workbook = WorkbookFactory.create(is)) {
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            return parse(workbook, evaluator, type, locale);
        } catch (IOException | IllegalStateException ex) {
            throw new BusinessException(BusinessCode.INVALID_EXCEL);
        }
    }

    private static <T> List<T> parse(Workbook workbook, FormulaEvaluator evaluator, Class<T> type, Locale locale) {
        MetaInfo metaInfo = MetaInfo.forType(type, locale);
        Sheet sheet = workbook.getSheetAt(workbook.getActiveSheetIndex());
        Iterator<Row> rows = sheet.rowIterator();
        if (!rows.hasNext()) {
            return Collections.emptyList();
        }
        Row firstRow = rows.next();
        Map<Integer, String> columnIndexToFieldName = Maps.newHashMapWithExpectedSize(metaInfo.size());
        for (Iterator<Cell> it = firstRow.cellIterator(); it.hasNext();) {
            Cell cell = it.next();
            JsonElement jsonElement = parseAsJsonElement(cell, evaluator);
            if (jsonElement != null) {
                Field field = metaInfo.getField(jsonElement.getAsString());
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
        switch (cell.getCellType()) {
            case NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    return new JsonPrimitive(DateFormatterHolder.FORMATTER.format(cell.getDateCellValue().toInstant()));
                } else {
                    return new JsonPrimitive(cell.getNumericCellValue());
                }
            case STRING:
                return new JsonPrimitive(cell.getStringCellValue());
            case FORMULA:
                CellValue cellValue = evaluator.evaluate(cell);
                switch (cellValue.getCellType()) {
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

    private interface GsonHolder {

        Gson GSON = new GsonBuilder().setDateFormat("yyyy-M-d H:mm:ss").create();

    }

    private interface DateFormatterHolder {

        DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-M-d H:mm:ss", Locale.US)
                .withZone(ZoneId.systemDefault());

    }

}
