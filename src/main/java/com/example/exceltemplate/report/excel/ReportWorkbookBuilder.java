package com.example.exceltemplate.report.excel;

import com.example.exceltemplate.common.ExcelColumn;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.util.List;

public interface ReportWorkbookBuilder {

    <T> SXSSFWorkbook build(
            String sheetName,
            List<ExcelColumn<T>> columns,
            Iterable<T> rows
    );
}
