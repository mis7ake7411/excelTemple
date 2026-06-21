package com.example.exceltemplate.report.excel;

import com.example.exceltemplate.common.ExcelColumn;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Component
public class PoiReportWorkbookBuilder implements ReportWorkbookBuilder {

    @Override
    public <T> SXSSFWorkbook build(
            String sheetName,
            List<ExcelColumn<T>> columns,
            Iterable<T> rows
    ) {
        SXSSFWorkbook workbook = new SXSSFWorkbook(100);
        Sheet sheet = workbook.createSheet(sheetName);

        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle contentStyle = createContentStyle(workbook);

        writeHeaderRow(sheet, columns, headerStyle);
        writeDataRows(sheet, columns, rows, contentStyle);

        return workbook;
    }

    private <T> void writeHeaderRow(
            Sheet sheet,
            List<ExcelColumn<T>> columns,
            CellStyle headerStyle
    ) {
        Row row = sheet.createRow(0);

        for (int i = 0; i < columns.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(columns.get(i).getHeader());
            cell.setCellStyle(headerStyle);
        }
    }

    private <T> void writeDataRows(
            Sheet sheet,
            List<ExcelColumn<T>> columns,
            Iterable<T> rows,
            CellStyle contentStyle
    ) {
        int rowIndex = 1;

        for (T dataRow : rows) {
            Row excelRow = sheet.createRow(rowIndex++);

            for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
                Object value = columns.get(columnIndex).getValue(dataRow);
                createCell(excelRow, columnIndex, value, contentStyle);
            }
        }
    }

    private void createCell(Row row, int columnIndex, Object value, CellStyle style) {
        Cell cell = row.createCell(columnIndex);

        if (value != null) {
            setCellValue(cell, value);
        }

        cell.setCellStyle(style);
    }

    private void setCellValue(Cell cell, Object value) {
        if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
            return;
        }

        if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
            return;
        }

        if (value instanceof Date) {
            cell.setCellValue((Date) value);
            return;
        }

        if (value instanceof LocalDate) {
            cell.setCellValue(value.toString());
            return;
        }

        if (value instanceof LocalDateTime) {
            cell.setCellValue(value.toString());
            return;
        }

        cell.setCellValue(String.valueOf(value));
    }

    private CellStyle createHeaderStyle(SXSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();

        Font font = workbook.createFont();
        font.setBold(true);

        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return style;
    }

    private CellStyle createContentStyle(SXSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        return style;
    }
}
