package com.example.exceltemplate.report.excel;

import com.example.exceltemplate.model.SampleReportDefinition;
import com.example.exceltemplate.model.SampleReportRow;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class SampleReportWorkbookBuilderImpl implements SampleReportWorkbookBuilder {

    private final SampleReportDefinition reportDefinition;

    public SampleReportWorkbookBuilderImpl(SampleReportDefinition reportDefinition) {
        this.reportDefinition = reportDefinition;
    }

    @Override
    public SXSSFWorkbook build(Iterable<SampleReportRow> rows) {
        SXSSFWorkbook workbook = new SXSSFWorkbook(100);
        Sheet sheet = workbook.createSheet(reportDefinition.getSheetName());
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle contentStyle = createContentStyle(workbook);

        writeHeaderRow(sheet, headerStyle);
        writeDataRows(sheet, rows, contentStyle);

        return workbook;
    }

    private void writeHeaderRow(Sheet sheet, CellStyle headerStyle) {
        Row row = sheet.createRow(0);
        List<String> headers = reportDefinition.getHeaders();
        for (int i = 0; i < headers.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(headers.get(i));
            cell.setCellStyle(headerStyle);
        }
    }

    private void writeDataRows(Sheet sheet, Iterable<SampleReportRow> rows, CellStyle contentStyle) {
        int rowIndex = 1;
        for (SampleReportRow sampleRow : rows) {
            Row row = sheet.createRow(rowIndex++);
            createCell(row, 0, sampleRow.getId() == null ? null : sampleRow.getId().doubleValue(), contentStyle);
            createCell(row, 1, sampleRow.getName(), contentStyle);
            createCell(row, 2, sampleRow.getDepartment(), contentStyle);
        }
    }

    private void createCell(Row row, int columnIndex, Double value, CellStyle style) {
        Cell cell = row.createCell(columnIndex);
        if (value != null) {
            cell.setCellValue(value);
        }
        cell.setCellStyle(style);
    }

    private void createCell(Row row, int columnIndex, String value, CellStyle style) {
        Cell cell = row.createCell(columnIndex);
        if (value != null) {
            cell.setCellValue(value);
        }
        cell.setCellStyle(style);
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