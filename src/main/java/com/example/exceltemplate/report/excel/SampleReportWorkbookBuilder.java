package com.example.exceltemplate.report.excel;

import com.example.exceltemplate.model.SampleReportRow;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public interface SampleReportWorkbookBuilder {

    SXSSFWorkbook build(Iterable<SampleReportRow> rows);
}