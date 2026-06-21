package com.example.exceltemplate.service;

import com.example.exceltemplate.common.exception.ErrorCode;
import com.example.exceltemplate.common.exception.ExcelTemplateException;
import com.example.exceltemplate.model.ReportDownload;
import com.example.exceltemplate.report.excel.SampleReportWorkbookBuilder;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Service
public class SampleReportService {

    private static final String FILE_NAME = "sample-report.xlsx";

    private final SampleReportWorkbookBuilder workbookBuilder;
    private final SampleReportDataProvider dataProvider;

    public SampleReportService(SampleReportWorkbookBuilder workbookBuilder, SampleReportDataProvider dataProvider) {
        this.workbookBuilder = workbookBuilder;
        this.dataProvider = dataProvider;
    }

    public String getSampleReportFilename() {
        return FILE_NAME;
    }

    public ReportDownload generateSampleReport() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        writeSampleReport(outputStream);
        return new ReportDownload(FILE_NAME, outputStream.toByteArray());
    }

    public void writeSampleReport(OutputStream outputStream) {
        SXSSFWorkbook workbook = workbookBuilder.build(dataProvider.findRows());
        ExcelTemplateException reportFailure = null;
        try {
            workbook.write(outputStream);
        } catch (IOException ex) {
            reportFailure = createReportGenerationException(ex);
            throw reportFailure;
        } finally {
            try {
                workbook.close();
            } catch (IOException ex) {
                if (reportFailure != null) {
                    reportFailure.addSuppressed(ex);
                } else {
                    throw createReportGenerationException(ex);
                }
            } finally {
                workbook.dispose();
            }
        }
    }

    private ExcelTemplateException createReportGenerationException(IOException ex) {
        return new ExcelTemplateException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.REPORT_GENERATION_FAILED, ex);
    }
}
