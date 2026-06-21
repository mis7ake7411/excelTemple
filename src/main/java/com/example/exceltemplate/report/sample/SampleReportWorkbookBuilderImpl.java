package com.example.exceltemplate.report.sample;

import com.example.exceltemplate.model.SampleReportDefinition;
import com.example.exceltemplate.model.SampleReportRow;
import com.example.exceltemplate.report.excel.ReportWorkbookBuilder;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Component;

@Component
public class SampleReportWorkbookBuilderImpl implements SampleReportWorkbookBuilder {

    private final SampleReportDefinition reportDefinition;
    private final SampleReportColumns sampleReportColumns;
    private final ReportWorkbookBuilder reportWorkbookBuilder;

    public SampleReportWorkbookBuilderImpl(
            SampleReportDefinition reportDefinition,
            SampleReportColumns sampleReportColumns,
            ReportWorkbookBuilder reportWorkbookBuilder
    ) {
        this.reportDefinition = reportDefinition;
        this.sampleReportColumns = sampleReportColumns;
        this.reportWorkbookBuilder = reportWorkbookBuilder;
    }

    @Override
    public SXSSFWorkbook build(Iterable<SampleReportRow> rows) {
        return reportWorkbookBuilder.build(
                reportDefinition.getSheetName(),
                sampleReportColumns.getColumns(),
                rows
        );
    }
}