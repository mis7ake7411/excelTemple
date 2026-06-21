package com.example.exceltemplate.report.excel;

import com.example.exceltemplate.model.SampleReportDefinition;
import com.example.exceltemplate.model.SampleReportRow;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SampleReportWorkbookBuilderTest {

    private final SampleReportDefinition reportDefinition = new SampleReportDefinition(
            "Custom Sheet",
            Arrays.asList("Column A", "Column B", "Column C"));
    private final SampleReportWorkbookBuilder builder = new SampleReportWorkbookBuilderImpl(reportDefinition);

    @Test
    void shouldCreateWorkbookWithConfiguredSheetNameAndHeaders() throws Exception {
        List<SampleReportRow> rows = Arrays.asList(
                new SampleReportRow(1L, "Alice", "Sales"),
                new SampleReportRow(2L, "Bob", "Engineering"));

        Workbook workbook = builder.build(rows);
        try {
            Sheet sheet = workbook.getSheet("Custom Sheet");

            assertNotNull(sheet);
            assertEquals("Column A", sheet.getRow(0).getCell(0).getStringCellValue());
            assertEquals("Column B", sheet.getRow(0).getCell(1).getStringCellValue());
            assertEquals("Column C", sheet.getRow(0).getCell(2).getStringCellValue());
            assertEquals(1L, (long) sheet.getRow(1).getCell(0).getNumericCellValue());
            assertEquals("Alice", sheet.getRow(1).getCell(1).getStringCellValue());
            assertEquals("Sales", sheet.getRow(1).getCell(2).getStringCellValue());
            assertEquals(3, sheet.getLastRowNum() + 1);
        } finally {
            workbook.close();
        }
    }

    @Test
    void shouldCreateHeaderOnlyWorkbookWhenRowsAreEmpty() throws Exception {
        Workbook workbook = builder.build(Collections.<SampleReportRow>emptyList());
        try {
            Sheet sheet = workbook.getSheet("Custom Sheet");

            assertNotNull(sheet);
            assertEquals("Column A", sheet.getRow(0).getCell(0).getStringCellValue());
            assertEquals(1, sheet.getPhysicalNumberOfRows());
        } finally {
            workbook.close();
        }
    }
}